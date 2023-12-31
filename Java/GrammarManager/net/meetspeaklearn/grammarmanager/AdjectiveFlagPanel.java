/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

/**
 *
 * @author steve
 */
public class AdjectiveFlagPanel extends javax.swing.JPanel {
    private Adjective subject, object;

    /**
     * Creates new form AdjectiveFlagPanel
     */
    public AdjectiveFlagPanel() {
        initComponents();
    }
    
    public AdjectiveFlagPanel(Adjective subject, Adjective object) {
        this();
        this.subject = subject;
        this.object = object;
        
        adjectiveCheckBox.setText(object.getText());
        antonymCheckBox.setSelected(subject.mutuallyConflict(object));
    }
    
    public boolean isSelected() {
        return adjectiveCheckBox.isSelected();
    }
    
    public Adjective getAdjective() {
        return this.object;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        adjectiveCheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        antonymCheckBox = new javax.swing.JCheckBox();
        synonymCheckBox = new javax.swing.JCheckBox();

        setBackground(new java.awt.Color(204, 204, 255));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        adjectiveCheckBox.setBackground(new java.awt.Color(204, 204, 255));
        adjectiveCheckBox.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        adjectiveCheckBox.setText("adjective");
        add(adjectiveCheckBox);

        jLabel1.setText(" - ");
        add(jLabel1);

        antonymCheckBox.setBackground(new java.awt.Color(204, 204, 255));
        antonymCheckBox.setText("antonym");
        antonymCheckBox.setPreferredSize(new java.awt.Dimension(100, 23));
        antonymCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                antonymCheckBoxActionPerformed(evt);
            }
        });
        add(antonymCheckBox);

        synonymCheckBox.setBackground(new java.awt.Color(204, 204, 255));
        synonymCheckBox.setText("synonym");
        synonymCheckBox.setPreferredSize(new java.awt.Dimension(100, 23));
        add(synonymCheckBox);
    }// </editor-fold>//GEN-END:initComponents

    private void antonymCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_antonymCheckBoxActionPerformed
        boolean isInConflict = antonymCheckBox.isSelected();
        
        if (isInConflict)
            subject.addMutalConflict(object);
        else
            subject.removeMutalConflict(object);
    }//GEN-LAST:event_antonymCheckBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox adjectiveCheckBox;
    private javax.swing.JCheckBox antonymCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JCheckBox synonymCheckBox;
    // End of variables declaration//GEN-END:variables
}
