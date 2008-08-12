/*
 * DebateRecordLogo.java
 *
 * Created on August 11, 2008, 10:42 PM
 */

package org.bungeni.editor.selectors.debaterecord.masthead;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.editor.BungeniEditorProperties;
import org.bungeni.editor.selectors.BaseMetadataPanel;

/**
 *
 * @author  undesa
 */
public class DebateRecordLogo extends BaseMetadataPanel {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DebateRecordLogo.class.getName());
   private String m_strLogoPath; 
   private String m_strLogoFileName;

    /** Creates new form DebateRecordLogo */
    public DebateRecordLogo() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txt_initdebate_selectlogo = new javax.swing.JTextField();
        btn_initdebate_selectlogo = new javax.swing.JButton();

        txt_initdebate_selectlogo.setEditable(false);
        txt_initdebate_selectlogo.setName("txt_initdebate_selectlogo"); // NOI18N

        btn_initdebate_selectlogo.setText("Select Logo...");
        btn_initdebate_selectlogo.setName("btn_initdebate_selectlogo"); // NOI18N
        btn_initdebate_selectlogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_initdebate_selectlogoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_initdebate_selectlogo, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_initdebate_selectlogo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btn_initdebate_selectlogo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_initdebate_selectlogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void btn_initdebate_selectlogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_initdebate_selectlogoActionPerformed
// TODO add your handling code here:
        String logoPath = "";
        logoPath = BungeniEditorProperties.getEditorProperty("logoPath");
        log.debug("logo path = " + logoPath);
        String strPath = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH();
        logoPath = strPath + File.separator + logoPath.replace('/', File.separatorChar);
        log.debug("logo path new = "+ logoPath);
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        File fLogoPath = new File(logoPath);
        chooser.setCurrentDirectory(fLogoPath);
        int nReturnVal = chooser.showOpenDialog(this);
        if (nReturnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            m_strLogoFileName = file.getName();
            m_strLogoPath = file.getAbsolutePath();
            txt_initdebate_selectlogo.setText(m_strLogoFileName);
            //This is where a real application would open the file.
            log.debug("Opening: " + file.getName() + "." + "\n");
        } else {
            log.debug("Open command cancelled by user." + "\n");
        }
}//GEN-LAST:event_btn_initdebate_selectlogoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_initdebate_selectlogo;
    private javax.swing.JTextField txt_initdebate_selectlogo;
    // End of variables declaration//GEN-END:variables

    @Override
    public String getPanelName() {
     return getName();
    }

    @Override
    public Component getPanelComponent() {
     return this;
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
    public boolean doCancel() {
     return true;
    }

    @Override
    public boolean doReset() {
     return true;
    }

}
