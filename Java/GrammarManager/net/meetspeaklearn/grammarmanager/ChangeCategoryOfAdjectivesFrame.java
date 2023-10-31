/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import static net.meetspeaklearn.grammarmanager.WorkingDataManager.currentWorkingDataManager;
import static net.meetspeaklearn.grammarmanager.WorkingDataManager.workingLexicon;

import java.util.*;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author steve
 */
public class ChangeCategoryOfAdjectivesFrame extends javax.swing.JFrame {
    private ArrayList<Adjective> adjectives;
    private Adjective caller;
    private AdjectivalCategory selectedCategory;

    /**
     * Creates new form ChangeCategoryOfAdjectivesFrame
     */
    public ChangeCategoryOfAdjectivesFrame() {
        initComponents();
    }
    
    public ChangeCategoryOfAdjectivesFrame(ArrayList<Adjective> adjectives) {
        this();
        this.adjectives = adjectives;
        this.caller = null;
        optionsPanel.remove(includeCallerCheckBox);
        mergeConflictsCheckBox.setEnabled(false);
        applyChanges.setEnabled(false);
        categoriesList.setModel(new DefaultComboBoxModel<String>(workingLexicon.getAdjCatManager().getCategoryNames().toArray(new String[0])));
    }
    
    public ChangeCategoryOfAdjectivesFrame(Adjective caller, ArrayList<Adjective> adjectives) {
        this();
        this.adjectives = adjectives;
        this.caller = caller;
        includeCallerCheckBox.setText("Include adjective \"" + caller.getText() + "\"?");
        includeCallerCheckBox.setEnabled(false);
        mergeConflictsCheckBox.setEnabled(false);
        applyChanges.setEnabled(false);
        categoriesList.setModel(new DefaultComboBoxModel<String>(workingLexicon.getAdjCatManager().getCategoryNames().toArray(new String[0])));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        categoriesPanel = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        categoriesListPane = new javax.swing.JScrollPane();
        categoriesList = new javax.swing.JList<>();
        selectFromCategoryLabel = new javax.swing.JLabel();
        rightPanel = new javax.swing.JPanel();
        optionsPanel = new javax.swing.JPanel();
        includeCallerCheckBox = new javax.swing.JCheckBox();
        mergeConflictsCheckBox = new javax.swing.JCheckBox();
        selectOptionsLabel = new javax.swing.JLabel();
        infoLabel = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        applyChanges = new javax.swing.JButton();
        titleLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel.setLayout(new java.awt.BorderLayout());

        categoriesPanel.setLayout(new java.awt.GridLayout(1, 2, 10, 10));

        leftPanel.setLayout(new java.awt.BorderLayout());

        categoriesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        categoriesList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                categoriesListValueChanged(evt);
            }
        });
        categoriesListPane.setViewportView(categoriesList);

        leftPanel.add(categoriesListPane, java.awt.BorderLayout.CENTER);

        selectFromCategoryLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        selectFromCategoryLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectFromCategoryLabel.setText("Select a category");
        leftPanel.add(selectFromCategoryLabel, java.awt.BorderLayout.PAGE_START);

        categoriesPanel.add(leftPanel);

        rightPanel.setLayout(new java.awt.BorderLayout());

        includeCallerCheckBox.setText("Include caller in transfer?");
        optionsPanel.add(includeCallerCheckBox);

        mergeConflictsCheckBox.setText("Merge conflicts?");
        optionsPanel.add(mergeConflictsCheckBox);

        rightPanel.add(optionsPanel, java.awt.BorderLayout.CENTER);

        selectOptionsLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        selectOptionsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectOptionsLabel.setText("Select options");
        rightPanel.add(selectOptionsLabel, java.awt.BorderLayout.PAGE_START);

        categoriesPanel.add(rightPanel);

        mainPanel.add(categoriesPanel, java.awt.BorderLayout.CENTER);

        infoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoLabel.setText("Moving from ? category");
        mainPanel.add(infoLabel, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(cancelButton);

        applyChanges.setText("Apply Changes");
        applyChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyChangesActionPerformed(evt);
            }
        });
        buttonPanel.add(applyChanges);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Change Adjectival Category");
        getContentPane().add(titleLabel, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void applyChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyChangesActionPerformed
        ArrayList<Adjective> targetAdjectives = new ArrayList<Adjective>();
        targetAdjectives.addAll(adjectives);
        
        if ((includeCallerCheckBox.isSelected()) && (caller != null)) {
            targetAdjectives.add(caller);
        }
        
        if (mergeConflictsCheckBox.isSelected()) {
            for (Adjective adj : targetAdjectives) {
                if (! selectedCategory.getAdjectiveNames().contains(adj.getText())) {
                    selectedCategory.setCategory(adj);
                } else {
                    adj.getCategory().removeCategory(adj);
                }
            }
        } else {
            for (Adjective adj : targetAdjectives) {
                selectedCategory.setCategory(adj);
            }
        }
        
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_applyChangesActionPerformed

    private void categoriesListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_categoriesListValueChanged
        String selectedCategoryName = categoriesList.getSelectedValue();
        
        if (selectedCategoryName == null) {
            selectedCategory = null;
            includeCallerCheckBox.setEnabled(false);
            mergeConflictsCheckBox.setEnabled(false);
            applyChanges.setEnabled(false);
            return;
        }
        
        selectedCategory = workingLexicon.getAdjCatManager().getCategoryByName(selectedCategoryName);
        
        if (selectedCategory == null) {
            includeCallerCheckBox.setEnabled(false);
            mergeConflictsCheckBox.setEnabled(false);
            applyChanges.setEnabled(false);
            return;
        }
        
        includeCallerCheckBox.setEnabled(true);
        mergeConflictsCheckBox.setEnabled(true);
        applyChanges.setEnabled(true);
    }//GEN-LAST:event_categoriesListValueChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChangeCategoryOfAdjectivesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChangeCategoryOfAdjectivesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChangeCategoryOfAdjectivesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChangeCategoryOfAdjectivesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChangeCategoryOfAdjectivesFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyChanges;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JList<String> categoriesList;
    private javax.swing.JScrollPane categoriesListPane;
    private javax.swing.JPanel categoriesPanel;
    private javax.swing.JCheckBox includeCallerCheckBox;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JCheckBox mergeConflictsCheckBox;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JLabel selectFromCategoryLabel;
    private javax.swing.JLabel selectOptionsLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
