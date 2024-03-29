# -*- coding: utf-8 -*-
#
# File: Question.py
#
# Copyright (c) 2007 by []
# Generator: ArchGenXML Version 1.6.0-beta-svn
#            http://plone.org/products/archgenxml
#
# GNU General Public License (GPL)
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# as published by the Free Software Foundation; either version 2
# of the License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
# 02110-1301, USA.
#

__author__ = """Jean Jordaan <jean.jordaan@gmail.com>"""
__docformat__ = 'plaintext'

from AccessControl import ClassSecurityInfo
from Products.Archetypes.atapi import *
from zope import interface
from Products.Bungeni.events.ParliamentaryEvent import ParliamentaryEvent
from Products.AuditTrail.interfaces.IAuditable import IAuditable
from Products.Relations.field import RelationField
from Products.Bungeni.config import *

##code-section module-header #fill in your manual code here
##/code-section module-header

schema = Schema((

    BooleanField(
        name='requireWrittenAnswer',
        widget=BooleanField._properties['widget'](
            label="Require written answer?",
            label_msgid='Bungeni_label_requireWrittenAnswer',
            i18n_domain='Bungeni',
        )
    ),

    RelationField(
        name='respondents',
        widget=ReferenceWidget(
            label='Respondents',
            label_msgid='Bungeni_label_respondents',
            i18n_domain='Bungeni',
        ),
        multiValued=1,
        relationship='Question_MemberOfParliament'
    ),

),
)

##code-section after-local-schema #fill in your manual code here
##/code-section after-local-schema

Question_schema = BaseFolderSchema.copy() + \
    getattr(ParliamentaryEvent, 'schema', Schema(())).copy() + \
    schema.copy()

##code-section after-schema #fill in your manual code here
##/code-section after-schema

class Question(BaseFolder, ParliamentaryEvent):
    """
    """
    security = ClassSecurityInfo()
    __implements__ = (getattr(BaseFolder,'__implements__',()),) + (getattr(ParliamentaryEvent,'__implements__',()),)
    # zope3 interfaces
    interface.implements(IAuditable)

    # This name appears in the 'add' box
    archetype_name = 'Question'

    meta_type = 'Question'
    portal_type = 'Question'
    allowed_content_types = ['Response'] + list(getattr(ParliamentaryEvent, 'allowed_content_types', []))
    filter_content_types = 1
    global_allow = 1
    #content_icon = 'Question.gif'
    immediate_view = 'base_view'
    default_view = 'base_view'
    suppl_views = ()
    typeDescription = "Question"
    typeDescMsgId = 'description_edit_question'

    _at_rename_after_creation = True

    schema = Question_schema

    ##code-section class-header #fill in your manual code here
    ##/code-section class-header

    # Methods


registerType(Question, PROJECTNAME)
# end of class Question

##code-section module-footer #fill in your manual code here
##/code-section module-footer



