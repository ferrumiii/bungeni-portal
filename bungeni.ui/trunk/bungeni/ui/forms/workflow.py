from zope import component
from zope.formlib import form
from bungeni.ui.i18n import _

from ore.workflow import interfaces
from alchemist.ui.core import handle_edit_action
from zope.security.proxy import removeSecurityProxy
from zope.traversing.browser import absoluteURL

def bindTransitions( form_instance, transitions, wf_name=None, wf=None):
    """ bind workflow transitions into formlib actions 
    """

    if wf_name:
        success_factory = lambda tid: TransitionHandler( tid, wf_name )
    else:
        success_factory = TransitionHandler

    actions = []
    for tid in transitions:
        d = {}
        if success_factory:
            d['success'] = success_factory( tid )
        if wf is not None:
            action = form.Action( _(unicode(wf.getTransitionById( tid ).title)), **d )
        else:
            action = form.Action( tid, **d )
        action.form = form_instance
        action.__name__ = "%s.%s"%(form_instance.prefix, action.__name__)
        
        actions.append( action )  
    return actions

def createVersion(context, comment = ''):
    """Create a new version of an object and return it."""
    instance = removeSecurityProxy(context)
    #XXX
    #versions = IVersioned(instance)
    #if not comment:
    #    comment =''
    #versions.create(u'New version created upon edit.' + comment)

#################################
# workflow transition 2 formlib action bindings
class TransitionHandler( object ):

    def __init__( self, transition_id, wf_name=None):
        self.transition_id = transition_id
        self.wf_name = wf_name


    def __call__( self, form, action, data ):
        """
        save data make version and fire transition
        """
        context = getattr( form.context, '_object', form.context )
        notes = None
        if self.wf_name:
            info = component.getAdapter( context, interfaces.IWorkflowInfo, self.wf_name )            
        else:
            info = interfaces.IWorkflowInfo( context ) 
        if data.has_key('note'):
            notes = data['note']     
        else:
            notes=''            
        result = handle_edit_action( form, action, data )
        if form.errors: 
            return result
        else:         
            createVersion(form.context, notes)                                                        
            info.fireTransition( self.transition_id, notes )       
            url = absoluteURL( form.context, form.request )  
            return form.request.response.redirect( url )             
        #form.setupActions()
