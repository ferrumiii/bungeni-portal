from zope.publisher.browser import BrowserView
from zope.app.pagetemplate import ViewPageTemplateFile
from zope.formlib import form
from zope.formlib import namedtemplate
from zope import schema
from zope import interface
from bungeni.ui.i18n import _
from bungeni.ui.forms.common import set_widget_errors
from ore.alchemist import Session
from bungeni.transcripts import domain
from bungeni.transcripts import orm
class MainView(BrowserView):
    __call__ = ViewPageTemplateFile("main.pt")
    
    def user_agent(self):
        return self.request.get('HTTP_USER_AGENT','')

class Add(form.PageForm):
    class IReportingForm(interface.Interface):
        start_time = schema.TextLine(
            title=_(u"Start Time"),
            required=True)
        end_time = schema.TextLine(
            title=_(u"EndTime"),
            required=True)
        text = schema.Text(title=u'Speech Text',
                   required=False,
                   )
        person = schema.TextLine(title=u'Person',
                   required=False,
                   )
        
    template = namedtemplate.NamedTemplate('alchemist.form')
    form_fields = form.Fields(IReportingForm)
    
    def setUpWidgets(self, ignore_request=False):
        class context:
            start_time = None
            end_time = None
            text = None
            person = None
            
        self.adapters = {
            self.IReportingForm: context
            }
        self.widgets = form.setUpEditWidgets(
            self.form_fields, self.prefix, self.context, self.request,
            adapters=self.adapters, ignore_request=ignore_request)
    
    def update(self):
        self.status = self.request.get('portal_status_message', '')
        super(Add, self).update()
        set_widget_errors(self.widgets, self.errors)

    def validate(self, action, data):    
        errors = super(Add, self).validate(action, data)
        return errors
        
    @form.action(_(u"Save"))  
    def handle_preview(self, action, data):
        session = Session()
        transcript = domain.Transcript()
        transcript.start_time =  data['start_time']                    
        transcript.end_time =  data['end_time']                      
        transcript.text = data['text']
        transcript.person = data['person'] 
        session.add(transcript)
        session.flush()
