/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import static net.meetspeaklearn.grammarmanager.WorkingDataManager.workingLexicon;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import static net.meetspeaklearn.grammarmanager.WorkingDataManager.LEXICA_PATH;
import static net.meetspeaklearn.grammarmanager.WorkingDataManager.LEXICA_SAVE_FAILED;

/**
 *
 * @author steve
 */
public class ExportCompiledExample extends javax.swing.JFrame {
    private final static int EXCLUSION_CHECK_BOX_COUNT = 6;
    
    private AdjectiveTable relatedAdjectivesTable;
    private AdjectiveTable filteredAdjectivesTable;
    private JCheckBox[] exclusionCheckBoxes;
    
    private AcademicLevel level;
    private ArrayList<Noun> allNouns;
    private ArrayList<Noun> selectedNouns;
    private HashMap<Lex, ArrayList<Adjective>> redundantAdjectiveMap;
    private ArrayList<Lex> redundantlyUsedLexemes;
    private HashSet<Adjective> implicitlySelectedAdjectives;
    private ArrayList<Adjective> currentlyDisplayCategoriesForExclusion;
    private HashSet<Adjective> excludedAdjectives;
    
    private net.meetspeaklearn.compiler.Example example;

    /**
     * Creates new form ExportCompiledExample
     */
    public ExportCompiledExample() {
        initComponents();
        
        example = null;        
        level = AcademicLevel.BEGINNER;
        allNouns = new ArrayList<Noun>();
        selectedNouns = new ArrayList<Noun>();
        redundantAdjectiveMap = new HashMap<Lex, ArrayList<Adjective>>();
        currentlyDisplayCategoriesForExclusion = new ArrayList<Adjective>();
        excludedAdjectives = new HashSet<Adjective>();
        redundantlyUsedLexemes = new ArrayList<Lex>();
        implicitlySelectedAdjectives = new HashSet<Adjective>();
        
        exclusionCheckBoxes = new JCheckBox[EXCLUSION_CHECK_BOX_COUNT];
        
        exclusionCheckBoxes[0] = categoryCheckBox1;
        exclusionCheckBoxes[1] = categoryCheckBox2;
        exclusionCheckBoxes[2] = categoryCheckBox3;
        exclusionCheckBoxes[3] = categoryCheckBox4;
        exclusionCheckBoxes[4] = categoryCheckBox5;
        exclusionCheckBoxes[5] = categoryCheckBox6;
        
        hideCategoriesForExclusion();
        addListenersToCategoriesForExclusion();
        
        relatedAdjectivesTable = new AdjectiveTable(new ArrayList<Adjective>());
        bottomSelectionPanel.add(relatedAdjectivesTable, java.awt.BorderLayout.CENTER);
        
        filteredAdjectivesTable = new AdjectiveTable(new ArrayList<Adjective>());
        filteredAdjectivesPanel.add(filteredAdjectivesTable, java.awt.BorderLayout.CENTER);

        academicLevelComboBox.setModel(new DefaultComboBoxModel<String>(AcademicLevel.valuesAsStrings()));
        academicLevelComboBox.setSelectedItem(level.toString());
        
        allNouns = workingLexicon.getAllNounsWithinAcademicLevelRange(AcademicLevel.BEGINNER, level);
        updateNounList(availableNounsList, allNouns);
        
        updateNounList(selectedNounsList, selectedNouns);
    }
    
    private net.meetspeaklearn.compiler.Example createExample() {
        HashSet<Adjective> filteredAdjectivesAsSet = new HashSet<Adjective>();
        filteredAdjectivesAsSet.addAll(implicitlySelectedAdjectives);
        filteredAdjectivesAsSet.removeAll(excludedAdjectives);
        
        net.meetspeaklearn.compiler.Example newExample
                = new net.meetspeaklearn.compiler.Example(selectedNouns, filteredAdjectivesAsSet);
        
        newExample.setName(nameTextField.getText().trim());
        newExample.setDescription(descriptionTextField.getText().trim());
        newExample.setCreator(creatorNameTextField.getText().trim());
        
        System.out.println(newExample.toString());
        
        return newExample;
    }
    
    private void registerAdjectiveForRedundancies(Adjective adj) {
        // redundantAdjectiveMap = new HashMap<Lex, ArrayList<Adjective>>();
        ArrayList<Adjective> redundantAdjectives;
        
        if (! redundantAdjectiveMap.containsKey(adj.getLexeme())) {
            redundantAdjectives = new ArrayList<Adjective>();
            redundantAdjectiveMap.put(adj.getLexeme(), redundantAdjectives);
        } else {
            redundantAdjectives = redundantAdjectiveMap.get(adj.getLexeme());
        }
        
        redundantAdjectives.add(adj);
    }
    
    private ArrayList<Lex> getLexemesOfRedundantAdjectives() {
        ArrayList<Lex> lexemes = new ArrayList<Lex>();
        
        redundantAdjectiveMap.forEach((lexeme, adjList) -> {
            if (adjList.size() > 1) lexemes.add(lexeme);
        });
        
        return lexemes;
    }
    
    private int indexOfCategoryForExclusionCheckBox(JCheckBox cb) {
        for (int i = 0; i < EXCLUSION_CHECK_BOX_COUNT; i++)
            if (exclusionCheckBoxes[i] == cb) return i;
        return -1;
    }
    
    private void addListenersToCategoriesForExclusion() {
        for (int i = 0; i < EXCLUSION_CHECK_BOX_COUNT; i++) {
            JCheckBox currentCB = exclusionCheckBoxes[i];
            currentCB.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    JCheckBox cb = (JCheckBox) evt.getSource();
                    int index = indexOfCategoryForExclusionCheckBox(cb);
                    Adjective adj = currentlyDisplayCategoriesForExclusion.get(index);
                    
                    System.out.println("actionPerformed: " + cb);
                    
                    if (cb.isSelected()) {
                        if (! excludedAdjectives.contains(adj)) {
                            System.out.println("Excluding: " + adj);
                            excludedAdjectives.add(adj);
                        }
                    } else {
                        if (excludedAdjectives.contains(adj))
                            System.out.println("Including: " + adj);
                            excludedAdjectives.remove(adj);
                    }
                    
                    updateFilteredAdjectivesTable();
            }
            });
        }
    }
    
    private void hideCategoriesForExclusion() {
        for (JCheckBox cb : exclusionCheckBoxes) {
            cb.setSelected(false);
            cb.setVisible(false);
        }
    }
    
    private void displayCategoriesForExclusion (Collection<Adjective> adjectives) {
        int count = Math.min(adjectives.size(), EXCLUSION_CHECK_BOX_COUNT);
        int i = 0;
        
        for (Adjective current : adjectives) {
            exclusionCheckBoxes[i].setText(current.getCategory().getName());
            exclusionCheckBoxes[i].setSelected(excludedAdjectives.contains(current));
            exclusionCheckBoxes[i].setVisible(true);
            i += 1;
        }
        
        while (i < EXCLUSION_CHECK_BOX_COUNT) {
            exclusionCheckBoxes[i].setSelected(false);
            exclusionCheckBoxes[i].setVisible(false);
            i += 1;
        }
    }
    
    private void updateNounList(javax.swing.JList list, ArrayList<Noun> nouns) {
        Collections.sort(nouns, Noun.getComparator());
        String[] nounsAsArray;
        
        if (nouns != null) {
            int count = nouns.size();
            nounsAsArray = new String[count];
            
            for (int i = 0; i < count; i++)
                nounsAsArray[i] = nouns.get(i).toString();
        } else {
            nounsAsArray = new String[0];
        }
        
        list.setModel(new DefaultComboBoxModel<String>(nounsAsArray));
    }
    
    private void updateFilteredAdjectivesTable() {
        ArrayList<Adjective> filteredAdjectivesAsList = new ArrayList<Adjective>();
        filteredAdjectivesAsList.addAll(implicitlySelectedAdjectives);
        filteredAdjectivesAsList.removeAll(excludedAdjectives);
        
        Collections.sort(filteredAdjectivesAsList, Adjective.getComparator());
        filteredAdjectivesTable.setAdjectives(filteredAdjectivesAsList);
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
        centerPanel = new javax.swing.JPanel();
        adjectiveManagementToolsPanel = new javax.swing.JPanel();
        exclusionControlsPanel = new javax.swing.JPanel();
        redundantlyUsedLexemePanel = new javax.swing.JScrollPane();
        redundantlyUsedLexemeList = new javax.swing.JList<>();
        redundancyControlsPanel = new javax.swing.JPanel();
        categoryCheckBox1 = new javax.swing.JCheckBox();
        categoryCheckBox2 = new javax.swing.JCheckBox();
        categoryCheckBox3 = new javax.swing.JCheckBox();
        categoryCheckBox4 = new javax.swing.JCheckBox();
        categoryCheckBox5 = new javax.swing.JCheckBox();
        categoryCheckBox6 = new javax.swing.JCheckBox();
        otherControls = new javax.swing.JPanel();
        filteredAdjectivesPanel = new javax.swing.JPanel();
        filteredAdjectivesTitle = new javax.swing.JLabel();
        nounSelectionPanel = new javax.swing.JPanel();
        topSelectionPanel = new javax.swing.JPanel();
        availableNounsPanel = new javax.swing.JPanel();
        availableNounsScrollPane = new javax.swing.JScrollPane();
        availableNounsList = new javax.swing.JList<>();
        availableNounsTitle = new javax.swing.JLabel();
        selectionToolsPanel = new javax.swing.JPanel();
        selectNounButton = new javax.swing.JButton();
        unselectNounButton = new javax.swing.JButton();
        selectedNounsPanel = new javax.swing.JPanel();
        selectedNounsScrollPane = new javax.swing.JScrollPane();
        selectedNounsList = new javax.swing.JList<>();
        selectedNounsTitle = new javax.swing.JLabel();
        bottomSelectionPanel = new javax.swing.JPanel();
        selectedAdjectivesTitle = new javax.swing.JLabel();
        topPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        filtersPanel = new javax.swing.JPanel();
        filterAcademicLevelPanel = new javax.swing.JPanel();
        academicLevelComboBox = new javax.swing.JComboBox<>();
        namePanel = new javax.swing.JPanel();
        nameTextField = new javax.swing.JTextField();
        descriptionPanel = new javax.swing.JPanel();
        descriptionTextField = new javax.swing.JTextField();
        creatorNamePanel = new javax.swing.JPanel();
        creatorNameTextField = new javax.swing.JTextField();
        buttonPanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        exportToJavaScriptButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1200, 2147483647));
        setMinimumSize(new java.awt.Dimension(1200, 492));

        mainPanel.setMaximumSize(new java.awt.Dimension(1200, 2147483647));
        mainPanel.setMinimumSize(new java.awt.Dimension(1200, 379));
        mainPanel.setPreferredSize(new java.awt.Dimension(1200, 552));
        mainPanel.setLayout(new java.awt.BorderLayout(10, 0));

        centerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Manage Adjectives:"));
        centerPanel.setLayout(new java.awt.GridLayout(2, 1, 10, 10));

        adjectiveManagementToolsPanel.setMaximumSize(new java.awt.Dimension(600, 32767));
        adjectiveManagementToolsPanel.setMinimumSize(new java.awt.Dimension(600, 79));
        adjectiveManagementToolsPanel.setPreferredSize(new java.awt.Dimension(600, 231));
        adjectiveManagementToolsPanel.setLayout(new java.awt.GridLayout(1, 2, 10, 10));

        exclusionControlsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Redundancies"));
        exclusionControlsPanel.setMaximumSize(new java.awt.Dimension(300, 32767));
        exclusionControlsPanel.setPreferredSize(new java.awt.Dimension(300, 231));
        exclusionControlsPanel.setLayout(new java.awt.GridLayout(1, 2, 0, 4));

        redundantlyUsedLexemePanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        redundantlyUsedLexemePanel.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        redundantlyUsedLexemeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        redundantlyUsedLexemeList.setMaximumSize(new java.awt.Dimension(200, 1000));
        redundantlyUsedLexemeList.setMinimumSize(new java.awt.Dimension(200, 500));
        redundantlyUsedLexemeList.setPreferredSize(new java.awt.Dimension(200, 1000));
        redundantlyUsedLexemeList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                redundantlyUsedLexemeListValueChanged(evt);
            }
        });
        redundantlyUsedLexemePanel.setViewportView(redundantlyUsedLexemeList);

        exclusionControlsPanel.add(redundantlyUsedLexemePanel);

        redundancyControlsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Exclude?"));
        redundancyControlsPanel.setMaximumSize(new java.awt.Dimension(203, 32767));
        redundancyControlsPanel.setPreferredSize(new java.awt.Dimension(220, 208));
        redundancyControlsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        categoryCheckBox1.setText("Category");
        categoryCheckBox1.setPreferredSize(new java.awt.Dimension(200, 23));
        redundancyControlsPanel.add(categoryCheckBox1);

        categoryCheckBox2.setText("Category");
        categoryCheckBox2.setPreferredSize(new java.awt.Dimension(200, 23));
        redundancyControlsPanel.add(categoryCheckBox2);

        categoryCheckBox3.setText("Category");
        categoryCheckBox3.setPreferredSize(new java.awt.Dimension(200, 23));
        redundancyControlsPanel.add(categoryCheckBox3);

        categoryCheckBox4.setText("Category");
        categoryCheckBox4.setPreferredSize(new java.awt.Dimension(200, 23));
        redundancyControlsPanel.add(categoryCheckBox4);

        categoryCheckBox5.setText("Category");
        categoryCheckBox5.setPreferredSize(new java.awt.Dimension(200, 23));
        redundancyControlsPanel.add(categoryCheckBox5);

        categoryCheckBox6.setText("Category");
        categoryCheckBox6.setPreferredSize(new java.awt.Dimension(200, 23));
        redundancyControlsPanel.add(categoryCheckBox6);

        exclusionControlsPanel.add(redundancyControlsPanel);

        adjectiveManagementToolsPanel.add(exclusionControlsPanel);

        javax.swing.GroupLayout otherControlsLayout = new javax.swing.GroupLayout(otherControls);
        otherControls.setLayout(otherControlsLayout);
        otherControlsLayout.setHorizontalGroup(
            otherControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 422, Short.MAX_VALUE)
        );
        otherControlsLayout.setVerticalGroup(
            otherControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 224, Short.MAX_VALUE)
        );

        adjectiveManagementToolsPanel.add(otherControls);

        centerPanel.add(adjectiveManagementToolsPanel);

        filteredAdjectivesPanel.setLayout(new java.awt.BorderLayout());

        filteredAdjectivesTitle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        filteredAdjectivesTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        filteredAdjectivesTitle.setText("Filtered Adjectives");
        filteredAdjectivesPanel.add(filteredAdjectivesTitle, java.awt.BorderLayout.NORTH);

        centerPanel.add(filteredAdjectivesPanel);

        mainPanel.add(centerPanel, java.awt.BorderLayout.CENTER);

        nounSelectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Select nouns:"));
        nounSelectionPanel.setPreferredSize(new java.awt.Dimension(600, 552));
        nounSelectionPanel.setLayout(new java.awt.GridLayout(2, 1, 10, 10));

        topSelectionPanel.setLayout(new java.awt.GridLayout(1, 3, 4, 4));

        availableNounsPanel.setLayout(new java.awt.BorderLayout());

        availableNounsScrollPane.setViewportView(availableNounsList);

        availableNounsPanel.add(availableNounsScrollPane, java.awt.BorderLayout.CENTER);

        availableNounsTitle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        availableNounsTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        availableNounsTitle.setText("Available Nouns");
        availableNounsPanel.add(availableNounsTitle, java.awt.BorderLayout.PAGE_START);

        topSelectionPanel.add(availableNounsPanel);

        selectionToolsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 75, 75));

        selectNounButton.setText("Select >>");
        selectNounButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectNounButtonActionPerformed(evt);
            }
        });
        selectionToolsPanel.add(selectNounButton);

        unselectNounButton.setText("<< Remove");
        unselectNounButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unselectNounButtonActionPerformed(evt);
            }
        });
        selectionToolsPanel.add(unselectNounButton);

        topSelectionPanel.add(selectionToolsPanel);

        selectedNounsPanel.setLayout(new java.awt.BorderLayout());

        selectedNounsScrollPane.setViewportView(selectedNounsList);

        selectedNounsPanel.add(selectedNounsScrollPane, java.awt.BorderLayout.CENTER);

        selectedNounsTitle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        selectedNounsTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectedNounsTitle.setText("Selected Nouns");
        selectedNounsPanel.add(selectedNounsTitle, java.awt.BorderLayout.PAGE_START);

        topSelectionPanel.add(selectedNounsPanel);

        nounSelectionPanel.add(topSelectionPanel);

        bottomSelectionPanel.setLayout(new java.awt.BorderLayout());

        selectedAdjectivesTitle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        selectedAdjectivesTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectedAdjectivesTitle.setText("Related Adjectives");
        bottomSelectionPanel.add(selectedAdjectivesTitle, java.awt.BorderLayout.NORTH);

        nounSelectionPanel.add(bottomSelectionPanel);

        mainPanel.add(nounSelectionPanel, java.awt.BorderLayout.WEST);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        topPanel.setLayout(new java.awt.BorderLayout());

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Export Compiled Example");
        topPanel.add(titleLabel, java.awt.BorderLayout.NORTH);

        filtersPanel.setMinimumSize(new java.awt.Dimension(1200, 80));
        filtersPanel.setPreferredSize(new java.awt.Dimension(1200, 80));
        filtersPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        filterAcademicLevelPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Academic Level"));

        academicLevelComboBox.setPreferredSize(new java.awt.Dimension(120, 24));
        academicLevelComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                academicLevelComboBoxActionPerformed(evt);
            }
        });
        filterAcademicLevelPanel.add(academicLevelComboBox);

        filtersPanel.add(filterAcademicLevelPanel);

        namePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Example Name:"));
        namePanel.setMinimumSize(new java.awt.Dimension(180, 70));
        namePanel.setPreferredSize(new java.awt.Dimension(180, 70));
        namePanel.setLayout(new java.awt.BorderLayout());

        nameTextField.setMinimumSize(new java.awt.Dimension(170, 24));
        nameTextField.setPreferredSize(new java.awt.Dimension(170, 24));
        namePanel.add(nameTextField, java.awt.BorderLayout.CENTER);

        filtersPanel.add(namePanel);

        descriptionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Description:"));
        descriptionPanel.setMinimumSize(new java.awt.Dimension(400, 70));
        descriptionPanel.setPreferredSize(new java.awt.Dimension(400, 70));
        descriptionPanel.setLayout(new java.awt.BorderLayout());
        descriptionPanel.add(descriptionTextField, java.awt.BorderLayout.CENTER);

        filtersPanel.add(descriptionPanel);

        creatorNamePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Creator Name:"));
        creatorNamePanel.setMinimumSize(new java.awt.Dimension(200, 57));
        creatorNamePanel.setPreferredSize(new java.awt.Dimension(200, 70));
        creatorNamePanel.setLayout(new java.awt.BorderLayout());
        creatorNamePanel.add(creatorNameTextField, java.awt.BorderLayout.CENTER);

        filtersPanel.add(creatorNamePanel);

        topPanel.add(filtersPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(topPanel, java.awt.BorderLayout.PAGE_START);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(closeButton);

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(saveButton);

        exportToJavaScriptButton.setText("Export to JavaScript");
        exportToJavaScriptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportToJavaScriptButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(exportToJavaScriptButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void selectNounButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectNounButtonActionPerformed
        int[] indices = availableNounsList.getSelectedIndices();
        int count = indices.length;
        Noun[] nouns = new Noun[count];
        
        for (int i = 0; i < count; i++) nouns[i] = allNouns.get(indices[i]);
        
        for (int i = 0; i < count; i++) {
            Noun noun = nouns[i];
            selectedNouns.add(noun);
            allNouns.remove(noun);
        }
        
        updateNounList(availableNounsList, allNouns);
        updateNounList(selectedNounsList, selectedNouns);
        
        updateAdjectivesTable();
    }//GEN-LAST:event_selectNounButtonActionPerformed

    private void unselectNounButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unselectNounButtonActionPerformed
        int[] indices = selectedNounsList.getSelectedIndices();
        int count = indices.length;
        Noun[] nouns = new Noun[count];
        
        for (int i = 0; i < count; i++) nouns[i] = selectedNouns.get(indices[i]);
        
        for (int i = 0; i < count; i++) {
            Noun noun = nouns[i];
            allNouns.add(noun);
            selectedNouns.remove(noun);
        }
        
        updateNounList(availableNounsList, allNouns);
        updateNounList(selectedNounsList, selectedNouns);
        
        updateAdjectivesTable();
    }//GEN-LAST:event_unselectNounButtonActionPerformed

    private void academicLevelComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_academicLevelComboBoxActionPerformed
        level = AcademicLevel.fromString((String) academicLevelComboBox.getSelectedItem());
        
        // Make sure all nouns in the selectedNouns list are within the new range of level
        
        ArrayList<Noun> copyOfSelectedNouns = new ArrayList<Noun>();
        copyOfSelectedNouns.addAll(selectedNouns);
        
        for (Noun n : copyOfSelectedNouns) {
            if (n.getLexeme().getLevel().compareTo(level) > 0) {
                selectedNouns.remove(n);
            }
        }
        
        updateNounList(selectedNounsList, selectedNouns);
        
        // Get a new set of nouns within the range of level
        
        allNouns = workingLexicon.getAllNounsWithinAcademicLevelRange(AcademicLevel.BEGINNER, level);
        updateNounList(availableNounsList, allNouns);
        
        // Update the adjectives table
        
        updateAdjectivesTable();
    }//GEN-LAST:event_academicLevelComboBoxActionPerformed

    private void redundantlyUsedLexemeListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_redundantlyUsedLexemeListValueChanged
        int selectedIndex = redundantlyUsedLexemeList.getSelectedIndex();
        
        if (selectedIndex != -1) {
            Lex lememe = redundantlyUsedLexemes.get(selectedIndex);
            currentlyDisplayCategoriesForExclusion.clear();
            currentlyDisplayCategoriesForExclusion.addAll(redundantAdjectiveMap.get(lememe));
            displayCategoriesForExclusion(currentlyDisplayCategoriesForExclusion);
        } else {
            hideCategoriesForExclusion();
        }
    }//GEN-LAST:event_redundantlyUsedLexemeListValueChanged

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        JFileChooser finder = new JFileChooser();
        finder.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
        int returnVal = finder.showSaveDialog(this);
        net.meetspeaklearn.compiler.Example example = createExample();
        
        if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = finder.getSelectedFile();
            String fileName = file.toString();
            String extension = "";
            int posOfDot = fileName.lastIndexOf(".");

            if (posOfDot == -1) {
                fileName += ".xml";
            } else if (! fileName.toLowerCase().endsWith(".xml")) {
                fileName += ".xml";
            }
            
            System.out.println("File name: " + fileName);
            
            try {
                XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(fileName)));
                
                encoder.writeObject(example);
                encoder.close();
            } catch (FileNotFoundException fileNotFound1) {
                System.err.println("SaveExample(): Failed to save: " + fileName);
            }
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void exportToJavaScriptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportToJavaScriptButtonActionPerformed
        JFileChooser finder = new JFileChooser();
        finder.setFileFilter(new FileNameExtensionFilter("JavaScript Files", "js"));
        int returnVal = finder.showSaveDialog(this);
        net.meetspeaklearn.compiler.Example example = createExample();
        
        if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = finder.getSelectedFile();
            String fileName = file.toString();
            String extension = "";
            int posOfDot = fileName.lastIndexOf(".");

            if (posOfDot == -1) {
                fileName += ".js";
            } else if (! fileName.toLowerCase().endsWith(".js")) {
                fileName += ".js";
            }
            
            System.out.println("File name: " + fileName);
            
            try {
                FileWriter output = new FileWriter(fileName);
                
                output.write("// Courseware generated by Ogma, Meet, Speak, & Learn's Artificial Intelligence\n");
                output.write("// Conceived, designed, and implemented by Stephen DeVoy\n");
                output.write("// 2020 © Meet, Speak, & Learn. All Rights Reserved.\n\n");
                
                output.write(example.toJavaScipt());
                output.write("\n");
                
                output.close();
            } catch (IOException fileNotFound) {
                System.err.println("SaveExample(): Failed to save: " + fileName);
            }
        }
    }//GEN-LAST:event_exportToJavaScriptButtonActionPerformed

    void updateAdjectivesTable() {
        implicitlySelectedAdjectives.clear();
        
        for (Noun n : selectedNouns) {
            implicitlySelectedAdjectives.addAll(n.getAllInheritedAdjectives(level));
        }
        
        ArrayList<Adjective> adjectivesAsList = new ArrayList<Adjective>();
        
        adjectivesAsList.addAll(implicitlySelectedAdjectives);
        
        Collections.sort(adjectivesAsList, Adjective.getComparator());
        
        relatedAdjectivesTable.setAdjectives(adjectivesAsList);
        
        updateFilteredAdjectivesTable();
        
        redundantAdjectiveMap.clear();
        
        for (Adjective adj : adjectivesAsList) {
            registerAdjectiveForRedundancies(adj);
        }
        
        redundantlyUsedLexemes.clear();
        redundantlyUsedLexemes.addAll(getLexemesOfRedundantAdjectives());
        
        Collections.sort(redundantlyUsedLexemes, Lex.getComparator());
        
        redundantlyUsedLexemeList.setModel(new DefaultComboBoxModel<String>(Lex.toString(redundantlyUsedLexemes)));
    }
    
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
            java.util.logging.Logger.getLogger(ExportCompiledExample.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExportCompiledExample.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExportCompiledExample.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExportCompiledExample.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExportCompiledExample().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> academicLevelComboBox;
    private javax.swing.JPanel adjectiveManagementToolsPanel;
    private javax.swing.JList<String> availableNounsList;
    private javax.swing.JPanel availableNounsPanel;
    private javax.swing.JScrollPane availableNounsScrollPane;
    private javax.swing.JLabel availableNounsTitle;
    private javax.swing.JPanel bottomSelectionPanel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JCheckBox categoryCheckBox1;
    private javax.swing.JCheckBox categoryCheckBox2;
    private javax.swing.JCheckBox categoryCheckBox3;
    private javax.swing.JCheckBox categoryCheckBox4;
    private javax.swing.JCheckBox categoryCheckBox5;
    private javax.swing.JCheckBox categoryCheckBox6;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel creatorNamePanel;
    private javax.swing.JTextField creatorNameTextField;
    private javax.swing.JPanel descriptionPanel;
    private javax.swing.JTextField descriptionTextField;
    private javax.swing.JPanel exclusionControlsPanel;
    private javax.swing.JButton exportToJavaScriptButton;
    private javax.swing.JPanel filterAcademicLevelPanel;
    private javax.swing.JPanel filteredAdjectivesPanel;
    private javax.swing.JLabel filteredAdjectivesTitle;
    private javax.swing.JPanel filtersPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel namePanel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JPanel nounSelectionPanel;
    private javax.swing.JPanel otherControls;
    private javax.swing.JPanel redundancyControlsPanel;
    private javax.swing.JList<String> redundantlyUsedLexemeList;
    private javax.swing.JScrollPane redundantlyUsedLexemePanel;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton selectNounButton;
    private javax.swing.JLabel selectedAdjectivesTitle;
    private javax.swing.JList<String> selectedNounsList;
    private javax.swing.JPanel selectedNounsPanel;
    private javax.swing.JScrollPane selectedNounsScrollPane;
    private javax.swing.JLabel selectedNounsTitle;
    private javax.swing.JPanel selectionToolsPanel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel topPanel;
    private javax.swing.JPanel topSelectionPanel;
    private javax.swing.JButton unselectNounButton;
    // End of variables declaration//GEN-END:variables
}
