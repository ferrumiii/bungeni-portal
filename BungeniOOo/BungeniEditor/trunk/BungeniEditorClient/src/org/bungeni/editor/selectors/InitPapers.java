/*
 * InitQuestionBlock.java
 *
 * Created on August 31, 2007, 4:01 PM
 */

package org.bungeni.editor.selectors;

import com.sun.star.xml.AttributeData;
import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.BungeniRegistryFactory;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.GeneralQueryFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.db.registryQueryDialog;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.editor.fragments.FragmentsFactory;
import org.bungeni.editor.macro.ExternalMacro;
import org.bungeni.editor.macro.ExternalMacroFactory;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.utils.MessageBox;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 *
 * @author  Administrator
 */
public  class InitPapers extends selectorTemplatePanel implements IBungeniForm {
   
    registryQueryDialog rqs;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InitPapers.class.getName());
 
    HashMap<String, ArrayList> selectionData = new HashMap<String,ArrayList>();
    /** Creates new form InitQuestionBlock */
    public InitPapers() {
       // initComponents();
        super();
    }
    public InitPapers(OOComponentHelper ooDocument, JDialog parentDlg, toolbarAction theAction) {
        super(ooDocument, parentDlg, theAction);
        init();
        //initComponents();
        //initFields();
        //initData();
        setControlModes();
        setControlData();
        tbl_tabledDocs.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }
    
      public InitPapers(OOComponentHelper ooDocument, 
            JDialog parentDlg, 
            toolbarAction theAction, toolbarSubAction subAction) {
         super(ooDocument, parentDlg, theAction, subAction);
         init();
         setControlModes();
         setControlData();
         //selectionControlModes();
         log.debug("calling constructor : initDebateRecord, mode = " + getDialogMode());
      }
    public String getClassName(){
        return InitPapers.class.getName();
    }
    
    protected void createContext(){
          super.createContext();
          formContext.setBungeniForm(this);
      }
    public void initObject(OOComponentHelper ooDoc, JDialog dlg, toolbarAction act, toolbarSubAction subAct) {
        super.initObject( ooDoc, dlg, act, subAct);
            init();
         setControlModes();
         setControlData();
    }
   
    public void init(){
        super.init();
        initComponents();
        buildComponentsArray();
      }
          
    private void initFields() {
        if (theMode == SelectorDialogModes.TEXT_INSERTION) {
            
            txtMessageArea.setText("You are attempting to insert a new Papers section, " +
                    "please select a title for the section, and the list of tabled documents");               
        } else if (theMode == SelectorDialogModes.TEXT_SELECTED_INSERT) {
            txtMessageArea.setText("You are attempting to markup some existing text" +
                    " as a Papers section, " +
                    "please select the text you would like to markup , and press apply" +
                    "to markup the selected text with the correct speech metadata");
        } else if (theMode == SelectorDialogModes.TEXT_SELECTED_EDIT) {
            
        }
    }
    
    protected void setControlData() {
        try {
        //only in edit mode, only if the metadata properties exist
        initData();
        initFields();
        if (theMode == SelectorDialogModes.TEXT_EDIT) {
         
            }
        } catch (Exception ex) {
            log.error("SetControlData: "+ ex.getMessage());
        }
    }
    
    private void initData() {
        dbInstance.Connect();
        QueryResults qr = dbInstance.QueryResults("select document_title, document_uri, document_date from tabled_documents");
        dbInstance.EndConnect();
        
        if (qr != null ) {
            if (qr.hasResults()) {
                Vector<Vector<String>> resultRows = new Vector<Vector<String>>();
                resultRows = qr.theResults();
              
                DefaultTableModel mdl = new DefaultTableModel();
                mdl.setDataVector(resultRows, qr.getColumnsAsVector());
                tbl_tabledDocs.setModel(mdl);
            }
        }
        
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        txt_title = new javax.swing.JTextField();
        lbl_title = new javax.swing.JLabel();
        btnApply = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        separatorLine1 = new javax.swing.JSeparator();
        scrollMessageArea = new javax.swing.JScrollPane();
        txtMessageArea = new javax.swing.JTextArea();
        lbl_tabledDocs = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_tabledDocs = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(348, 314));
        txt_title.setName("txt_initpapers_title");

        lbl_title.setText("Enter a title for the section");
        lbl_title.setName("lbl_initpapers_title");

        btnApply.setText("Apply");
        btnApply.setName("btn_apply");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

        btnCancel.setText("Close");
        btnCancel.setName("btn_cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        txtMessageArea.setBackground(new java.awt.Color(204, 204, 204));
        txtMessageArea.setColumns(20);
        txtMessageArea.setEditable(false);
        txtMessageArea.setFont(new java.awt.Font("Tahoma", 0, 11));
        txtMessageArea.setLineWrap(true);
        txtMessageArea.setRows(5);
        txtMessageArea.setWrapStyleWord(true);
        scrollMessageArea.setViewportView(txtMessageArea);

        lbl_tabledDocs.setText("Select Tabled Documents");
        lbl_tabledDocs.setName("lbl_initpapers_tableddocs");

        tbl_tabledDocs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_tabledDocs.setName("tbl_initpapers_tableddocs");
        jScrollPane1.setViewportView(tbl_tabledDocs);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, separatorLine1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                    .add(scrollMessageArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                    .add(lbl_title, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 216, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txt_title, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                    .add(lbl_tabledDocs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 195, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(btnApply, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 99, Short.MAX_VALUE)
                        .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 112, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(scrollMessageArea, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separatorLine1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lbl_title, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txt_title, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lbl_tabledDocs)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnApply)
                    .add(btnCancel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public boolean validateTabledDocs(Component field) {
        JTable tbl = (JTable) field;
        if (tbl.getSelectedRowCount() == 0) {
            checkFieldsMessages.add("Please select a tabled document!");
            return false;
        }
        return true;
    }
    
    public boolean validateTitle (Component field, Object fieldValue ) {
        JTextField txtField = (JTextField) field; 
        if (txtField.getText().trim().length() == 0 ) {
            checkFieldsMessages.add("You must enter a title! ");
            return false;
       }
        theControlDataMap.put(field.getName(), fieldValue);
        return true;
    }

    
    public boolean preValidationFullInsert(){
      boolean bResult = true;
      try {
      //check root
      dbSettings.Connect();
      QueryResults qr = dbSettings.QueryResults(SettingsQueryFactory.Q_CHECK_IF_ACTION_HAS_PARENT(theAction.action_naming_convention()));
      dbSettings.EndConnect();
      String[] results = qr.getSingleColumnResult("THE_COUNT");
      if (results[0].equals("0")) {
        if (!ooDocument.hasSection("root")) {
            checkFieldsMessages.add("The document does not have a root section!");
            bResult = false;
            return bResult;
       }  
      }
      
       if (ooDocument.hasSection(theAction.action_naming_convention())) {
        checkFieldsMessages.add( "The document already has a papers section, please delete the section first");
        bResult = false;
        return bResult;
       }
      
      } catch(Exception ex) {
        log.error("preValidation : " + ex.getMessage());
        bResult = false;
      } finally {
          return bResult;
      }
    }
    
    /*
     *This is a funciton overriden from the base class
     *validateFieldValue is invoked by check fields
     *user must write validation functions for fields and return true / false 
     *after setting an error message
     *
     */

    public boolean validateFieldValue(Component field, Object fieldValue ) {
        String formFieldName = field.getName();
        boolean bFailure=false;
      //table validations need to be handled directly.
                                
        if (formFieldName.equals("tbl_initpapers_tableddocs")) {
            bFailure = validateTabledDocs(field);
        } else if (formFieldName.equals("txt_initpapers_title")) {
            bFailure = validateTitle(field, fieldValue);
        }
        /*
     if (tbl_tabledDocs.getSelectedRowCount() == 0) {
            MessageBox.OK(parent, "Please select a Tabled Document!");
            returnError(true);
            return;
        }
       if (txt_title.getText().trim().length() == 0 ) {
            MessageBox.OK(parent, "You must enter a title! ");
            returnError(true);
            return;
       }
      dbSettings.Connect();
      QueryResults qr = dbSettings.QueryResults(SettingsQueryFactory.Q_CHECK_IF_ACTION_HAS_PARENT(theAction.action_naming_convention()));
      dbSettings.EndConnect();
      String[] results = qr.getSingleColumnResult("THE_COUNT");
      if (results[0].equals("0")) {
        //no parent section
        if (!ooDocument.hasSection("root")) {
            MessageBox.OK(parent, "The document does not have a root section!");
            returnError(true);
            return;
        }  
      }
      
     int[] selectedRows = tbl_tabledDocs.getSelectedRows();
     ArrayList<String> docTitles = new ArrayList<String>();
     ArrayList<String> docURIs = new ArrayList<String>();
     
     for (int i=0; i < selectedRows.length; i++) {
         String docTitle = (String)tbl_tabledDocs.getModel().getValueAt(i, 0 );
         String docURI = (String) tbl_tabledDocs.getModel().getValueAt(i, 1);
         docTitles.add(docTitle);
         docURIs.add(docURI);
     }
     
    String[] arrDocTitles = docTitles.toArray(new String[docTitles.size()]); 
    String[] arrDocURI = docURIs.toArray(new String[docURIs.size()]);
    String targetSection = "";
    
    //if document already has section delete it
    if (ooDocument.hasSection(theAction.action_naming_convention())) {
       int nRet = MessageBox.Confirm(parent, "The existing section and its contents will be deleted, please confirm ?", "Deleting section");
       if (nRet == JOptionPane.NO_OPTION) {
           return;
       }
        //routes to appropriate field validator...
        if (formFieldName.equals("dt_initdebate_hansarddate")) {
        } else if (formFieldName.equals("dt_initdebate_timeofhansard")) {
        } else if (formFieldName.equals("txt_initdebate_selectlogo")) {
        }  else {
            log.debug("no validator found for field: "+ field.getName()+ " , returning true");
            return true;
        }
         */
        return bFailure;
    }
    
    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed
// TODO add your handling code here:
     super.formApply();
     parent.dispose();
        /*
        returnError(false);

     if (tbl_tabledDocs.getSelectedRowCount() == 0) {
            MessageBox.OK(parent, "Please select a Tabled Document!");
            returnError(true);
            return;
        }
       if (txt_title.getText().trim().length() == 0 ) {
            MessageBox.OK(parent, "You must enter a title! ");
            returnError(true);
            return;
       }
      dbSettings.Connect();
      QueryResults qr = dbSettings.QueryResults(SettingsQueryFactory.Q_CHECK_IF_ACTION_HAS_PARENT(theAction.action_naming_convention()));
      dbSettings.EndConnect();
      String[] results = qr.getSingleColumnResult("THE_COUNT");
      if (results[0].equals("0")) {
        //no parent section
        if (!ooDocument.hasSection("root")) {
            MessageBox.OK(parent, "The document does not have a root section!");
            returnError(true);
            return;
        }  
      }
      
     int[] selectedRows = tbl_tabledDocs.getSelectedRows();
     ArrayList<String> docTitles = new ArrayList<String>();
     ArrayList<String> docURIs = new ArrayList<String>();
     
     for (int i=0; i < selectedRows.length; i++) {
         String docTitle = (String)tbl_tabledDocs.getModel().getValueAt(i, 0 );
         String docURI = (String) tbl_tabledDocs.getModel().getValueAt(i, 1);
         docTitles.add(docTitle);
         docURIs.add(docURI);
     }
     
    String[] arrDocTitles = docTitles.toArray(new String[docTitles.size()]); 
    String[] arrDocURI = docURIs.toArray(new String[docURIs.size()]);
    String targetSection = "";
    
    //if document already has section delete it
    if (ooDocument.hasSection(theAction.action_naming_convention())) {
       int nRet = MessageBox.Confirm(parent, "The existing section and its contents will be deleted, please confirm ?", "Deleting section");
       if (nRet == JOptionPane.NO_OPTION) {
           return;
       }
      //remove the section and components...
       ExternalMacro removeSection = ExternalMacroFactory.getMacroDefinition("RemoveSectionAndContents");
       removeSection.addParameter(ooDocument.getComponent());
       removeSection.addParameter(theAction.action_naming_convention());
       ooDocument.executeMacro(removeSection.toString(), removeSection.getParams());
       
    }
    
    targetSection = theAction.getSelectedSectionToActUpon();
    ExternalMacro createSectionMacro;
    //now we need to check if the SelectSection resulted in a AFTER_SECTION or INSIDE_SECTION selection
    if (theAction.getSelectedSectionActionCommand().equals("INSIDE_SECTION")) {
        createSectionMacro = ExternalMacroFactory.getMacroDefinition("AddSectionInsideSection");
        createSectionMacro.addParameter(ooDocument.getComponent());
        createSectionMacro.addParameter(targetSection);
        createSectionMacro.addParameter(theAction.action_naming_convention());
    } else  {
        createSectionMacro = ExternalMacroFactory.getMacroDefinition("InsertSectionAfterSection");
        createSectionMacro.addParameter(ooDocument.getComponent());
        createSectionMacro.addParameter(theAction.action_naming_convention());
        createSectionMacro.addParameter(targetSection);
        
    }

    ooDocument.executeMacro(createSectionMacro.toString(), createSectionMacro.getParams());
    
    
    
    ExternalMacro insertDocIntoSection = ExternalMacroFactory.getMacroDefinition("InsertDocumentIntoSection");
    insertDocIntoSection.addParameter(ooDocument.getComponent());
    insertDocIntoSection.addParameter(theAction.action_naming_convention())   ;
    insertDocIntoSection.addParameter(FragmentsFactory.getFragment("hansard_papers"));
    ooDocument.executeMacro(insertDocIntoSection.toString(), insertDocIntoSection.getParams());
          
    ExternalMacro searchAndReplace = ExternalMacroFactory.getMacroDefinition("SearchAndReplace");
    searchAndReplace.addParameter(ooDocument.getComponent());
    searchAndReplace.addParameter(new String("[[PAPER_TITLE]]"));
    searchAndReplace.addParameter(txt_title.getText());
    ooDocument.executeMacro(searchAndReplace.toString(), searchAndReplace.getParams());
    
    ExternalMacro insertArrayAsBulletList = ExternalMacroFactory.getMacroDefinition("InsertArrayAsBulletList");
    insertArrayAsBulletList.addParameter(new String("begin_tabled_documents_list"));
    insertArrayAsBulletList.addParameter(arrDocTitles);
    insertArrayAsBulletList.addParameter(arrDocURI);
    ooDocument.executeMacro(insertArrayAsBulletList.toString(), insertArrayAsBulletList.getParams());
    //MessageBox.OK(parent, "Paper details have been imported !");
    returnError(true);
    parent.dispose();
    */
    }//GEN-LAST:event_btnApplyActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
// TODO add your handling code here:
        parent.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void returnError(boolean state) {
        btnApply.setEnabled(state);
        btnCancel.setEnabled(state);
   }
    private void fillDocument(){
        
    }
 
    
    public HashMap<String,ArrayList<String>> getTableSelection() {
         int[] selectedRows = tbl_tabledDocs.getSelectedRows();
         ArrayList<String> docTitles = new ArrayList<String>();
          ArrayList<String> docURIs = new ArrayList<String>();
     
             for (int i=0; i < selectedRows.length; i++) {
                 String docTitle = (String)tbl_tabledDocs.getModel().getValueAt(i, 0 );
                 String docURI = (String) tbl_tabledDocs.getModel().getValueAt(i, 1);
                 docTitles.add(docTitle);
                 docURIs.add(docURI);
             }
     
        HashMap<String,ArrayList<String>> tblData = new HashMap<String,ArrayList<String>>();
        tblData.put("tabled_document_titles", docTitles);
        tblData.put("tabled_document_uris", docURIs);
        return tblData;
        
    }
    

    public boolean preSelectInsert(){
        //gets table into preInserMap
        HashMap<String, ArrayList<String>> tblData = getTableSelection();
        formContext.addFieldSet("tabled_document_titles");
        formContext.addFieldSet("tabled_document_uris");
        
        formContext.getFieldSets("tabled_document_titles").add(tblData.get("tabled_document_titles").toArray(new String[tblData.get("tabled_document_titles").size()]));
        
        //thePreInsertMap.put("tabled_document_titles", 
          //      tblData.get("tabled_document_titles").toArray(new String[tblData.get("tabled_document_titles").size()]));
      
        formContext.getFieldSets("tabled_document_uris").add( tblData.get("tabled_document_uris").toArray(new String[tblData.get("tabled_document_uris").size()]));
       // thePreInsertMap.put("tabled_document_uris", 
         //       tblData.get("tabled_document_uris").toArray(new String[tblData.get("tabled_document_uris").size()]));
    
        return true;
    }
    
    
    public boolean processSelectInsert(){
        //System.out.println("theControlDataMap = " + theControlDataMap.keySet().size());
        //this.
         boolean bReturn = processCatalogCommand();
        return bReturn; 
       
    }
    
    
    
    public boolean preFullInsert(){
        HashMap<String, ArrayList<String>> tblData = getTableSelection();
        formContext.addFieldSet("tabled_document_titles");
        formContext.addFieldSet("tabled_document_uris");
        formContext.addFieldSet("current_section");
        formContext.addFieldSet("target_section");
        formContext.addFieldSet("container_section");
        formContext.addFieldSet("document_fragment");
        formContext.addFieldSet("document_fragment");
        formContext.addFieldSet("document_import_section");
        formContext.addFieldSet("search_for");
        formContext.addFieldSet("replacement_text");
        formContext.addFieldSet("bullet_list_begin_bookmark");
        formContext.addFieldSet("selected_section_action_command");
        
        formContext.getFieldSets("tabled_document_titles").add(tblData.get("tabled_document_titles").toArray(new String[tblData.get("tabled_document_titles").size()]));
        formContext.getFieldSets("tabled_document_uris").add( tblData.get("tabled_document_uris").toArray(new String[tblData.get("tabled_document_uris").size()]));
 
        thePreInsertMap.put("tabled_document_titles", 
                tblData.get("tabled_document_titles").toArray(new String[tblData.get("tabled_document_titles").size()]));
        thePreInsertMap.put("tabled_document_uris", 
                tblData.get("tabled_document_uris").toArray(new String[tblData.get("tabled_document_uris").size()]));
       
        /* the above adds : 
         *         thePreInsertMap.put("tabled_document_titles", arrDocTitles);
         *         thePreInsertMap.put("tabled_document_urs", arrDocURI);
         */
        formContext.getFieldSets("current_section").add(theAction.action_naming_convention());
        //thePreInsertMap.put("current_section", theAction.action_naming_convention());
        String parentSection = getParentSection();
        formContext.getFieldSets("target_section").add(parentSection);
        //thePreInsertMap.put("target_section",  getParentSection());
        formContext.getFieldSets("container_section").add(parentSection);
        //thePreInsertMap.put("container_section", thePreInsertMap.get("target_section"));
        formContext.getFieldSets("document_import_section").add(theAction.action_naming_convention());
        formContext.getFieldSets("document_fragment").add(FragmentsFactory.getFragment("hansard_papers"));
        //thePreInsertMap.put("document_fragment", FragmentsFactory.getFragment("hansard_papers"));
        formContext.getFieldSets("search_for").add( "[[PAPER_TITLE]]");
        //thePreInsertMap.put("search_for", "[[PAPER_TITLE]]");
        formContext.getFieldSets("replacement_text").add( txt_title.getText());
        //thePreInsertMap.put("replacement_text", txt_title.getText());
        formContext.getFieldSets("bullet_list_begin_bookmark").add( new String("begin_tabled_documents_list"));
        //thePreInsertMap.put("bullet_list_begin_bookmark", new String("begin_tabled_documents_list"));
        //in insert mode this is always INSIDE_SECTION
        formContext.getFieldSets("selected_section_action_command").add( new String("INSIDE_SECTION"));
       // thePreInsertMap.put("selected_section_action_command",  "INSIDE_SECTION");
        return true;
    }
    
    public boolean postFullInsert(){
        parent.dispose();
        return true;
    }
    
    public boolean processFullInsert(){
                
        boolean bReturn = processCatalogCommand();
        return bReturn;
        
    }
  
    public boolean preFullEdit(){
        return true;
    }
    
    public boolean processFullEdit(){
        return true;
    }
    
    public boolean postFullEdit(){
        return true;
    }
    
    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_tabledDocs;
    private javax.swing.JLabel lbl_title;
    private javax.swing.JScrollPane scrollMessageArea;
    private javax.swing.JSeparator separatorLine1;
    private javax.swing.JTable tbl_tabledDocs;
    private javax.swing.JTextArea txtMessageArea;
    private javax.swing.JTextField txt_title;
    // End of variables declaration//GEN-END:variables
    
}
