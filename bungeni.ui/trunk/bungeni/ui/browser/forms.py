# encoding: utf-8
import datetime

from ore.alchemist.vocabulary import DatabaseSource

from alchemist.ui.content import ContentAddForm
from alchemist.ui.viewlet import EditFormViewlet

from zope.formlib import form
from zope import schema, interface
from zope.formlib.namedtemplate import NamedTemplate

import bungeni.core.vocabulary as vocabulary
import bungeni.core.domain as domain
from bungeni.core.i18n import _
from bungeni.core.interfaces import IGroupSitting

from bungeni.ui.datetimewidget import  SelectDateTimeWidget

import pdb

###########
# Add forms


def CheckSittingDatesInsideParentDates( context, data ):
    """
    start date must be >= parents start date
    end date must be <= parents end date (if parents end date is set)
    """
    errors =[]
    if context.__parent__.start_date > data['start_date'].date():
        errors.append( interface.Invalid(_("Start must be after Session Start Date")) )
    if context.__parent__.end_date is not None:
        if data['end_date'].date() > context.__parent__.end_date:
            errors.append(  interface.Invalid(_("End must be before Session End Date")) )
    return errors

class GroupSittingAdd( ContentAddForm ):
    """
    override the AddForm for GroupSittingAttendance
    """
    form_fields = form.Fields( IGroupSitting )
    form_fields["start_date"].custom_widget = SelectDateTimeWidget
    form_fields["end_date"].custom_widget = SelectDateTimeWidget
                      
    def update(self):
        """
        Called by formlib after __init__ for every page update. This is
        the method you can use to update form fields from your class
        """        
        self.status = self.request.get('portal_status_message','')        
        form.AddForm.update( self )
 
    def finishConstruction( self, ob ):
        """
        adapt the custom fields to the object
        """
        self.adapters = { IGroupSitting : ob }
        
    def validate(self, action, data):    
        """
        validation that require context must be called here,
        invariants may be defined in the descriptor
        """                                       
        return (form.getWidgetsData(self.widgets, self.prefix, data) +
                 form.checkInvariants(self.form_fields, data) +
                 CheckSittingDatesInsideParentDates( self.context, data))         
     

sql_add_members ='''SELECT "users"."titles" || ' ' || "users"."first_name" || ' ' || "users"."middle_name" || ' ' || "users"."last_name" as user_name, 
                    "users"."user_id", "group_sittings"."sitting_id" 
                    FROM "public"."group_sittings", "public"."sessions", 
                    "public"."user_group_memberships", "public"."users" 
                    WHERE ( "group_sittings"."session_id" = "sessions"."session_id" 
                    AND "user_group_memberships"."group_id" = "sessions"."parliament_id" 
                    AND "user_group_memberships"."user_id" = "users"."user_id" )
                    AND ( "user_group_memberships"."active_p" = True )
                    AND ("group_sittings"."sitting_id" = %(primary_key)s)
                    AND ( "users"."user_id" NOT IN (SELECT member_id 
                                                    FROM sitting_attendance 
                                                    WHERE sitting_id = %(primary_key)s)                                           
                         )
                    ORDER BY "users"."last_name"                    
                    '''
membersAddVocab = vocabulary.SQLQuerySource(sql_add_members, 'user_name', 'user_id')      
attendanceVocab = DatabaseSource(domain.AttendanceType, 'attendance_type', 'attendance_id' )

class IGroupSittingAttendanceAdd( interface.Interface ):
    """ """
    member_id = schema.Choice(title=_(u"Member of Parliament"),  
                                source=membersAddVocab, 
                                required=True,
                                )
    attendance_id = schema.Choice( title=_(u"Attendance"),  
                                    source=attendanceVocab, 
                                    required=True,
                                    )  



class GroupSittingAttendanceAdd( ContentAddForm ):
    """
    override the AddForm for GroupSittingAttendance
    """
    form_fields = form.Fields( IGroupSittingAttendanceAdd )
                      
    def update(self):
        """
        Called by formlib after __init__ for every page update. This is
        the method you can use to update form fields from your class
        """        
        self.status = self.request.get('portal_status_message','')        
        form.AddForm.update( self )
 
    def finishConstruction( self, ob ):
        """
        adapt the custom fields to the object
        """
        self.adapters = { IGroupSittingAttendanceAdd : ob }
        
        
##############
# Edit forms      
             
sql_edit_members = '''SELECT "users"."titles" || ' ' || "users"."first_name" || ' ' || "users"."middle_name" || ' ' || "users"."last_name" as user_name, 
                      "users"."user_id" 
                       FROM  "public"."users" 
                       WHERE  "users"."user_id" = %(member_id)s                                                                  
                    '''            
membersEditVocab = vocabulary.SQLQuerySource(sql_edit_members, 'user_name', 'user_id', {'member_id':'$member_id'} )      

class IGroupSittingAttendanceEdit( interface.Interface ):
    """ """
    member_id = schema.Choice(title=_(u"Member of Parliament"),  
                                source=membersEditVocab, 
                                required=True,
                                )
    attendance_id = schema.Choice( title=_(u"Attendance"),  
                                    source=attendanceVocab, 
                                    required=True,
                                    )  
   
class GroupSittingAttendanceEdit( EditFormViewlet ):
    """
    override the Edit Form for GroupSittingAttendance
    """
    form_fields = form.Fields( IGroupSittingAttendanceEdit )
    template = NamedTemplate('alchemist.subform')   

    def update( self ):
        """
        adapt the custom fields to our object
        """
        self.adapters = { IGroupSittingAttendanceEdit : self.context }        
        super( GroupSittingAttendanceEdit, self).update()
                   


 
        
        

    
