/*
 * InitQuestionBlock.java
 *
 * Created on August 31, 2007, 4:01 PM
 */

package org.bungeni.editor.selectors;

import java.awt.Component;
import javax.swing.JDialog;
import javax.swing.JTextField;
import org.bungeni.db.registryQueryDialog;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.editor.fragments.FragmentsFactory;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author  Administrator
 */
public class InitQAsection extends selectorTemplatePanel implements IBungeniForm {
     registryQueryDialog rqs;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InitQAsection.class.getName());
    String txtURI = "";
    /** Creates new form InitQuestionBlock */
    public InitQAsection() {
        initComponents();
    }
    public InitQAsection(OOComponentHelper ooDocument, JDialog parentDlg, toolbarAction theAction) {
        super(ooDocument, parentDlg, theAction);
        init();
    }
   
      public void initObject(OOComponentHelper ooDoc, JDialog dlg, toolbarAction act, toolbarSubAction subAct) {
        super.initObject( ooDoc, dlg, act, subAct);
        init();
        setControlModes();
        // setControlData();
    }
      
    public void init(){
        super.init();
        initComponents();
        initFields();
        buildComponentsArray();
    }
    
    public void createContext(){
          super.createContext();
          formContext.setBungeniForm(this);
      }
    public String getClassName(){
        return this.getClass().getName();
    }
           
    private void initFields() {

        if (theMode == SelectorDialogModes.TEXT_INSERTION) {
            txtMessageArea.setText("You are attempting to insert a new Question-Answer section, " +
                    "please enter the titular text for the section, and press apply to add a new  " +
                    "Question-Answer block to the document " +
                    "document");
        } else if (theMode == SelectorDialogModes.TEXT_SELECTED_INSERT) {
            txtMessageArea.setText("You are attempting to markup some existing text" +
                    " as a Question-Answer block, " +
                    "please select the questions  you would like to markup , and press apply" +
                    "to markup the selected text with the correct question metadata");
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

        setPreferredSize(new java.awt.Dimension(299, 200));
        txt_title.setName("txt_qa_text");

        lbl_title.setText("Question Title ");
        lbl_title.setName("lbl_qa_title");

        btnApply.setText("Apply");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

        btnCancel.setText("Close");
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

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(separatorLine1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .add(scrollMessageArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .add(txt_title, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .add(lbl_title, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(btnApply, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 43, Short.MAX_VALUE)
                        .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 119, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(scrollMessageArea, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(separatorLine1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lbl_title)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txt_title, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnApply)
                    .add(btnCancel))
                .add(262, 262, 262))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
// TODO add your handling code here:
        parent.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void returnError (boolean state) {
        btnApply.setEnabled(state);
        btnCancel.setEnabled(state);
         return;
    }
    public boolean preFullInsert() {
        long sectionBackColor = 0xeeffff;
        float sectionLeftMargin = (float).1;
        String actionSectionName = "";
        actionSectionName = getActionSectionName();
        formContext.addFieldSet("container_section");
        formContext.addFieldSet("current_section");
        formContext.addFieldSet("new_section");
        formContext.addFieldSet("section_back_color");
        formContext.addFieldSet("section_left_margin");
        formContext.addFieldSet("document_fragment");
        formContext.addFieldSet("document_import_section");
        formContext.addFieldSet("search_for");
        formContext.addFieldSet("replacement_text");
                         
        formContext.getFieldSets("container_section").add(ooDocument.currentSectionName());
        //thePreInsertMap.put("container_section", ooDocument.currentSectionName());
        formContext.getFieldSets("current_section").add(actionSectionName);
        //thePreInsertMap.put("current_section", actionSectionName);
        formContext.getFieldSets("new_section").add(actionSectionName);
        //thePreInsertMap.put("new_section", actionSectionName);
        formContext.getFieldSets("section_back_color").add(Long.toHexString(sectionBackColor));
        //thePreInsertMap.put("section_back_color", Long.toHexString(sectionBackColor));
        formContext.getFieldSets("section_left_margin").add(Float.toString(sectionLeftMargin));
        //thePreInsertMap.put("section_left_margin", Float.toString(sectionLeftMargin));
        formContext.getFieldSets("document_import_section").add(actionSectionName);
        formContext.getFieldSets("document_fragment").add(FragmentsFactory.getFragment("hansard_qa"));
        //thePreInsertMap.put("document_fragment", FragmentsFactory.getFragment("hansard_qa"));
        formContext.getFieldSets("search_for").add(new String("[[QA_TITLE]]"));
        //thePreInsertMap.put("search_for", new String("[[QA_TITLE]]"));
        formContext.getFieldSets("replacement_text").add(txt_title.getText());
        //thePreInsertMap.put("replacement_text", txt_title.getText());
        /* the above adds : 
         *         thePreInsertMap.put("tabled_document_titles", arrDocTitles);
         *         thePreInsertMap.put("tabled_document_urs", arrDocURI);
         */
        /*
        thePreInsertMap.put("current_section", theAction.action_naming_convention());
        thePreInsertMap.put("target_section",  getParentSection());
        thePreInsertMap.put("container_section", thePreInsertMap.get("target_section"));
        thePreInsertMap.put("document_fragment", FragmentsFactory.getFragment("hansard_papers"));
        thePreInsertMap.put("search_for", "[[PAPER_TITLE]]");
        thePreInsertMap.put("replacement_text", txt_title.getText());
        thePreInsertMap.put("bullet_list_begin_bookmark", new String("begin_tabled_documents_list"));
        //in insert mode this is always INSIDE_SECTION
        thePreInsertMap.put("selected_section_action_command",  "INSIDE_SECTION");
        return true;
        */
        return true;
    }
    
    public boolean processFullInsert(){
        boolean bReturn = processCatalogCommand();
        return bReturn;
    }
    
    public boolean postFullInsert(){
        return true;
    }
    
    
    
    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt)  {//GEN-FIRST:event_btnApplyActionPerformed
// TODO add your handling code here:
        FORM_APPLY_NO_ERROR = false;
        super.formApply();
        if (FORM_APPLY_NO_ERROR)
            parent.dispose();


        
    }//GEN-LAST:event_btnApplyActionPerformed

    private void fillDocument(){
           //check if section exists
           //if already exists, bail out with error message
           //else
           //create section with appropriate name
           //set section metadata
           //fill up respetive information on the document.
           String newSectionName = "";
           //must check for action type too, but for testing purposes ignored...
        

    }

    public boolean validateFieldValue(Component field, Object fieldValue) {
        boolean bFailure = true;
        String formFieldName = field.getName();
        
         if (formFieldName.equals("txt_qa_text")) {
               JTextField txtField = (JTextField) field; 
                 if (txtField.getText().trim().length() == 0 ) {
                        checkFieldsMessages.add("You must enter a title! ");
                    return false;
                   }
         theControlDataMap.put(field.getName(), fieldValue);
        }
        return bFailure;
    }

 
    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JLabel lbl_title;
    private javax.swing.JScrollPane scrollMessageArea;
    private javax.swing.JSeparator separatorLine1;
    private javax.swing.JTextArea txtMessageArea;
    private javax.swing.JTextField txt_title;
    // End of variables declaration//GEN-END:variables
    
}
