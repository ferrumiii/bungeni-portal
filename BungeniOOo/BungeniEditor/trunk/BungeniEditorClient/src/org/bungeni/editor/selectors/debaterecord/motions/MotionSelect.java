/*
 * QuestionSelect.java
 *
 * Created on August 12, 2008, 12:09 PM
 */

package org.bungeni.editor.selectors.debaterecord.motions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.BungeniRegistryFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.registryQueryDialog;
import org.bungeni.editor.selectors.BaseMetadataPanel;
import org.bungeni.ooo.OOComponentHelper;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author  undesa
 */
public class MotionSelect extends BaseMetadataPanel {
 
    registryQueryDialog rqs;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MotionSelect.class.getName());
   // HashMap<String, String> selectionData = new HashMap<String,String>();
  
    /** Creates new form QuestionSelect */
    public MotionSelect() {
        initComponents();
        initComboSelect();
        this.cboSelectMotion.addActionListener(new MotionSelector());
        this.btnSelectQuestion.setVisible(false);
    }

    class MotionSelector implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            if (cboSelectMotion.getSelectedIndex() != -1) {
               ObjectMotion selectedMotion = (ObjectMotion)cboSelectMotion.getModel().getSelectedItem();

               HashMap<String,String> selData = new HashMap<String,String>();
               selData.put("MOTION_ID", selectedMotion.motionId);
               selData.put("MOTION_NAME", selectedMotion.motionName);
               selData.put("MOTION_URI", selectedMotion.motionUri);
               selData.put("MOTION_TEXT", selectedMotion.motionText);
               selData.put("MOTION_TITLE", selectedMotion.motionTitle);

                ((Main)getContainerPanel()).selectionData = selData;
                if ( ((Main)getContainerPanel()).selectionData.size() > 0 ) 
                    ((Main)getContainerPanel()).updateAllPanels();
            }
        }
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSelectQuestion = new javax.swing.JButton();
        cboSelectMotion = new javax.swing.JComboBox();

        setName("Select a Question"); // NOI18N

        btnSelectQuestion.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnSelectQuestion.setText("Select a Motion...");
        btnSelectQuestion.setActionCommand("Select a Question");
        btnSelectQuestion.setContentAreaFilled(false);
        btnSelectQuestion.setName("btn_select_question"); // NOI18N
        btnSelectQuestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectQuestionActionPerformed(evt);
            }
        });

        cboSelectMotion.setEditable(true);
        cboSelectMotion.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboSelectMotion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboSelectMotion.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSelectQuestion, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cboSelectMotion, 0, 224, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cboSelectMotion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSelectQuestion))
        );
    }// </editor-fold>//GEN-END:initComponents

private void btnSelectQuestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectQuestionActionPerformed
// TODO add your handling code here:
    
        rqs = new registryQueryDialog("Select A Motion", "Select MOTION_ID, MOTION_TITLE, MOTION_NAME, MOTION_TEXT, MOTION_URI from motions", getParentFrame());
        rqs.show();
        log.debug("Moved on before closing child dialog");
       // HashMap<String,String> selectionData = ((Main)getContainerPanel()).selectionData;
        ((Main)getContainerPanel()).selectionData = rqs.getData();
        if ( ((Main)getContainerPanel()).selectionData.size() > 0 ) {
            HashMap<String,String> registryMap = BungeniRegistryFactory.fullConnectionString();  
            BungeniClientDB dbInstance = new BungeniClientDB(registryMap);
        
            Set keyset =  ((Main)getContainerPanel()).selectionData.keySet();
            log.debug("selected keyset size = " + keyset.size());
            ((Main)getContainerPanel()).updateAllPanels();
        } else {
            log.debug("selected keyset empty");
        }
}//GEN-LAST:event_btnSelectQuestionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSelectQuestion;
    private javax.swing.JComboBox cboSelectMotion;
    // End of variables declaration//GEN-END:variables

    private void initComboSelect(){
            Vector<ObjectMotion> motionObjects = new Vector<ObjectMotion>();
           HashMap<String,String> registryMap = BungeniRegistryFactory.fullConnectionString();  
            BungeniClientDB dbInstance = new BungeniClientDB(registryMap);
            dbInstance.Connect();
            QueryResults qr = dbInstance.QueryResults("Select MOTION_ID, MOTION_TITLE, MOTION_NAME, MOTION_TEXT, MOTION_URI from motions order by motion_name");
            dbInstance.EndConnect();
            String motionId, motionTitle, motionName, motionText, motionURI;
            if (qr.hasResults()) {
                Vector<Vector<String>> theResults = qr.theResults();
                for (Vector<String> row : theResults) {
                     motionId = qr.getField(row, "MOTION_ID");
                     motionTitle = qr.getField(row, "MOTION_TITLE");
                     motionName = qr.getField(row, "MOTION_NAME");
                     motionText = qr.getField(row, "MOTION_TITLE");
                     motionURI = qr.getField(row, "MOTION_URI");
                    ObjectMotion m = new ObjectMotion(motionId, motionTitle, motionName, motionText, motionURI);
                    motionObjects.add(m);
                }
            }
            this.cboSelectMotion.setModel(new DefaultComboBoxModel(motionObjects));
            AutoCompleteDecorator.decorate(cboSelectMotion);
    }

    public String getPanelName() {
        return "Title";
    }

    public Component getPanelComponent() {
        return this;
    }


    @Override
    public boolean doCancel() {
        return true;
    }

    @Override
    public boolean doReset() {
        return true;
    }

    @Override
    public boolean preFullEdit() {
        return true;
    }

    @Override
    public boolean processFullEdit() {
        return true;
    }

    @Override
    public boolean postFullEdit() {
        return true;
    }

    @Override
    public boolean preFullInsert() {
        return true;
    }

    @Override
    public boolean processFullInsert() {
        return true;
    }

    @Override
    public boolean postFullInsert() {
        return true;
    }

    @Override
    public boolean preSelectEdit() {
        return true;
    }

    @Override
    public boolean processSelectEdit() {
        return true;
    }

    @Override
    public boolean postSelectEdit() {
        return true;
    }

    @Override
    public boolean preSelectInsert() {
        return true;
    }

    @Override
    public boolean processSelectInsert() {
        String motionId = ((Main)getContainerPanel()).selectionData.get("MOTION_ID");
        OOComponentHelper ooDoc = getContainerPanel().getOoDocument();
        HashMap<String,String> sectionMeta = new HashMap<String,String>();
        String newSectionName = ((Main)getContainerPanel()).mainSectionName;
        sectionMeta.put("BungeniMotionNo", motionId);
        ooDoc.setSectionMetadataAttributes(newSectionName, sectionMeta);
        return true;
    }

    @Override
    public boolean postSelectInsert() {
       return true;
    }

    @Override
    public boolean validateSelectedEdit() {
        return true;
    }

    @Override
    public boolean validateSelectedInsert() {
        return true;
    }

    @Override
    public boolean validateFullInsert() {
        return true;
    }

    @Override
    public boolean validateFullEdit() {
        return true;
    }
    
        @Override
    protected void initFieldsSelectedEdit() {
        return;
    }

    @Override
    protected void initFieldsSelectedInsert() {
        //initComboSelect();
        return;
    }

    @Override
    protected void initFieldsInsert() {
        return;
    }

    @Override
    protected void initFieldsEdit() {
        return;
    }
}
