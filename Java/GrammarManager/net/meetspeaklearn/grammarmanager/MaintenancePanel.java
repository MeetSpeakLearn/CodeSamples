/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import static net.meetspeaklearn.grammarmanager.WorkingDataManager.currentWorkingDataManager;
import static net.meetspeaklearn.grammarmanager.WorkingDataManager.workingLexicon;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import static javax.swing.JOptionPane.*;

/**
 *
 * @author steve
 */
public class MaintenancePanel extends javax.swing.JFrame {
    
    private ArrayList<Lex> displayedLexemes = null;
    private ArrayList<Adjective> displayedAdjectives = null;
    AdjectiveMaintenancePanel adjectiveListValueChangedPanel = null;

    /**
     * Creates new form MaintenancePanel
     */
    public MaintenancePanel() {
        initComponents();
        
        lexemeList.setModel(new DefaultListModel<String>());
        adjectiveList.setModel(new DefaultListModel<String>());
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
        lexemePanel = new javax.swing.JPanel();
        lexemeScrollPane = new javax.swing.JScrollPane();
        lexemeList = new javax.swing.JList<>();
        lexemeSearchPanel = new javax.swing.JPanel();
        unusedLexemesButton = new javax.swing.JButton();
        lexemeCommandPanel = new javax.swing.JPanel();
        nounPanel = new javax.swing.JPanel();
        nounScrollPane = new javax.swing.JScrollPane();
        nounList = new javax.swing.JList<>();
        nounSearchPanel = new javax.swing.JPanel();
        nounCommandPanel = new javax.swing.JPanel();
        adjectivePanel = new javax.swing.JPanel();
        adjectiveScrollPane = new javax.swing.JScrollPane();
        adjectiveList = new javax.swing.JList<>();
        adjectiveSearchPanel = new javax.swing.JPanel();
        orphanedAdjectivesButton = new javax.swing.JButton();
        adjectiveCommandPanel = new javax.swing.JPanel();
        verbPanel = new javax.swing.JPanel();
        adverbPanel = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel.setLayout(new java.awt.GridLayout(1, 5, 10, 10));

        lexemePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Lexemes"));
        lexemePanel.setLayout(new java.awt.BorderLayout());

        lexemeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lexemeScrollPane.setViewportView(lexemeList);

        lexemePanel.add(lexemeScrollPane, java.awt.BorderLayout.CENTER);

        unusedLexemesButton.setText("Unused");
        unusedLexemesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unusedLexemesButtonActionPerformed(evt);
            }
        });
        lexemeSearchPanel.add(unusedLexemesButton);

        lexemePanel.add(lexemeSearchPanel, java.awt.BorderLayout.PAGE_START);
        lexemePanel.add(lexemeCommandPanel, java.awt.BorderLayout.PAGE_END);

        mainPanel.add(lexemePanel);

        nounPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Nouns"));
        nounPanel.setLayout(new java.awt.BorderLayout());

        nounList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        nounScrollPane.setViewportView(nounList);

        nounPanel.add(nounScrollPane, java.awt.BorderLayout.CENTER);
        nounPanel.add(nounSearchPanel, java.awt.BorderLayout.PAGE_START);
        nounPanel.add(nounCommandPanel, java.awt.BorderLayout.PAGE_END);

        mainPanel.add(nounPanel);

        adjectivePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Adjectives"));
        adjectivePanel.setLayout(new java.awt.BorderLayout());

        adjectiveList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        adjectiveList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                adjectiveListValueChanged(evt);
            }
        });
        adjectiveScrollPane.setViewportView(adjectiveList);

        adjectivePanel.add(adjectiveScrollPane, java.awt.BorderLayout.CENTER);

        orphanedAdjectivesButton.setText("Orphaned");
        orphanedAdjectivesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orphanedAdjectivesButtonActionPerformed(evt);
            }
        });
        adjectiveSearchPanel.add(orphanedAdjectivesButton);

        adjectivePanel.add(adjectiveSearchPanel, java.awt.BorderLayout.PAGE_START);
        adjectivePanel.add(adjectiveCommandPanel, java.awt.BorderLayout.PAGE_END);

        mainPanel.add(adjectivePanel);

        verbPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Verbs"));

        javax.swing.GroupLayout verbPanelLayout = new javax.swing.GroupLayout(verbPanel);
        verbPanel.setLayout(verbPanelLayout);
        verbPanelLayout.setHorizontalGroup(
            verbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 203, Short.MAX_VALUE)
        );
        verbPanelLayout.setVerticalGroup(
            verbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 398, Short.MAX_VALUE)
        );

        mainPanel.add(verbPanel);

        adverbPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Adverbs"));

        javax.swing.GroupLayout adverbPanelLayout = new javax.swing.GroupLayout(adverbPanel);
        adverbPanel.setLayout(adverbPanelLayout);
        adverbPanelLayout.setHorizontalGroup(
            adverbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 203, Short.MAX_VALUE)
        );
        adverbPanelLayout.setVerticalGroup(
            adverbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 398, Short.MAX_VALUE)
        );

        mainPanel.add(adverbPanel);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(closeButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void unusedLexemesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unusedLexemesButtonActionPerformed
        // ListModel
        displayedLexemes = workingLexicon.unusedLexemes();
        String[] lexemeNames = new String[displayedLexemes.size()];
        int i = 0;
        
        for (Lex lexeme : displayedLexemes) {
            lexemeNames[i] = lexeme.getLexeme();
            i += 1;
        }
        
        lexemeList.setModel(new DefaultComboBoxModel<String>(lexemeNames));
    }//GEN-LAST:event_unusedLexemesButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void orphanedAdjectivesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orphanedAdjectivesButtonActionPerformed
        displayedAdjectives = workingLexicon.orphanedAdjectives();
        String[] adjectiveNames = new String[displayedAdjectives.size()];
        int i = 0;
        
        for (Adjective adj : displayedAdjectives) {
            adjectiveNames[i] = adj.getText();
            i += 1;
        }
        
        adjectiveList.setModel(new DefaultComboBoxModel<String>(adjectiveNames));
    }//GEN-LAST:event_orphanedAdjectivesButtonActionPerformed

    private void adjectiveListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_adjectiveListValueChanged
        // AdjectiveMaintenancePanel
        if (! adjectiveList.isSelectionEmpty()) {
            int index = adjectiveList.getSelectedIndex();
            Adjective adj = displayedAdjectives.get(index);
            if (adj == null) System.out.println("adj is null");
            adjectiveListValueChangedPanel = AdjectiveMaintenancePanel.get(adj);
            adjectiveListValueChangedPanel.setLocationRelativeTo(this);
            adjectiveListValueChangedPanel.setVisible(true);
        }
    }//GEN-LAST:event_adjectiveListValueChanged

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
            java.util.logging.Logger.getLogger(MaintenancePanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MaintenancePanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MaintenancePanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MaintenancePanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MaintenancePanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel adjectiveCommandPanel;
    private javax.swing.JList<String> adjectiveList;
    private javax.swing.JPanel adjectivePanel;
    private javax.swing.JScrollPane adjectiveScrollPane;
    private javax.swing.JPanel adjectiveSearchPanel;
    private javax.swing.JPanel adverbPanel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel lexemeCommandPanel;
    private javax.swing.JList<String> lexemeList;
    private javax.swing.JPanel lexemePanel;
    private javax.swing.JScrollPane lexemeScrollPane;
    private javax.swing.JPanel lexemeSearchPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel nounCommandPanel;
    private javax.swing.JList<String> nounList;
    private javax.swing.JPanel nounPanel;
    private javax.swing.JScrollPane nounScrollPane;
    private javax.swing.JPanel nounSearchPanel;
    private javax.swing.JButton orphanedAdjectivesButton;
    private javax.swing.JButton unusedLexemesButton;
    private javax.swing.JPanel verbPanel;
    // End of variables declaration//GEN-END:variables
}
