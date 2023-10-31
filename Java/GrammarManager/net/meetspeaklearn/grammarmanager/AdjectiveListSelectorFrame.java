/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import static net.meetspeaklearn.grammarmanager.WorkingDataManager.workingLexicon;

import java.util.*;
import javax.swing.*;
import net.meetspeaklearn.util.listener.*;

/**
 *
 * @author steve
 */
public class AdjectiveListSelectorFrame
        extends javax.swing.JFrame
        implements ICollectionModifier {
    
    private ArrayList<ICollectionModifiedListener> listeners;
    private ArrayList<AdjectivalCategory> categories;
    private ArrayList<Adjective> adjectives;
    private ArrayList<Adjective> selectedAdjectives;

    /**
     * Creates new form AdjectiveListSelectorFrame
     */
    public AdjectiveListSelectorFrame() {
        initComponents();
        listeners = new ArrayList<ICollectionModifiedListener>();
        categories = workingLexicon.getAdjCatManager().getCategoriesByPrecedence();
        ArrayList<String> categoryNames = workingLexicon.getAdjCatManager().getCategoryNamesByPrecedence();
        categoriesList.setModel(new DefaultComboBoxModel<String>(categoryNames.toArray(new String[0])));
    }
    
    public void addListener(ICollectionModifiedListener listener) {
        if (listeners.contains(listener)) return;
        listeners.add(listener);
    }
    
    public void removeListener(ICollectionModifiedListener listener) {
        if (! listeners.contains(listener)) return;
        listeners.remove(listener);
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
        categoriesScrollPane = new javax.swing.JScrollPane();
        categoriesList = new javax.swing.JList<>();
        adjectivesPanel = new javax.swing.JPanel();
        adjectivesScrollPane = new javax.swing.JScrollPane();
        adjectivesList = new javax.swing.JList<>();
        selectorsPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        selectedPanel = new javax.swing.JPanel();
        selectedScrollPane = new javax.swing.JScrollPane();
        selectedList = new javax.swing.JList<>();
        titleLabel = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        selectButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(600, 2147483647));
        setPreferredSize(new java.awt.Dimension(600, 542));

        mainPanel.setPreferredSize(new java.awt.Dimension(600, 492));
        mainPanel.setLayout(new java.awt.GridLayout(1, 4, 4, 0));

        categoriesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Select a Category"));
        categoriesPanel.setLayout(new java.awt.BorderLayout());

        categoriesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        categoriesList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                categoriesListValueChanged(evt);
            }
        });
        categoriesScrollPane.setViewportView(categoriesList);

        categoriesPanel.add(categoriesScrollPane, java.awt.BorderLayout.CENTER);

        mainPanel.add(categoriesPanel);

        adjectivesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Select Adjectives"));
        adjectivesPanel.setLayout(new java.awt.BorderLayout());

        adjectivesScrollPane.setViewportView(adjectivesList);

        adjectivesPanel.add(adjectivesScrollPane, java.awt.BorderLayout.CENTER);

        mainPanel.add(adjectivesPanel);

        selectorsPanel.setMaximumSize(new java.awt.Dimension(200, 32767));
        selectorsPanel.setMinimumSize(new java.awt.Dimension(200, 223));
        selectorsPanel.setPreferredSize(new java.awt.Dimension(200, 492));
        selectorsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 40, 100));

        addButton.setText("Add >>");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        selectorsPanel.add(addButton);

        removeButton.setText("<< Remove");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        selectorsPanel.add(removeButton);

        mainPanel.add(selectorsPanel);

        selectedPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected Adjectives"));
        selectedPanel.setLayout(new java.awt.BorderLayout());

        selectedScrollPane.setViewportView(selectedList);

        selectedPanel.add(selectedScrollPane, java.awt.BorderLayout.CENTER);

        mainPanel.add(selectedPanel);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Select Adjectives");
        getContentPane().add(titleLabel, java.awt.BorderLayout.PAGE_START);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(cancelButton);

        selectButton.setText("Select");
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(selectButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void categoriesListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_categoriesListValueChanged
        int index = categoriesList.getSelectedIndex();
        AdjectivalCategory cat = categories.get(index);
        HashSet<Adjective> adjectiveSet = cat.getAdjectives();
        
        if (adjectiveSet == null) {
            adjectiveSet = new HashSet<Adjective>();
        }
        
        ArrayList<Adjective> adjectiveList = new ArrayList<Adjective>();
        adjectiveList.addAll(adjectiveSet);
        Collections.sort(adjectiveList, Adjective.getComparator());
        adjectives = adjectiveList;
        
        String[] names = new String[adjectives.size()];
        int i = 0;
        
        for (Adjective adj : adjectives) {
            names[i] = adjectives.get(i).getText();
            i += 1;
        }
        
        adjectivesList.setModel(new DefaultComboBoxModel<String>(names));
    }//GEN-LAST:event_categoriesListValueChanged

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        int[] indices = adjectivesList.getSelectedIndices();
        
        if (selectedAdjectives == null)
            selectedAdjectives = new ArrayList<Adjective>();
        
        for (int i : indices) {
            Adjective a = adjectives.get(i);
            
            if (selectedAdjectives.contains(a)) continue;
            
            selectedAdjectives.add(a);
        }
        
        Collections.sort(selectedAdjectives, Adjective.getComparator());
        
        String[] names = new String[selectedAdjectives.size()];
        int i = 0;
        
        for (Adjective adj : selectedAdjectives) {
            names[i] = selectedAdjectives.get(i).getText();
            i += 1;
        }
        
        selectedList.setModel(new DefaultComboBoxModel<String>(names));
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        int[] indices = selectedList.getSelectedIndices();
        
        if (selectedAdjectives == null)
            selectedAdjectives = new ArrayList<Adjective>();
        
        ArrayList<Adjective> removals = new ArrayList<Adjective>();
        
        for (int i : indices) {
            Adjective a = selectedAdjectives.get(i);
            removals.add(a);
        }
        
        for (Adjective a : removals)
            selectedAdjectives.remove(a);
        
        String[] names = new String[selectedAdjectives.size()];
        int i = 0;
        
        for (Adjective adj : selectedAdjectives) {
            names[i] = selectedAdjectives.get(i).getText();
            i += 1;
        }
        
        selectedList.setModel(new DefaultComboBoxModel<String>(names));
    }//GEN-LAST:event_removeButtonActionPerformed

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        for (ICollectionModifiedListener listener : listeners)
            listener.collectionModified(selectedAdjectives);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_selectButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

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
            java.util.logging.Logger.getLogger(AdjectiveListSelectorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdjectiveListSelectorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdjectiveListSelectorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdjectiveListSelectorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdjectiveListSelectorFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JList<String> adjectivesList;
    private javax.swing.JPanel adjectivesPanel;
    private javax.swing.JScrollPane adjectivesScrollPane;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JList<String> categoriesList;
    private javax.swing.JPanel categoriesPanel;
    private javax.swing.JScrollPane categoriesScrollPane;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton selectButton;
    private javax.swing.JList<String> selectedList;
    private javax.swing.JPanel selectedPanel;
    private javax.swing.JScrollPane selectedScrollPane;
    private javax.swing.JPanel selectorsPanel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}