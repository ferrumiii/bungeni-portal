
from zope import interface, schema, lifecycleevent
from zope.component.interfaces import IObjectEvent, ObjectEvent
from zope.app.container.interfaces import IContainer
from ore.alchemist.interfaces import IAlchemistContent
from ore.wsgiapp.interfaces import IApplication
from i18n import _



DEBUG = True
ENABLE_LOGGING = False
ENABLE_EVENT_LOGGING = False

if ENABLE_EVENT_LOGGING:
    import eventlog

class IBungeniApplication( IApplication ):
    """
    Bungeni Application
    """

class IBungeniAdmin( IContainer ):
    """
    Admin Container
    """

class IAdminUserContainer( interface.Interface ):
    """
    a container that returns object for the admin ui, marked with admin interface markers
    """

class IAdminGroupContainer( interface.Interface ):
    pass

class IUserAdmin( interface.Interface ):
    """
    marker interface attached to user objects viewed in the admin for admin views
    """
class IGroupAdmin( interface.Interface ):
    """
    marker interface attached to user objects viewed in the admin for admin views    
    """

class IBungeniUser( interface.Interface ):
    """
    a user in bungeni
    """     
    
class IBungeniGroup( interface.Interface ):
    """
    a group in bungeni
    """

class IBungeniContent( interface.Interface ):
    """
    parliamentary content
    """

class IQuestion( IBungeniContent ):
    """ Parliamentary Question
    """

class IBill( IBungeniContent ):
    """ Parliamentary Bill
    """

class IMotion( IBungeniContent ):
    """ Parliamentary Motion
    """

class IGroupSitting(interface.Interface):
    pass

class IItemSchedule(interface.Interface):
    pass

class IResponse( IBungeniContent ):
    """
    response to a question
    """


class ITabledDocument( interface.Interface ):
    """
    tabled document
    """


class IBungeniSetup( interface.Interface ):

    def setUp( app ):
        """
        setup the application on server start
        """

class IBungeniSettings( interface.Interface ):
    speakers_office_email = schema.TextLine(title=_(u"Speaker's Office Email"), 
                                            default=u"speakers.office@parliament.go.tld")
    speakers_office_notification = schema.Bool( title=_(u"Speaker's Office Notification"),
                                                description=_(
                                                    u"true if the Speakers office wants to be alerted by mail" \
                                                    u"whenever a bill, motion, question is submitted" ),
                                                default=True
                                                )
    clerks_office_notification = schema.Bool( title=_("Clerk's Office Notification"),
                                              description=_(
                                                  u"true if the clerks office wants to be alerted by mail"
                                                  u"whenever a bill, motion, question is submitted"),
                                              default=True
                                              )
    clerks_office_email   = schema.TextLine(title=_(u"Clerks's Office Email"),
                                            default=u"clerks.office@parliament.go.tld" )
    
    administrators_email  = schema.TextLine(title=_(u"Administrator's Email") )
    question_submission_allowed = schema.Bool( title=_(u"Allow Question Submission"), default=True )    
    days_to_defer_question = schema.Int(title=_(u"Days to Defer Question"),
                                        description=_(u"Time after which admissible questions are automatically deferred"),
                                        default=10 )
    days_to_notify_ministry_unanswered = schema.Int(title=_(u"Days to Notify Ministry of Pending Response"),
                                                    description=_(u"Timeframe after which the clerksoffice and the ministry is alerted that" \
                                                                  u"questions that are pending response are not yet answered"
                                                                  )
                                                    )
    days_before_question_schedule = schema.Int( title=_(u"Days before question scheduled"), default=3 )
    days_before_bill_schedule = schema.Int( title=_(u"Days before question scheduled"), default=3 )    
    max_questions_sitting = schema.Int( title=_(u"Max Questions Per Sitting Per MP"), default=15  )
    max_mp_questions_sitting = schema.Int( title=_(u"Max Questions Per Sitting Per MP"), default=1  )

class IBungeniUserSettings( interface.Interface ):

    # examples
    email_delivery = schema.Bool( title=_(u"Email Notifications Enabled?"), default=True  )
    
class IAssignment( IAlchemistContent ):

    content = schema.Object( IAlchemistContent )
    context = schema.Object( IAlchemistContent )
    title = schema.TextLine(title=_(u"Name of the Assignment"))
    start_date = schema.Date(title=_(u"Start Date of the Assignment"))
    end_date = schema.Date(title=_(u"End Date of the Assignment"))    
    type = schema.TextLine(title=_(u"Assignment Type"), readonly=True)
    status = schema.TextLine(title=_(u"Status"), readonly=True)
    notes  = schema.Text( title=_(u"Notes"), description=_(u"Notes"))
    
class IContentAssignments( interface.Interface ):
    """ assignments of this content to different contexts""" 

    def __iter__(  ):
        """ iterate over assignments for this context """

class IContextAssignments( interface.Interface ):
    """ content assignments for the given context/group """
    
    def __iter__(  ):
        """ iterate over assignments for this context """

class IAssignmentFactory( interface.Interface ):
    """ assignment factory """
    
    def new( **kw ):
        """
        create a new assignment
        """
    
        
class IFileAttachments( interface.Interface ):
    """
    for content that wants to have a locally scoped svn directory for file storage
    """
    files = interface.Attribute('an ore.svn/library directory instance')
    
class IDirectoryLocation( interface.Interface ):

    repo_path = schema.ASCIILine()
    object_id = schema.Int()
    object_type = schema.ASCIILine()
    #directory = schema.Object( interfaces.I)

class IProxiedDirectory( interface.Interface ):
    """ an interface for a contained directory we can attach menu links
        to that point back to our parent
    """
    
class IVersion( interface.Interface ):
    """
    a version of an object is identical in attributes to the actual object, based
    on that object's domain schema
    """
