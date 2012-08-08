# bungeni - http://www.bungeni.org/
# Parliamentary and Legislative Information System
# Copyright (C) 2010 UN/DESA - http://www.un.org/esa/desa/
# Licensed under GNU GPL v2 - http://www.gnu.org/licenses/gpl-2.0.txt

'''Utilities to help with working with queries on the domain model

$Id$
'''
log = __import__("logging").getLogger("bungeni.models.utils")

import collections

import zope.component
import zope.schema
from zope.security.interfaces import NoInteraction
from zope.security.management import getInteraction
from zope.security.proxy import removeSecurityProxy
from zope.publisher.interfaces import IRequest
from zope.dublincore.interfaces import IDCDescriptiveProperties

import sqlalchemy as rdb
from sqlalchemy import sql
from sqlalchemy.orm import (eagerload, RelationshipProperty, ColumnProperty, 
    class_mapper
)
from sqlalchemy.types import Binary

from bungeni.alchemist import Session, utils
from bungeni.alchemist.interfaces import (IAlchemistContainer, 
    IAlchemistContent
)
import domain, schema, delegation
from bungeni.core.workflow.states import get_head_object_state_rpm

# !+ move "contextual" utils to ui.utils.contextual

# !+rename get_request_principal(), get_request_principal_id()
def get_principal():
    """ () -> either(zope.security.interfaces.IGroupAwarePrincipal, None)
    """
    interaction = getInteraction()
    for participation in interaction.participations:
        if IRequest.providedBy(participation):
            return participation.principal

def get_principal_id():
    """ () -> either(str, None), login name of current principal, or None.
    """
    principal = get_principal()
    if principal is not None:
        return principal.id


# !+rename get_request_user(), get_request_user_id()
def get_db_user(context=None):
    """ get the logged in user 
    Note: context is not used, but accommodated for as a dummy optional input 
    parameter to allow usage of this utility in e.g.
    bungeni.core.app: container_getter(get_db_user, "questions")
    """
    principal_id = get_principal_id()
    session = Session()
    query = session.query(domain.User).filter(domain.User.login == principal_id)
    results = query.all()
    if len(results) == 1:
        return results[0]

def get_db_user_id(context=None):
    """ get the (numerical) user_id for the currently logged in user
    """
    db_user = get_db_user(context)
    if db_user is not None:
        return db_user.user_id

def is_current_or_delegated_user(user):
    """Is this user (a delegation of) the currently logged user?
    """
    current_user = get_db_user()
    # Only if there is a user logged in!
    if current_user:
        if current_user == user:
            return True
        for d in delegation.get_user_delegations(current_user.user_id):
            if d == user:
                return True

from zope.securitypolicy.interfaces import IPrincipalRoleMap
def get_prm_owner_principal_id(context):
    """Get the principal_id, if any, of the bungeni.Owner for context.
    Raise ValueError if multiple, return None if none.
    """
    principal_ids = [ pid for (pid, setting) in 
        IPrincipalRoleMap(context).getPrincipalsForRole("bungeni.Owner") 
        if setting ]
    len_pids = len(principal_ids)
    if len_pids > 1:
        # multiple Owner roles, force exception
        raise ValueError("Ambiguous, multiple Owner roles assigned.")
    elif len_pids == 1:
        return principal_ids[0]

def get_user_for_principal_id(principal_id):
    """Get the User for this principal_id.
    """
    # !+group_principal(mr, may-2012) and when principal_id is for a group?
    query = Session().query(domain.User).filter(domain.User.login == principal_id)
    try:
        return query.one()
    except rdb.exc.InvalidRequestError:
        # !+ sqlalchemy.orm.exc NoResultFound, MultipleResultsFound
        return None


# contextual
def get_current_parliament(context=None):
    from bungeni.core import globalsettings
    return globalsettings.get_current_parliament()


def container_getter(parent_container_or_getter, name, query_modifier=None):
    """Get a child container with name from the specified parent 
    container/container_callback."""
    #from bungeni.alchemist.interfaces import IAlchemistContainer
    # !+ the parent container SHOULD be implementing IAlchemistContainer but it
    # does not seem to! As a best alternative, that is close but conceptually 
    # not quite the same, we check for IBungeniGroup
    from bungeni.models.interfaces import IBungeniGroup
    def func(context):
        if IBungeniGroup.providedBy(parent_container_or_getter):
            parent_container = parent_container_or_getter
        else:
            parent_container = parent_container_or_getter(context)
        #
        try:
            c = getattr(parent_container, name)
        except AttributeError:
            # the container we need is not there, data may be missing in the db
            from zope.publisher.interfaces import NotFound
            raise NotFound(context, name)
        c.setQueryModifier(sql.and_(c.getQueryModifier(), query_modifier))
        return c
    func.__name__ = "get_%s_container" % name
    return func


def get_current_parliament_governments(parliament=None):
    if parliament is None:
        parliament = get_current_parliament()
    governments = Session().query(domain.Government).filter(
            sql.and_(domain.Government.parent_group_id == parliament.group_id,
                     domain.Government.status == 'active')).all()
    return governments

def get_current_parliament_committees(parliament=None):
    if parliament is None:
        parliament = get_current_parliament(None)
    committees = Session().query(domain.Committee).filter(
            sql.and_(domain.Committee.parent_group_id == parliament.group_id,
                     domain.Committee.status == 'active')).all()
    return committees

def get_all_group_ids_in_parliament(parliament_id):
    """ get all groups (group_ids) in a parliament
    including the sub (e.g. ministries) groups """
    session = Session()
    group_ids = [parliament_id, ]
    query = session.query(domain.Group).filter(
        domain.Group.parent_group_id == parliament_id).options(
            eagerload('contained_groups'),
            )
    results = query.all()
    for result in results:
        group_ids.append(result.group_id)
        for group in result.contained_groups:
            group_ids.append(group.group_id)
    return group_ids


def get_ministries_for_user_in_government(user_id, government_id):
    """Get the ministries where user_id is a active member."""
    session = Session()
    query = session.query(domain.Ministry).join(domain.Minister).filter(
        rdb.and_(
            schema.user_group_memberships.c.user_id == user_id,
            schema.groups.c.parent_group_id == government_id,
            schema.groups.c.status == 'active',
            schema.user_group_memberships.c.active_p == True))
    return query.all()
def get_ministry_ids_for_user_in_government(user_id, government_id):
    """Get the ministry ids where user_id is a active member."""
    return [ ministry.group_id for ministry in
             get_ministries_for_user_in_government(user_id, government_id) ]
    '''
    # alternative approach to get ministry_ids: 
    connection = session.connection(domain.Group)
    ministries_ids_query = rdb.select([schema.groups.c.group_id],
        from_obj=[
        rdb.join(schema.groups, schema.user_group_memberships,
        schema.groups.c.group_id == schema.user_group_memberships.c.group_id),
        ],
        whereclause =
            rdb.and_(
            schema.user_group_memberships.c.user_id==user_id,
            schema.groups.c.parent_group_id==government_id,
            schema.groups.c.status=='active',
            schema.user_group_memberships.c.active_p==True))
    session = Session()
    connection = session.connection(domain.Group)
    return [ group_id[0] for group_id in 
             connection.execute(ministries_ids_query) ]
    '''


def get_groups_held_for_user_in_parliament(user_id, parliament_id):
    """ get the Offices (functions/titles) held by a user """
    session = Session()
    connection = session.connection(domain.Group)
    group_ids = get_all_group_ids_in_parliament(parliament_id)
    #!+MODELS(miano, 16 march 2011) Why are these queries hardcorded?
    #TODO:Fix this
    offices_held = rdb.select([schema.groups.c.short_name,
        schema.groups.c.full_name,
        schema.groups.c.type,
        schema.title_types.c.title_name,
        schema.member_titles.c.start_date,
        schema.member_titles.c.end_date,
        schema.user_group_memberships.c.start_date,
        schema.user_group_memberships.c.end_date,
        ],
        from_obj=[
        rdb.join(schema.groups, schema.user_group_memberships,
        schema.groups.c.group_id == schema.user_group_memberships.c.group_id
            ).outerjoin(
            schema.member_titles, schema.user_group_memberships.c.membership_id ==
            schema.member_titles.c.membership_id).outerjoin(
                schema.title_types,
                schema.member_titles.c.title_type_id ==
                    schema.title_types.c.title_type_id)],
            whereclause=rdb.and_(
                schema.groups.c.group_id.in_(group_ids),
                schema.user_group_memberships.c.user_id == user_id),
            order_by=[schema.user_group_memberships.c.start_date,
                        schema.user_group_memberships.c.end_date,
                        schema.member_titles.c.start_date,
                        schema.member_titles.c.end_date]
            )
    o_held = connection.execute(offices_held)
    return o_held

def get_group_ids_for_user_in_parliament(user_id, parliament_id):
    """ get the groups a user is member of for a specific parliament """
    session = Session()
    connection = session.connection(domain.Group)
    group_ids = get_all_group_ids_in_parliament(parliament_id)
    my_groups = rdb.select([schema.user_group_memberships.c.group_id],
        rdb.and_(schema.user_group_memberships.c.active_p == True,
            schema.user_group_memberships.c.user_id == user_id,
            schema.user_group_memberships.c.group_id.in_(group_ids)),
        distinct=True)
    my_group_ids = []
    for group_id in connection.execute(my_groups):
        my_group_ids.append(group_id[0])
    return my_group_ids

def get_parliament_for_group_id(group_id):
    if group_id is None:
        return None
    session = Session()
    group = session.query(domain.Group).get(group_id)
    if group.type == 'parliament':
        return group
    else:
        return get_parliament_for_group_id(group.parent_group_id)


# serialization utilities

def get_permissions_dict(permissions):
    results= []
    for x in permissions:
        # !+XML pls read the styleguide
        results.append({"role": x[2], 
            "permission": x[1], 
            "setting": x[0] and "Allow" or "Deny"})
    return results

def obj2dict(obj, depth, parent=None, include=[], exclude=[]):
    """ Returns dictionary representation of a domain object.
    """
    result = {}
    obj = removeSecurityProxy(obj)
    descriptor = None
    if IAlchemistContent.providedBy(obj):
        try:
            descriptor = utils.get_descriptor(obj)
        except KeyError:
            log.error("Could not get descriptor for IAlchemistContent %r", obj)
    
    # Get additional attributes
    for name in include:
        value = getattr(obj, name, None)
        if value is None:
            continue
        if not name.endswith("s"):
            name += "s"
        if isinstance(value, collections.Iterable):
            res = []
            # !+ allowance for non-container-api-conformant alchemist containers
            if IAlchemistContainer.providedBy(value):
                value = value.values()
            for item in value:
                i = obj2dict(item, 0)
                if name == "versions":
                    permissions = get_head_object_state_rpm(item).permissions
                    i["permissions"] = get_permissions_dict(permissions)
                res.append(i)
            result[name] = res
        else:
            result[name] = value
    
    # Get mapped attributes
    mapper = class_mapper(obj.__class__)
    for property in mapper.iterate_properties:
        if property.key in exclude:
            continue
        value = getattr(obj, property.key)
        if value == parent:
            continue
        if value is None:
            continue
        
        if isinstance(property, RelationshipProperty) and depth > 0:
            if isinstance(value, collections.Iterable):
                result[property.key] = []
                for item in value:
                    result[property.key].append(obj2dict(item, depth-1, 
                            parent=obj,
                            include=[],
                            exclude=exclude + ["changes"]
                    ))
            else:
                result[property.key] = obj2dict(value, depth-1, 
                    parent=obj,
                    include=[],
                    exclude=exclude + ["changes"]
                )
        else:
            if isinstance(property, RelationshipProperty):
                continue
            elif isinstance(property, ColumnProperty):
                columns = property.columns
                if len(columns) == 1:
                    if columns[0].type.__class__ == Binary:
                        continue
            if descriptor:
                columns = property.columns
                is_foreign = False
                if len(columns) == 1:
                    if len(columns[0].foreign_keys):
                        is_foreign = True
                if (not is_foreign) and (property.key in descriptor.keys()):
                    field = descriptor.get(property.key)
                    if (field and field.property and
                        (field.property.__class__ == zope.schema.Choice)):
                                factory = field.property.vocabulary
                                if factory is None:
                                    vocab_name = getattr(field.property, 
                                        "vocabularyName", None)
                                    factory = zope.component.getUtility(
                                        zope.schema.interfaces.IVocabularyFactory,
                                        vocab_name
                                    )
                                #!+VOCABULARIES(mb, Aug-2012)some vocabularies
                                # expect an interaction to generate values
                                # todo - update these vocabularies to work 
                                # with no request e.g. in notification threads 
                                try:
                                    vocabulary = factory(obj)                             
                                    display_name = vocabulary.getTerm(value).title
                                except NoInteraction:
                                    log.error("This vocabulary %s expects an"
                                        "interaction to generate terms.",
                                        factory
                                    )
                                    #try to use dc adapter lookup
                                    display_name = str(value)
                                    try:
                                        _prop = mapper.get_property_by_column(
                                            property.columns[0])
                                        _prop_value = getattr(obj, _prop.key)
                                        dc = IDCDescriptiveProperties(
                                            _prop_value, None)
                                        if dc:
                                            display_name = (
                                                IDCDescriptiveProperties(
                                                    _prop_value).title
                                            )
                                    except KeyError:
                                        log.warn("No display text found for %s" 
                                            " on object %s. Unmapped in orm.",
                                            property.key, obj
                                        )
                                result[property.key] = dict(
                                    name=property.key,
                                    value=value,
                                    displayAs=display_name
                                )
                                continue
            result[property.key] = value
    return result


