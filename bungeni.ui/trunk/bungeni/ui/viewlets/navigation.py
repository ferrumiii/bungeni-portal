# encoding: utf-8

from zope import component
from zope.location.interfaces import ILocation
from zope.dublincore.interfaces import IDCDescriptiveProperties
from zope.container.interfaces import IReadContainer
from zope.security import proxy
from zope.proxy import sameProxiedObjects
from zope.viewlet import viewlet
from zope.app.component.hooks import getSite
from zope.app.pagetemplate import ViewPageTemplateFile
from zope.app.publisher.interfaces.browser import IBrowserMenu
from zope.traversing.browser import absoluteURL
from zope.app.publisher.browser import queryDefaultViewName

from ore.alchemist.interfaces import IAlchemistContainer, IAlchemistContent
from ore.alchemist.model import queryModelDescriptor
from ore.wsgiapp.interfaces import IApplication
from ore.svn.interfaces import ISubversionNode

from alchemist.traversal.managed import ManagedContainerDescriptor

from ploned.ui.menu import make_absolute
from ploned.ui.menu import is_selected
from ploned.ui.interfaces import IStructuralView

from bungeni.core.interfaces import ISection
from bungeni.core import location

def get_parent_chain(context):
    context = proxy.removeSecurityProxy(context)

    chain = []
    while context is not None:
        chain.append(context)
        context = context.__parent__

    return chain

class SecondaryNavigationViewlet(object):
    render = ViewPageTemplateFile("templates/secondary-navigation.pt")
    default_menu = "workspace_navigation"
    
    def update(self):
        context = self.context
        view = self.__parent__.__parent__
        chain = get_parent_chain(context)
        
        length = len(chain)
        if length < 2:
            container = None
        else:
            container = chain[-2]
            assert container.__name__ is not None

            if not IReadContainer.providedBy(container):
                container = None

        if container is None:
            self.items = self.get_menu_items(chain[-1], self.default_menu)
            return

        if length > 2:
            context = chain[-3]
        else:
            context = None
            
        url = absoluteURL(container, self.request)
        self.items = items = self.get_menu_items(
            container, "%s_navigation" % container.__name__)

        if IReadContainer.providedBy(container):
            #XXX should be the same in all containers ?          
            container=proxy.removeSecurityProxy(container)
            for name, item in container.items():
                if context is None:
                    selected = False
                else:
                    selected = context.__name__ == name
                item = proxy.removeSecurityProxy(item)
                if IDCDescriptiveProperties.providedBy(item):
                    title = item.title
                else:
                    props = IDCDescriptiveProperties(item)
                    title = props.title

                items.append({
                    'title': title,
                    'selected': selected,
                    'url': "%s/%s" % (url, name)})

        default_view_name = queryDefaultViewName(container, self.request)
        default_view = component.queryMultiAdapter(
            (container, self.request), name=default_view_name)

        if hasattr(default_view, "title"):
            items.insert(0, {
                'title': default_view.title,
                'selected': sameProxiedObjects(container, self.context),
                'url': url})

    def get_menu_items(self, container, name):
        #XXX ad hoc fix - todo: write a utility for this navigation structure
        try:    
            menu = component.getUtility(IBrowserMenu, name=name)        
            items = menu.getMenuItems(container, self.request)
        except:
            return
            
        local_url = absoluteURL(container, self.request)
        site_url = absoluteURL(getSite(), self.request)
        request_url = self.request.getURL()

        default_view_name = queryDefaultViewName(container, self.request)
        selection = None
        
        for item in sorted(items, key=lambda item: item['action'], reverse=True):
            action = item['action']

            if default_view_name == action.lstrip('@@'):
                url = local_url
                if selection is None:
                    selected = sameProxiedObjects(container, self.context)
            else:
                url = make_absolute(action, local_url, site_url)
                if selection is None:
                    selected = is_selected(item, action, request_url)

            item['url'] = url
            item['selected'] = selected and u'selected' or u''

            if selected:
                # self is marker
                selection = self
                selected = False
                
        return items

class GlobalSectionsViewlet(viewlet.ViewletBase):
    render = ViewPageTemplateFile( 'templates/sections.pt' )
    selected_portal_tab = None
    
    def update(self):
        base_url = absoluteURL(getSite(), self.request)
        item_url = self.request.getURL()

        assert item_url.startswith(base_url)
        path = item_url[len(base_url):]

        self.portal_tabs = []
        seen = set()
        menu = component.getUtility(IBrowserMenu, "site_actions")
        for item in menu.getMenuItems(self.context, self.request):
            if item['action'] in seen:
                continue
            seen.add(item['action'])
            item['url'] = item.setdefault('url', base_url + item['action'])
            item['id'] = item['action'].strip('/')
            item['name'] = item['title']
            self.portal_tabs.append(item)
            if path.startswith(item['action']):
                self.selected_portal_tab = item['id']
        
class BreadCrumbsViewlet(viewlet.ViewletBase):
    """Breadcrumbs.
    
    Render the breadcrumbs to show a user his current location.
    """

    render = ViewPageTemplateFile( 'templates/breadcrumbs.pt' )        

    def __init__( self,  context, request, view, manager ):        
        self.context = context
        self.request = request
        self.__parent__= view
        self.manager = manager
        self.path = []
        self.site_url = absoluteURL(getSite(), self.request)
        self.user_name = ''

    def _get_path(self, context):
        """
        Return the current path as a list
        """

        descriptor = None
        name = None 
        path = []

        context = proxy.removeSecurityProxy( context )
        if context.__parent__ is not None:
            path.extend(
                self._get_path(context.__parent__))

        url = absoluteURL(context, self.request)
        
        if  IAlchemistContent.providedBy(context):
            if IDCDescriptiveProperties.providedBy(context):
                title = context.title
            else:
                props = IDCDescriptiveProperties(context, None)
                if props is not None:
                    title = props.title
                else:
                    title = context.short_name
                    
            path.append({
                'name' : title,
                'url' : url})
            
        elif IAlchemistContainer.providedBy(context):                        
            domain_model = context._class 
            try:
                descriptor = queryModelDescriptor( domain_model )
            except:
                descriptor = None
                name = ""                
            if descriptor:
                name = getattr(descriptor, 'container_name', None)
                if name is None:
                    name = getattr(descriptor, 'display_name', None)
            if not name:
                name = getattr( context, '__name__', None)  
            path.append({
                'name' : name,
                'url' : url,
                })

        elif ILocation.providedBy(context) and \
             IDCDescriptiveProperties.providedBy(context):
            path.append({
                'name' : context.title,
                'url' : url,
                })
        elif ILocation.providedBy(context) and \
            ISubversionNode.providedBy(context):
            path.append({
                'name' : context.__name__,
                'url' : url,
            })

        return path
        
    def update(self):
        self.path = self._get_path(self.context)

        # if the view is a location, append this to the breadcrumbs
        if ILocation.providedBy(self.__parent__) and \
               IDCDescriptiveProperties.providedBy(self.__parent__):
            self.path.append({
                'name': self.__parent__.title,
                'url': None,
                })

        try:
            self.user_name = self.request.principal.login          
        except:
            pass

class NavigationTreeViewlet( viewlet.ViewletBase ):
    """Render a navigation tree."""

    render = ViewPageTemplateFile( 'templates/bungeni-navigation-tree.pt' )
    template = ViewPageTemplateFile('templates/contained-constraint-navigation.pt')
    path = ()
    
    def __new__(cls, context, request, view, manager):
        # we have both primary and secondary navigation, so we won't
        # show the navigation tree unless we're at a depth > 2
        chain = get_parent_chain(context)[:-2]
        if not chain:
            return

        # we require the tree to begin with a container object
        if not IReadContainer.providedBy(chain[-1]):
            return

        subcontext = chain[-1]
        if (len(chain) > 1 or
            IReadContainer.providedBy(subcontext) and not
            IAlchemistContainer.providedBy(subcontext) and len(subcontext)):
            inst = object.__new__(cls, context, request, view, manager)
            inst.chain = chain
            return inst

    def __init__(self, context, request, view, manager):
        self.context = context
        self.request = request
        self.__parent__= view
        self.manager = manager
        self.name = ''

    def update(self):
        """Creates a navigation tree for ``context``.

        Recursively, by visiting the parent chain in reverse order,
        the tree is built. The siblings of managed containers are
        included.
        """

        chain = list(self.chain)
        self.nodes = self.expand(chain, include_siblings=False)

    def expand(self, chain, include_siblings=True):
        if len(chain) == 0:
            return ()

        context = chain.pop()
        items = []

        if IApplication.providedBy(context):
            items.extend(self.expand(chain))

        elif IAlchemistContent.providedBy(context):
            url = absoluteURL(context, self.request)
            if IDCDescriptiveProperties.providedBy(context):
                title = context.title
            else:
                props = IDCDescriptiveProperties(context, None)
                if props is not None:
                    title = props.title
                else:
                    title = context.short_name

            selected = len(chain) == 0
            
            if chain:
                nodes = self.expand(chain)
            else:
                kls = context.__class__
                containers = [
                    (key, getattr(context, key))
                    for key, value in kls.__dict__.items()
                    if isinstance(value, ManagedContainerDescriptor)]
                nodes = []
                self.expand_containers(nodes, containers, url, chain, None)

            items.append(
                {'title': title,
                 'url': url,
                 'current': True,
                 'selected': selected,
                 'kind': 'content',
                 'nodes': nodes,
                 })

        elif IAlchemistContainer.providedBy(context):
            # loop through all managed containers of the parent
            # object, and include the present container as the
            # 'current' node.
            parent = context.__parent__
            assert parent is not None
            url = absoluteURL(parent, self.request)

            # append managed containers as child nodes
            kls = type(proxy.removeSecurityProxy(parent))

            if include_siblings is True:
                if IApplication.providedBy(parent):
                    containers = [
                        (name, parent[name])
                        for name in location.model_to_container_name_mapping.values()
                        if name in parent]
                elif IReadContainer.providedBy(parent):
                    containers = list(parent.items())
                else:
                    containers = [
                        (key, getattr(parent, key))
                        for key, value in kls.__dict__.items()
                        if isinstance(value, ManagedContainerDescriptor)]
            else:
                containers = [(context.__name__, context)]
                
            self.expand_containers(items, containers, url, chain, context)

        elif ILocation.providedBy(context):
            url = absoluteURL(context, self.request)
            props = IDCDescriptiveProperties.providedBy(context) and \
                context or IDCDescriptiveProperties(context)
            props = proxy.removeSecurityProxy(props)

            selected = len(chain) == 0
            if selected and IReadContainer.providedBy(context):
                nodes = []
                self.expand_containers(nodes, context.items(), url, chain, context)
            else:
                nodes = self.expand(chain)
            
            items.append(
                {'title': props.title,
                 'url': url,
                 'current': True,
                 'selected': selected,
                 'kind': 'location',
                 'nodes': nodes,
                 })

        elif IReadContainer.providedBy(context):
            items.extend(self.expand(chain))

        return items

    def expand_containers(self, items, containers, url, chain=(), context=None):
        seen_context = False
        current = False
        
        for key, container in containers:
            if IAlchemistContainer.providedBy(container):
                descriptor = queryModelDescriptor(
                    proxy.removeSecurityProxy(container).domain_model)
                if descriptor:
                    name = getattr(descriptor, 'container_name', None)
                    if name is None:
                        name = getattr(descriptor, 'display_name', None)
                        
                if not name:
                    name = container.domain_model.__name__
            else:
                assert IDCDescriptiveProperties.providedBy(container)
                container = proxy.removeSecurityProxy(container)
                name = container.title

            if context is not None:
                current = container.__name__ == context.__name__

            selected = len(chain) == 0 and current

            if current:
                seen_context = True
                nodes = self.expand(chain)
            else:
                nodes = ()

            items.append(
                {'title': name,
                 'url': "%s/%s" % (url.rstrip('/'), key),
                 'current': current,
                 'selected': selected,
                 'kind': 'container',
                 'nodes': nodes,
                 })
