/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import static net.meetspeaklearn.grammarmanager.WorkingDataManager.currentWorkingDataManager;
import static net.meetspeaklearn.grammarmanager.WorkingDataManager.workingLexicon;

import net.meetspeaklearn.util.listener.ICollectionModifiedListener;
import java.awt.Rectangle;
import java.util.Arrays;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.NO_OPTION;

/**
 *
 * @author steve
 */
public class GrammarManangerFrame extends javax.swing.JFrame {
    public static CreateLanguage createLanguageDialog = null;
    public static CreateLexicon createLexiconDialog = null;
    
    private Lex selectedLexeme;
    private Noun selectedNoun;
    private Adjective selectedAdjective;
    private Adjective[] selectedAdjectiveNonConflicts;
    private Adjective[] selectedAdjectiveConflicts;
    private AdjectivalCategoryManager adjCategoryManager;
    private NounEditorManager nounEditorManager;
    public SemanticNetworkNounIsAManager semanticNetworkNounIsAManager = new SemanticNetworkNounIsAManager();
    private EditLanguageAdjectivalCategoriesPanel adjectivalCategoriesPanel;
    EditLexiconAdjectivalCategoriesPanel editLexiconAdjectivalCategoriesPanel;
    AdjectiveTable nounAdjectiveTable;
    AdjectivePanelList adjPanelList;
    
    class NounEditorManager {
        public boolean edited;
        public boolean editing;
        public Lex lexeme;
        public Noun noun;
        public Noun.COUNTABILITY_TYPE countability;
        public Noun.NOUN_TYPE nounType;
        public boolean alwaysPlural;
        public Gender gender;
        
        public ArrayList<Adjective> adjectives = null;
        
        public NounEditorManager(Lex lex) {
            Lexicon lexicon = WorkingDataManager.currentWorkingDataManager.workingLexicon;
            
            edited = false;
            editing = false;
            lexeme = lex;
            
            if (lexeme == null) {
                if (selectedLexeme != null) {
                    lexeme = selectedLexeme;
                    noun = lexeme.getNoun();
                    selectedNoun = noun;
                } else {
                    System.err.println("NounEditorManager(): selectedLexeme is null.");
                    selectedNoun = noun = null;
                }
            } else {
                noun = lexeme.getNoun();
                selectedNoun = noun;
            }
            
            nounLexemeComboBox.setSelectedItem(lexeme.getLexeme());
            
            if (noun == null) {
                nounCountabilityComboBox.setSelectedIndex(0);
                nounTypeComboBox.setSelectedIndex(0);
                nounAlwaysPluralCheckBox.setSelected(false);
                
                masculineRadioButton.setSelected(false);
                feminineRadioButton.setSelected(false);
                neuterRadioButton.setSelected(false);
                animateRadioButton.setSelected(false);
                inanimateRadioButton.setSelected(false);
                commonRadioButton.setSelected(false);
                
                countability = Noun.COUNTABILITY_TYPE.UNKNOWN;
                nounType = Noun.NOUN_TYPE.UNKNOWN;
                alwaysPlural = false;
                gender = Gender.UNKNOWN;
                
                viewOntologyGeneralizeDiagramButton.setEnabled(false);
            } else if (noun != null) {
                nounCountabilityComboBox.setSelectedItem(noun.getCountability().toString());
                nounTypeComboBox.setSelectedItem(noun.getNounType().toString());
                nounPluralTextField.setText(noun.getDeclination(PartOfSpeech.NUMBER.PLURAL));
                nounAlwaysPluralCheckBox.setSelected(noun.isAlwaysPlural());
                
                Gender gender = noun.getGender();
                
                masculineRadioButton.setSelected(false);
                feminineRadioButton.setSelected(false);
                neuterRadioButton.setSelected(false);
                animateRadioButton.setSelected(false);
                inanimateRadioButton.setSelected(false);
                commonRadioButton.setSelected(false);
                
                System.err.println("gender=" + gender);
                
                if (gender == null) {
                    noun.setGender(Gender.UNKNOWN);
                    gender = Gender.UNKNOWN;
                }
                
                switch (gender) {
                    case MASCULINE:
                        masculineRadioButton.setSelected(true);
                        break;
                    case FEMININE:
                        feminineRadioButton.setSelected(true);
                        break;
                    case NEUTER:
                        neuterRadioButton.setSelected(true);
                        break;
                    case ANIMATE:
                        animateRadioButton.setSelected(true);
                        break;
                    case INANIMATE:
                        inanimateRadioButton.setSelected(true);
                        break;
                    case COMMON:
                        commonRadioButton.setSelected(true);
                        break;
                }
                
                countability = noun.getCountability();
                nounType = noun.getNounType();
                alwaysPlural = noun.isAlwaysPlural();
                gender = noun.getGender();
                
                viewOntologyGeneralizeDiagramButton.setEnabled(true);
            }
        }
        
        void setLexeme(String lexemeName) {
            lexeme = WorkingDataManager.currentWorkingDataManager.workingLexicon.get(lexemeName);
            edited = true;
        }
        
        void setCountability(String countabilityString) {
            countability = Noun.COUNTABILITY_TYPE.fromString(countabilityString);
            edited = true;
        }
        
        void setNounType(String nounTypeString) {
            nounType = Noun.NOUN_TYPE.fromString(nounTypeString);
            edited = true;
        }
        
        void setAlwaysPlural(boolean alwaysPlural) {
            this.alwaysPlural = alwaysPlural;
            edited = true;
        }
        
        void setGender(Gender gender) {
            this.gender = gender;
        }
        
        void setEditing(boolean value) {
            editing = value;
            
            nounEditButton.setEnabled(! editing);
            nounCancelEditButton.setEnabled(editing);
            nounSaveChangesButton.setEnabled(editing);
            
            nounLexemeComboBox.setEnabled(editing);
            nounCountabilityComboBox.setEnabled(editing);
            nounTypeComboBox.setEnabled(editing);
            nounAlwaysPluralCheckBox.setEnabled(editing);
            nounPluralTextField.setEnabled(editing);
            
            if (editing) {
                enableGenderRadioButtonsPerLanguage();
            } else {
                masculineRadioButton.setEnabled(false);
                feminineRadioButton.setEnabled(false);
                neuterRadioButton.setEnabled(false);
                animateRadioButton.setEnabled(false);
                inanimateRadioButton.setEnabled(false);
                commonRadioButton.setEnabled(false);
                nounPluralTextField.setEnabled(false);
            }
        }
        
        void save() {
            if (noun == null) {
                System.out.println("Creating noun.\n");
                noun = lexeme.createNoun();
                selectedNoun = noun;
            }
            
            noun.setCountability(countability);
            noun.setNounType(nounType);
            noun.setAlwaysPlural(alwaysPlural);
            noun.setCustomPlural(nounPluralTextField.getText());
            noun.setGender(gender);
            
            edited = false;
            
            setEditing(false);
        }
    }
    
    class SemanticNetworkNounIsAManager {
        public SemanticNetworkNounIsAManager() {
        }
        
        public void removeAll() {
            thingsThatIAmList.removeAll();
            thingsThatIAmThroughInferenceList.removeAll();
            thingsThatExistList.removeAll();
            nounAdjectiveTable.setAdjectives(new ArrayList<Adjective>());
        }
        
        public void updateThingsThatExistButIAmNot() {
            String[] nounsAsStrings = thingsThatExistButIAmNot();
            
            Arrays.sort(nounsAsStrings, new IgnoreCaseStringComparator());
            thingsThatExistList.setModel(new DefaultComboBoxModel<String>(nounsAsStrings));
        }
        
        public String[] thingsThatExistButIAmNot() {
            Lexicon lexicon = WorkingDataManager.currentWorkingDataManager.workingLexicon;
            
            if ((lexicon == null) || (selectedLexeme == null) || (selectedNoun == null)) {
                System.out.println("thingsThatExistButIAmNot(): requirements are null.");
                return new String[0];
            }
            
            HashSet<Noun> allNouns = new HashSet<Noun>();
            HashMap<String, Lex> allLexemes = lexicon.getStringToLexMap();
            Iterator<Entry<String, Lex>> it = allLexemes.entrySet().iterator();
            SemanticNode[] thingsNounIs = selectedNoun.get_things_I_am();
            
            while (it.hasNext()) {
                HashMap.Entry<String, Lex> pair = (HashMap.Entry<String, Lex>) it.next();
                
                Lex lexeme = pair.getValue();
                
                if (selectedLexeme != lexeme) {
                    Noun noun = lexeme.getNoun();
                    
                    if (noun == null) {
                        System.out.println("Lexeme " + lexeme.getLexeme() + " has no noun.\n");
                    }

                    if ((noun != null) && (noun != selectedNoun)) {
                        boolean iAmNot = true;
                        
                        for (SemanticNode n : thingsNounIs) {
                            if (((SemanticNode) noun) == n) {
                                iAmNot = false;
                            }
                        }
                        
                        if (iAmNot) allNouns.add(noun);
                    }
                }
            }
            
            int nounCount = allNouns.size();
            String[] result = new String[nounCount];
            int i = 0;
            
            for (Noun current : allNouns) {
                result[i++] = current.getLexeme().getLexeme();
            }
            
            return result;
        }
        
        public void updateThingsThatIAm() {
            String[] nounsAsStrings1 = thingsThatIAm();
            String[] nounsAsStrings2 = thingsThatIAmThroughInference();
            
            Arrays.sort(nounsAsStrings1, new IgnoreCaseStringComparator());
            Arrays.sort(nounsAsStrings2, new IgnoreCaseStringComparator());
            
            thingsThatIAmList.setModel(new DefaultComboBoxModel<String>(nounsAsStrings1));
            thingsThatIAmThroughInferenceList.setModel(new DefaultComboBoxModel<String>(nounsAsStrings2));
            
            thingsThatIAmList.invalidate();
            thingsThatIAmThroughInferenceList.invalidate();
        }
        
        public String[] thingsThatIAm() {
            Lexicon lexicon = WorkingDataManager.currentWorkingDataManager.workingLexicon;
            
            if ((lexicon == null) || (selectedLexeme == null) || (selectedNoun == null)) {
                System.out.println("thingsThatIAm(): requirements are null.");
                return new String[0];
            }
            
            SemanticNode[] thingsISpecialize = selectedNoun.get_things_I_specialize();
            int count = thingsISpecialize.length;
            String[] result = new String[count];
            
            for (int i = 0; i < count; i++)
                result[i] = ((Noun) thingsISpecialize[i]).getLexeme().getLexeme();
            
            return result;
        }
        
        public String[] thingsThatIAmThroughInference() {
            Lexicon lexicon = WorkingDataManager.currentWorkingDataManager.workingLexicon;
            
            if ((lexicon == null) || (selectedLexeme == null) || (selectedNoun == null)) {
                System.out.println("thingsThatIAm(): requirements are null.");
                return new String[0];
            }
            
            SemanticNode[] thingsISpecialize = selectedNoun.get_things_I_am();
            int count = thingsISpecialize.length;
            String[] result = new String[count];
            
            for (int i = 0; i < count; i++)
                result[i] = ((Noun) thingsISpecialize[i]).getLexeme().getLexeme();
            
            return result;
        }
        
        public void addThingsThatIAm(List<String> selectedItems) {
            Lexicon lexicon = WorkingDataManager.currentWorkingDataManager.workingLexicon;
            
            if ((lexicon == null) || (selectedLexeme == null) || (selectedNoun == null)) {
                System.out.println("addThingsThatIAm(): requirements are null.");
                return;
            }
            
            if (selectedItems != null) {
                for (String name : selectedItems) {
                    Lex lex = lexicon.get(name);
                    
                    if (lex != null) {
                        Noun noun = lex.getNoun();
                        
                        if (noun != null) {
                            ((SemanticNode) selectedNoun).is_a(noun);
                        }
                    }
                }
            }
            
            updateThingsThatExistButIAmNot();
            updateThingsThatIAm();
        }
        
        public void removeThingsThatIAm(List<String> selectedItems) {
            Lexicon lexicon = WorkingDataManager.currentWorkingDataManager.workingLexicon;
            
            if ((lexicon == null) || (selectedLexeme == null) || (selectedNoun == null)) {
                System.out.println("removeThingsThatIAm(): requirements are null.");
                return;
            }
            
            if (selectedItems != null) {
                for (String name : selectedItems) {
                    Lex lex = lexicon.get(name);
                    
                    if (lex != null) {
                        Noun noun = lex.getNoun();
                        
                        if (noun != null) {
                            ((SemanticNode) selectedNoun).remove_is_a(noun);
                        }
                    }
                }
            }
            
            updateThingsThatExistButIAmNot();
            updateThingsThatIAm();
        }
    }

    /**
     * Creates new form GrammarManangerFrame
     */
    public GrammarManangerFrame() { // Main
        initComponents();
        
        this.setLocationRelativeTo(null);
        new WorkingDataManager();
        
        selectedLexeme = null;
        
        WorkingDataManager.currentWorkingDataManager.LoadLanguages();
        currentWorkingDataManager.GetLanguage(Language.LANGUAGE_CODE.ENGLISH).setGrammaticalNumberSystemType(Language.GRAMMATICAL_NUMBER_SYSTEM_TYPE.SINGULAR_PLURAL);
        WorkingDataManager.workingLanguage = Language.LANGUAGE_CODE.ENGLISH;
        WorkingDataManager.currentWorkingDataManager.LoadLexica();
        
        // workingLexicon.makeEverythingSingular();
        // workingLexicon.setGrammaticalNumbers(PartOfSpeech.getGrammaticalNumbers(Language.GRAMMATICAL_NUMBER_SYSTEM_TYPE.SINGULAR_PLURAL));
        // workingLexicon.makeAllNounsCustom();
        // workingLexicon.makeAllNounsCustomWithExclusions();
        // workingLexicon.associateAllLexemesWithLexicon();
        
        if (WorkingDataManager.workingLexicon != null) {
            if (WorkingDataManager.workingLexicon.getAdjCatManager() == null) {
                if (showConfirmDialog(this, "The current lexicon, \""
                        + WorkingDataManager.workingLexicon.getName()
                        + "\", does not include adjectival categories.\n"
                        + "Import categories from the current language, \""
                        + WorkingDataManager.workingLanguage.toHumanReadableString()
                        + "\"?",
                        "Import Adjectival Categories",
                        YES_NO_OPTION)
                        == YES_OPTION) {
                    ArrayList<String> categories
                            = WorkingDataManager.currentWorkingDataManager
                                    .GetLanguage(WorkingDataManager.workingLanguage)
                                    .getAdjectivalCatgeoriesByPrecedence();
                    AdjectivalCategoryManager newAdjCatManager
                            = new AdjectivalCategoryManager(categories.toArray(new String[0]));
                    WorkingDataManager.workingLexicon.setAdjCatManager(newAdjCatManager);
                }
            }
            
            adjCategoryManager = WorkingDataManager.workingLexicon.getAdjCatManager();
        }
        
        viewPanelDeleteLexemeButton.setEnabled(false);
        viewPanelRenameLexeme.setEnabled(false);
        lexemeNameLabel.setText("");
        lexemeStringTextField.setText("");
        lexemeIsAbstractCheckBox.setSelected(false);
        lexemeIsDynamicCheckBox.setSelected(false);
        
        workingLanguageComboBox.setModel(new DefaultComboBoxModel<String>(Language.LANGUAGE_CODE.valuesAsString()));
        workingLanguageComboBox.setSelectedIndex(Language.LANGUAGE_CODE.indexOf(WorkingDataManager.workingLanguage));
        languageViewTopPanelTitleLabel.setText(WorkingDataManager.workingLanguage.toHumanReadableString());
        
        adjectivalCategoriesPanel = new EditLanguageAdjectivalCategoriesPanel();
        languagePropertiesPanel4.add(adjectivalCategoriesPanel);
        
        lexiconViewLexemeScrollPane.getViewport().setView(lexiconViewLexemeListControl);
        lexiconViewLexemeScrollPane.setAutoscrolls(true);
        lexiconViewLexemeScrollPane.setWheelScrollingEnabled(true);
        lexiconViewLexemeListControl.setLayoutOrientation(JList.VERTICAL);
        
        String[] lexicaNames = WorkingDataManager.currentWorkingDataManager.lexicaNames(WorkingDataManager.workingLanguage);
        int lexicaIndex = 0;
        String workingLexiconName = WorkingDataManager.workingLexicon.getName();
        
        for (int i = 0; i < lexicaNames.length; i++)
            if (workingLexiconName.compareToIgnoreCase(lexicaNames[i]) == 0) {
                lexicaIndex = i;
                break;
            }
        
        workingLexiconComboBox.setModel(new DefaultComboBoxModel<String>(lexicaNames));
        workingLexiconComboBox.setSelectedIndex(lexicaIndex);
        lexiconViewTopPanelTitleLabel.setText(workingLexiconName);
        
        thingsThatExistList.setModel(new DefaultComboBoxModel<String>(new String[0]));
        thingsThatIAmList.setModel(new DefaultComboBoxModel<String>(new String[0]));
        thingsThatIAmThroughInferenceList.setModel(new DefaultComboBoxModel<String>(new String[0]));
        addThingsThatIAmButton.setEnabled(false);
        removeThingsThatIAmButton.setEnabled(false);
        
        categoriesList.setModel(new DefaultComboBoxModel<String>(getCategoriesIPartakeIn()));
        categoriesIPartakeInList.setModel(new DefaultComboBoxModel<String>(new String[0]));
        
        editLexiconAdjectivalCategoriesPanel = new EditLexiconAdjectivalCategoriesPanel();
        adjectivalCategoriesPropertiesPanel.add(editLexiconAdjectivalCategoriesPanel);
        
        synchronizeToAdjectivalCategories();
        
        adjPanelList = new AdjectivePanelList(this);
        adjDimLeftPanel.add(adjPanelList);
        
        if (adjCategoryManager != null) {
            selCatComboBox.setModel(new DefaultComboBoxModel<String>(adjCategoryManager.getCategoryNames().toArray(new String[0])));
            selCatComboBox.setEnabled(true);
            
            setAdjGenderComboBox.setModel(new DefaultComboBoxModel<String>(Gender.valuesAsStrings()));
            setAdjGenderComboBox.setEnabled(true);
            
            setPluralityComboBox.setModel(new DefaultComboBoxModel<String>(Adjective.PLURALITY.valuesAsString()));
            setPluralityComboBox.setEnabled(true);
            setPluralityLabel.setVisible(false);
            setPluralityComboBox.setVisible(false);
            
            setQuantifierTypeComboBox.setModel(new DefaultComboBoxModel<String>(Adjective.QUANTIFIER_TYPE.valuesAsString()));
            setQuantifierTypeComboBox.setEnabled(true);
            setQuantifierTypeLabel.setVisible(false);
            setQuantifierTypeComboBox.setVisible(false);
        } else {
            selCatComboBox.setModel(new DefaultComboBoxModel<String>(new String[0]));
            selCatComboBox.setEnabled(false);
            
            setAdjGenderComboBox.setModel(new DefaultComboBoxModel<String>(new String[0]));
            setAdjGenderComboBox.setEnabled(false);
            
            setPluralityComboBox.setModel(new DefaultComboBoxModel<String>(new String[0]));
            setPluralityComboBox.setEnabled(true);
            setPluralityLabel.setVisible(false);
            setPluralityComboBox.setVisible(false);
            
            setQuantifierTypeComboBox.setModel(new DefaultComboBoxModel<String>(new String[0]));
            setQuantifierTypeComboBox.setEnabled(true);
            setQuantifierTypeLabel.setVisible(false);
            setQuantifierTypeComboBox.setVisible(false);
        }
        
        nounAdjectiveTable = new AdjectiveTable();
        adjectiveTablePanel.add(nounAdjectiveTable, java.awt.BorderLayout.CENTER);
        
        setAdjEditingEnabled(false);
        
        updateView();
    }
    
    private String[] getCategoriesIPartakeIn() {
        Lexicon lexicon = WorkingDataManager.workingLexicon;
        AdjectivalCategoryManager adjCatManager = lexicon.getAdjCatManager();
        
        return adjCatManager.getCategoryNames().toArray(new String[0]);
    }

    public AdjectivalCategoryManager getAdjCategoryManager() {
        return adjCategoryManager;
    }

    public void setAdjCategoryManager(AdjectivalCategoryManager adjCategoryManager) {
        this.adjCategoryManager = adjCategoryManager;
    }
    
    public void setAdjEditingEnabled(boolean enable) {
        selCatLabel.setEnabled(enable);
        selCatComboBox.setEnabled(enable);
        setPluralityComboBox.setEnabled(enable);
        setQuantifierTypeComboBox.setEnabled(enable);
        setAdjGenderComboBox.setEnabled(enable);
        adjDefPrompt.setEnabled(enable);
        adjDefTextPane.setEnabled(enable);
        saveAdjectiveChangesButton.setEnabled(enable);
        deleteAdjectiveButton.setEnabled(enable);
    }
    
    public void adjectivalCategoryPanelListAdjectiveSelected(Adjective adj) {
        // Callback
        
        selectedAdjective = adj;
        setAdjEditingEnabled(adj != null);
        
        if (adj == null) {
            selCatComboBox.setSelectedItem(null);
            setAdjGenderComboBox.setSelectedItem(null);
            setPluralityComboBox.setSelectedItem(null);
            setQuantifierTypeComboBox.setSelectedItem(null);
        } else if (adj.getCategory() != null) {
            selCatComboBox.setSelectedItem(adj.getCategory().getName());
            setAdjGenderComboBox.setSelectedItem(adj.getGender().toString());
            
            switch (adj.getType()) {
                case QUANTIFIER:
                    setQuantifierTypeComboBox.setSelectedItem(adj.getQuantifierType().toString());
                case ARTICLE:
                case DEMONSTRATIVE:
                case DETERMINER:
                case POSSESSIVE:
                    setPluralityComboBox.setSelectedItem(adj.getPlurality().toString());
                    break;                    
            }
        } else {
            selCatComboBox.setSelectedItem(null);
            setAdjGenderComboBox.setSelectedItem(null);
            setPluralityComboBox.setSelectedItem(null);
            setQuantifierTypeComboBox.setSelectedItem(null);
        }
        
        updateAdjectiveConflicts(adj);
        
        // Update
    }
    
    private void updateAdjectiveConflicts(Adjective adj) {
        if (adj == null) {
            adjNonConflictsList.setModel(new DefaultComboBoxModel<String>(new String[0]));
            adjConflictsList.setModel(new DefaultComboBoxModel<String>(new String[0]));
        } else {
            selectedAdjectiveNonConflicts = adj.getNonConflictsAsArray();
            selectedAdjectiveConflicts = adj.getConflictsAsArray();
            
            int nonConflictsLen = selectedAdjectiveNonConflicts.length;
            int conflictsLen = selectedAdjectiveConflicts.length;
            String[] nonConflicts = new String[nonConflictsLen];
            String[] conflicts = new String[conflictsLen];
            
            for (int i = 0; i < nonConflictsLen; i++)
                nonConflicts[i] = selectedAdjectiveNonConflicts[i].getLexeme().getLexeme();
            
            for (int i = 0; i < conflictsLen; i++)
                conflicts[i] = selectedAdjectiveConflicts[i].getLexeme().getLexeme();
            
            adjNonConflictsList.setModel(new DefaultComboBoxModel<String>(nonConflicts));
            adjConflictsList.setModel(new DefaultComboBoxModel<String>(conflicts));
        }
    }
    
    private void synchronizeToAdjectivalCategories() {
        Lexicon lexicon = WorkingDataManager.workingLexicon;
        AdjectivalCategoryManager adjCatManager = lexicon.getAdjCatManager();
        ArrayList<AdjectivalCategory> categories = adjCatManager.getCategoriesByPrecedence();
        // ArrayList<String> categoryNames = adjCatManager.getCategoryNames();
        
        if (categories != null) {
            for (AdjectivalCategory current : categories) {
                if (current != null) {
                    AdjectivalCategoryTable adjTab = new AdjectivalCategoryTable(current);
                    adjectivalCategoriesTabbedPane.addTab(current.getName(), adjTab);
                }
            }
        }
    }
    
    public void enableGenderRadioButtonsPerLanguage() {
        if (WorkingDataManager.workingLanguage != null) {
            Language currentLanguage = WorkingDataManager.currentWorkingDataManager.GetLanguage(WorkingDataManager.workingLanguage);
            Language.GENDER_DIVISION_SYSTEM_TYPE gds = currentLanguage.getGenderDivisionType();
            
            switch (gds) {
                case MASCULINE_FEMININE_NEUTER:
                    masculineRadioButton.setEnabled(true);
                    feminineRadioButton.setEnabled(true);
                    neuterRadioButton.setEnabled(true);
                    animateRadioButton.setEnabled(false);
                    inanimateRadioButton.setEnabled(false);
                    commonRadioButton.setEnabled(false);
                    break;
                case MASCULINE_FEMININE:
                    masculineRadioButton.setEnabled(true);
                    feminineRadioButton.setEnabled(true);
                    neuterRadioButton.setEnabled(false);
                    animateRadioButton.setEnabled(false);
                    inanimateRadioButton.setEnabled(false);
                    commonRadioButton.setEnabled(false);
                    break;
                case ANIMATE_INANIMATE:
                    masculineRadioButton.setEnabled(false);
                    feminineRadioButton.setEnabled(false);
                    neuterRadioButton.setEnabled(false);
                    animateRadioButton.setEnabled(true);
                    inanimateRadioButton.setEnabled(true);
                    commonRadioButton.setEnabled(false);
                    break;
                case COMMON_NEUTER:
                    masculineRadioButton.setEnabled(false);
                    feminineRadioButton.setEnabled(false);
                    neuterRadioButton.setEnabled(true);
                    animateRadioButton.setEnabled(false);
                    inanimateRadioButton.setEnabled(false);
                    commonRadioButton.setEnabled(true);
                    break;
                case NONE:
                    masculineRadioButton.setEnabled(false);
                    feminineRadioButton.setEnabled(false);
                    neuterRadioButton.setEnabled(false);
                    animateRadioButton.setEnabled(false);
                    inanimateRadioButton.setEnabled(false);
                    commonRadioButton.setEnabled(false);
                    break;
            }
        }
    }
    
    public void updateView() {
        lexiconViewLexemeListControl.setModel(new DefaultComboBoxModel<String>(WorkingDataManager.workingLexicon.allLexemesAsStrings()));
        nounLexemeComboBox.setModel(new DefaultComboBoxModel<String>(WorkingDataManager.workingLexicon.allLexemesAsStrings()));
        nounCountabilityComboBox.setModel(new DefaultComboBoxModel<String>(Noun.COUNTABILITY_TYPE.valuesAsStrings()));
        nounTypeComboBox.setModel(new DefaultComboBoxModel<String>(Noun.NOUN_TYPE.valuesAsStrings()));
        
        if (selectedLexeme == null) {
            lexemeNameLabel.setText("");
            lexemeIsAbstractCheckBox.setSelected(false);
            lexemeIsDynamicCheckBox.setSelected(false);
            lexemeEditButton.setEnabled(false);
        } else {
            lexemeNameLabel.setText(selectedLexeme.getLexeme());
            lexemeStringTextField.setText(selectedLexeme.getLexeme());
            lexemeIsAbstractCheckBox.setSelected(selectedLexeme.getAbstraction());
            lexemeIsDynamicCheckBox.setSelected(selectedLexeme.getDynamic());
            lexemeEditButton.setEnabled(true);
            nounLexemeComboBox.setSelectedItem(selectedLexeme.getLexeme());
        }
        
        enableGenderRadioButtonsPerLanguage();
        
        lexemeDimensionCancelButton.setEnabled(false);
        lexemeDimensionSaveButton.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        genderButtonGroup = new javax.swing.ButtonGroup();
        topPanel = new javax.swing.JPanel();
        workingLanguageLabel = new javax.swing.JLabel();
        workingLanguageComboBox = new javax.swing.JComboBox<>();
        workingLexiconLabel = new javax.swing.JLabel();
        workingLexiconComboBox = new javax.swing.JComboBox<>();
        bottomPanel = new javax.swing.JPanel();
        centerPanel = new javax.swing.JPanel();
        mainTabbedPane = new javax.swing.JTabbedPane();
        languageViewPanel = new javax.swing.JPanel();
        languageViewCenterPanel = new javax.swing.JPanel();
        languagePropertiesPanel1 = new javax.swing.JPanel();
        languagePropertiesPanel2 = new javax.swing.JPanel();
        languagePropertiesPanel3 = new javax.swing.JPanel();
        languagePropertiesPanel4 = new javax.swing.JPanel();
        languageViewTopPanel = new javax.swing.JPanel();
        languageViewTopPanelTitleLabel = new javax.swing.JLabel();
        lexiconViewPanel = new javax.swing.JPanel();
        lexiconViewTopPanel = new javax.swing.JPanel();
        lexiconViewTopPanelTitleLabel = new javax.swing.JLabel();
        languageViewInfoPanel = new javax.swing.JPanel();
        lexemeLabel = new javax.swing.JLabel();
        lexemeNameLabel = new javax.swing.JLabel();
        partOfSpeechLabel = new javax.swing.JLabel();
        lexiconViewLeftPanel = new javax.swing.JPanel();
        lexiconViewSearchAddDeletePanel = new javax.swing.JPanel();
        lexiconViewAddDeletePanel = new javax.swing.JPanel();
        viewPanelAddLexemeButton = new javax.swing.JButton();
        viewPanelRenameLexeme = new javax.swing.JButton();
        viewPanelDeleteLexemeButton = new javax.swing.JButton();
        repairSemanticNetworkButton = new javax.swing.JButton();
        repairAdjectivalCategoryManager = new javax.swing.JButton();
        lexiconViewSearchPanel = new javax.swing.JPanel();
        lexiconViewSearchTextField = new javax.swing.JTextField();
        lexiconViewSearchButton = new javax.swing.JButton();
        lexiconViewLexemeScrollPane = new javax.swing.JScrollPane();
        lexiconViewLexemeListControl = new javax.swing.JList<>();
        lexicalDimensionsPanel = new javax.swing.JTabbedPane();
        lexemeDimensionPanel = new javax.swing.JPanel();
        lexemeDimensionCenterPanel = new javax.swing.JPanel();
        lexemeDimensionTopPanel = new javax.swing.JPanel();
        lexemeStringLabel = new javax.swing.JLabel();
        lexemeStringTextField = new javax.swing.JTextField();
        lexemeIsAbstractCheckBox = new javax.swing.JCheckBox();
        lexemeIsDynamicCheckBox = new javax.swing.JCheckBox();
        lexemeEditButton = new javax.swing.JButton();
        lexemeDimensionBottomPanel = new javax.swing.JPanel();
        lexemeDimensionCancelButton = new javax.swing.JButton();
        lexemeDimensionSaveButton = new javax.swing.JButton();
        nounDimensionPanel = new javax.swing.JPanel();
        nounTabbedPanel = new javax.swing.JTabbedPane();
        nounGrammarPanel = new javax.swing.JPanel();
        nounSemanticNetworkPanel = new javax.swing.JPanel();
        lexiconSemanticNetworkScrollPane = new javax.swing.JScrollPane();
        lexiconSemanticNetworkMainPanel = new javax.swing.JPanel();
        lexiconSemanticNetworkScrollPanel = new javax.swing.JPanel();
        lexiconSemanticNetworkIsAPanel = new javax.swing.JPanel();
        thingsThatExistPanel = new javax.swing.JPanel();
        nounIsANounListScrollPane = new javax.swing.JScrollPane();
        thingsThatExistList = new javax.swing.JList<>();
        thingsThatExistLabel = new javax.swing.JLabel();
        thingsThatExistButtonPanel = new javax.swing.JPanel();
        removeThingsThatIAmButton = new javax.swing.JButton();
        addThingsThatIAmButton = new javax.swing.JButton();
        thingsThatIAmPanel = new javax.swing.JPanel();
        thingsThatIAmScrollPanel = new javax.swing.JScrollPane();
        thingsThatIAmList = new javax.swing.JList<>();
        thingsThatIAmLabel = new javax.swing.JLabel();
        thingsThatIAmByInferencePanel = new javax.swing.JPanel();
        thingsThatIAmThroughInferenceScrollPanel = new javax.swing.JScrollPane();
        thingsThatIAmThroughInferenceList = new javax.swing.JList<>();
        thingsThatIAmThroughInferenceLabel = new javax.swing.JLabel();
        lexiconSemanticNetworkHasPanel = new javax.swing.JPanel();
        lexiconSemanticNetworkIsAbleToPanel = new javax.swing.JPanel();
        lexiconSemanticNetworkIsModifiedByPanel = new javax.swing.JPanel();
        adjectiveToNounAssignmentPanel = new javax.swing.JPanel();
        assignAdjectivesButtonPanel = new javax.swing.JPanel();
        addAdjectives = new javax.swing.JButton();
        selectAdjectivesToAssignButton = new javax.swing.JButton();
        excludeAdjectivesButton = new javax.swing.JButton();
        adjectiveTablePanel = new javax.swing.JPanel();
        categoryToNounAssignmentPanel = new javax.swing.JPanel();
        categoriesThatExistPanel = new javax.swing.JPanel();
        categoriesScrollPane = new javax.swing.JScrollPane();
        categoriesList = new javax.swing.JList<>();
        adjectivalCategoriesIParktakeInLabel = new javax.swing.JLabel();
        categoriesIPartakeInButtonPanel = new javax.swing.JPanel();
        removeCategoriesIPartakeInButton = new javax.swing.JButton();
        addCategoriesIPartakeInButton = new javax.swing.JButton();
        categoriesIPartakeInPanel = new javax.swing.JPanel();
        categoriesIPartakeInLabel = new javax.swing.JLabel();
        categoriesIPartakeInScrollPane = new javax.swing.JScrollPane();
        categoriesIPartakeInList = new javax.swing.JList<>();
        nounAdjectivesPanel = new javax.swing.JPanel();
        nounAttributesPanel = new javax.swing.JPanel();
        nounAbilitiesPanel = new javax.swing.JPanel();
        nounBottomPanel = new javax.swing.JPanel();
        nounCancelEditButton = new javax.swing.JButton();
        nounSaveChangesButton = new javax.swing.JButton();
        nounTopPanel = new javax.swing.JPanel();
        nounTopTopPanel = new javax.swing.JPanel();
        nounLexemeLabel = new javax.swing.JLabel();
        nounLexemeComboBox = new javax.swing.JComboBox<>();
        nounCountabilityComboBox = new javax.swing.JComboBox<>();
        nounTypeComboBox = new javax.swing.JComboBox<>();
        nounPluralTextField = new javax.swing.JTextField();
        nounAlwaysPluralCheckBox = new javax.swing.JCheckBox();
        nounEditButton = new javax.swing.JButton();
        viewOntologyGeneralizeDiagramButton = new javax.swing.JButton();
        nounBottomTopPanel = new javax.swing.JPanel();
        masculineRadioButton = new javax.swing.JRadioButton();
        feminineRadioButton = new javax.swing.JRadioButton();
        neuterRadioButton = new javax.swing.JRadioButton();
        animateRadioButton = new javax.swing.JRadioButton();
        inanimateRadioButton = new javax.swing.JRadioButton();
        commonRadioButton = new javax.swing.JRadioButton();
        adjectiveDimensionMainPanel = new javax.swing.JPanel();
        adjectiveDimensionPanel = new javax.swing.JPanel();
        adjDimLeftPanel = new javax.swing.JPanel();
        adjDimBottomPanel = new javax.swing.JPanel();
        adjDefPanel = new javax.swing.JPanel();
        adjDefPromptPane = new javax.swing.JPanel();
        adjDefPrompt = new javax.swing.JLabel();
        adjDefScrollPane = new javax.swing.JScrollPane();
        adjDefTextPane = new javax.swing.JTextPane();
        adjCancelSavePanel = new javax.swing.JPanel();
        saveAdjectiveChangesButton = new javax.swing.JButton();
        adjCatPanel = new javax.swing.JPanel();
        adjCatCategoryPanel = new javax.swing.JPanel();
        selCatLabel = new javax.swing.JLabel();
        selCatComboBox = new javax.swing.JComboBox<>();
        adjCatGenderPanel = new javax.swing.JPanel();
        setAdjGenderLabel = new javax.swing.JLabel();
        setAdjGenderComboBox = new javax.swing.JComboBox<>();
        adjCatPluralityPanel = new javax.swing.JPanel();
        setPluralityLabel = new javax.swing.JLabel();
        setPluralityComboBox = new javax.swing.JComboBox<>();
        adjCatQuantifierTypePanel = new javax.swing.JPanel();
        setQuantifierTypeLabel = new javax.swing.JLabel();
        setQuantifierTypeComboBox = new javax.swing.JComboBox<>();
        adjButtonPanel = new javax.swing.JPanel();
        newAdjectiveButton = new javax.swing.JButton();
        deleteAdjectiveButton = new javax.swing.JButton();
        adjDimRightPanel1 = new javax.swing.JPanel();
        adjNonConflictsPanel = new javax.swing.JPanel();
        adjNonConflictsScrollPane = new javax.swing.JScrollPane();
        adjNonConflictsList = new javax.swing.JList<>();
        nonConflictsLabel = new javax.swing.JLabel();
        adjConflictTransferPanel = new javax.swing.JPanel();
        adjConflictsPanel = new javax.swing.JPanel();
        adjConflictsScrollPane = new javax.swing.JScrollPane();
        adjConflictsList = new javax.swing.JList<>();
        nonConflictsLabel1 = new javax.swing.JLabel();
        adjDimRightPanel2 = new javax.swing.JPanel();
        adjDimTopPanel = new javax.swing.JPanel();
        lexiconAdjectivalCategoriesPanel = new javax.swing.JPanel();
        adjectivalCategoriesPropertiesPanel = new javax.swing.JPanel();
        adjectivalCategoriesTabbedPane = new javax.swing.JTabbedPane();
        topMenuBar = new javax.swing.JMenuBar();
        fileDropDownMenu = new javax.swing.JMenu();
        saveWorkingDataMenuItem = new javax.swing.JMenuItem();
        fileExitMenuItem = new javax.swing.JMenuItem();
        linguisticDropDownMenu = new javax.swing.JMenu();
        linguisticAddLanguageMenuItem = new javax.swing.JMenuItem();
        linguisticAddLexiconMenuItem = new javax.swing.JMenuItem();
        assignAcademicLevelsMenuItem = new javax.swing.JMenuItem();
        exportCompiledExampleMenuItem = new javax.swing.JMenuItem();
        maintenanceDropDownMenu = new javax.swing.JMenu();
        openMaitenanceMenuItem = new javax.swing.JMenuItem();
        aboutDropDownMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Grammar Manager");
        setBackground(new java.awt.Color(102, 204, 255));
        setMinimumSize(new java.awt.Dimension(800, 269));
        setPreferredSize(new java.awt.Dimension(1500, 864));

        topPanel.setBackground(new java.awt.Color(204, 204, 255));
        topPanel.setName(""); // NOI18N

        workingLanguageLabel.setText("Working language:");

        workingLanguageComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        workingLanguageComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                workingLanguageComboBoxActionPerformed(evt);
            }
        });

        workingLexiconLabel.setText("Working lexicon:");

        workingLexiconComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        workingLexiconComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                workingLexiconComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap(1329, Short.MAX_VALUE)
                .addComponent(workingLanguageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(workingLanguageComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(workingLexiconLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(workingLexiconComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(workingLanguageLabel)
                    .addComponent(workingLanguageComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(workingLexiconLabel)
                    .addComponent(workingLexiconComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(topPanel, java.awt.BorderLayout.PAGE_START);

        bottomPanel.setBackground(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout bottomPanelLayout = new javax.swing.GroupLayout(bottomPanel);
        bottomPanel.setLayout(bottomPanelLayout);
        bottomPanelLayout.setHorizontalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1807, Short.MAX_VALUE)
        );
        bottomPanelLayout.setVerticalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );

        getContentPane().add(bottomPanel, java.awt.BorderLayout.PAGE_END);

        centerPanel.setBackground(new java.awt.Color(204, 204, 255));
        centerPanel.setLayout(new java.awt.BorderLayout());

        mainTabbedPane.setBackground(new java.awt.Color(255, 204, 204));

        languageViewPanel.setBackground(new java.awt.Color(255, 204, 204));
        languageViewPanel.setLayout(new java.awt.BorderLayout());

        languageViewCenterPanel.setLayout(new java.awt.GridLayout(1, 4, 10, 10));

        javax.swing.GroupLayout languagePropertiesPanel1Layout = new javax.swing.GroupLayout(languagePropertiesPanel1);
        languagePropertiesPanel1.setLayout(languagePropertiesPanel1Layout);
        languagePropertiesPanel1Layout.setHorizontalGroup(
            languagePropertiesPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 443, Short.MAX_VALUE)
        );
        languagePropertiesPanel1Layout.setVerticalGroup(
            languagePropertiesPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 455, Short.MAX_VALUE)
        );

        languageViewCenterPanel.add(languagePropertiesPanel1);

        javax.swing.GroupLayout languagePropertiesPanel2Layout = new javax.swing.GroupLayout(languagePropertiesPanel2);
        languagePropertiesPanel2.setLayout(languagePropertiesPanel2Layout);
        languagePropertiesPanel2Layout.setHorizontalGroup(
            languagePropertiesPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 443, Short.MAX_VALUE)
        );
        languagePropertiesPanel2Layout.setVerticalGroup(
            languagePropertiesPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 455, Short.MAX_VALUE)
        );

        languageViewCenterPanel.add(languagePropertiesPanel2);

        javax.swing.GroupLayout languagePropertiesPanel3Layout = new javax.swing.GroupLayout(languagePropertiesPanel3);
        languagePropertiesPanel3.setLayout(languagePropertiesPanel3Layout);
        languagePropertiesPanel3Layout.setHorizontalGroup(
            languagePropertiesPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 443, Short.MAX_VALUE)
        );
        languagePropertiesPanel3Layout.setVerticalGroup(
            languagePropertiesPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 455, Short.MAX_VALUE)
        );

        languageViewCenterPanel.add(languagePropertiesPanel3);

        languagePropertiesPanel4.setLayout(new java.awt.BorderLayout());
        languageViewCenterPanel.add(languagePropertiesPanel4);

        languageViewPanel.add(languageViewCenterPanel, java.awt.BorderLayout.CENTER);

        languageViewTopPanelTitleLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        languageViewTopPanelTitleLabel.setText("Language");
        languageViewTopPanel.add(languageViewTopPanelTitleLabel);

        languageViewPanel.add(languageViewTopPanel, java.awt.BorderLayout.PAGE_START);

        mainTabbedPane.addTab("Language View", languageViewPanel);

        lexiconViewPanel.setBackground(new java.awt.Color(153, 153, 255));
        lexiconViewPanel.setPreferredSize(new java.awt.Dimension(200, 749));
        lexiconViewPanel.setLayout(new java.awt.BorderLayout());

        lexiconViewTopPanel.setBackground(new java.awt.Color(153, 153, 255));

        lexiconViewTopPanelTitleLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lexiconViewTopPanelTitleLabel.setText("Lexicon");
        lexiconViewTopPanelTitleLabel.setToolTipText("");
        lexiconViewTopPanel.add(lexiconViewTopPanelTitleLabel);

        lexemeLabel.setText("/");
        languageViewInfoPanel.add(lexemeLabel);

        lexemeNameLabel.setText("lexeme");
        languageViewInfoPanel.add(lexemeNameLabel);

        partOfSpeechLabel.setText("/ part of speech");
        languageViewInfoPanel.add(partOfSpeechLabel);

        lexiconViewTopPanel.add(languageViewInfoPanel);

        lexiconViewPanel.add(lexiconViewTopPanel, java.awt.BorderLayout.PAGE_START);

        lexiconViewLeftPanel.setBackground(new java.awt.Color(153, 153, 255));
        lexiconViewLeftPanel.setPreferredSize(new java.awt.Dimension(220, 361));
        lexiconViewLeftPanel.setLayout(new java.awt.BorderLayout());

        lexiconViewSearchAddDeletePanel.setBackground(new java.awt.Color(153, 153, 255));
        lexiconViewSearchAddDeletePanel.setPreferredSize(new java.awt.Dimension(200, 120));
        lexiconViewSearchAddDeletePanel.setLayout(new java.awt.BorderLayout());

        lexiconViewAddDeletePanel.setBackground(new java.awt.Color(153, 153, 255));
        lexiconViewAddDeletePanel.setLayout(new java.awt.GridLayout(3, 3));

        viewPanelAddLexemeButton.setText("Add Lexeme");
        viewPanelAddLexemeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewPanelAddLexemeButtonActionPerformed(evt);
            }
        });
        lexiconViewAddDeletePanel.add(viewPanelAddLexemeButton);

        viewPanelRenameLexeme.setText("Rename");
        viewPanelRenameLexeme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewPanelRenameLexemeActionPerformed(evt);
            }
        });
        lexiconViewAddDeletePanel.add(viewPanelRenameLexeme);

        viewPanelDeleteLexemeButton.setText("Delete Lexeme");
        viewPanelDeleteLexemeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewPanelDeleteLexemeButtonActionPerformed(evt);
            }
        });
        lexiconViewAddDeletePanel.add(viewPanelDeleteLexemeButton);

        repairSemanticNetworkButton.setText("Repair SN");
        repairSemanticNetworkButton.setActionCommand("");
        repairSemanticNetworkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repairSemanticNetworkButtonActionPerformed(evt);
            }
        });
        lexiconViewAddDeletePanel.add(repairSemanticNetworkButton);

        repairAdjectivalCategoryManager.setText("Repair Adj Cat");
        repairAdjectivalCategoryManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repairAdjectivalCategoryManagerActionPerformed(evt);
            }
        });
        lexiconViewAddDeletePanel.add(repairAdjectivalCategoryManager);

        lexiconViewSearchAddDeletePanel.add(lexiconViewAddDeletePanel, java.awt.BorderLayout.CENTER);

        lexiconViewSearchPanel.setBackground(new java.awt.Color(153, 153, 255));
        lexiconViewSearchPanel.setPreferredSize(new java.awt.Dimension(180, 50));

        lexiconViewSearchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lexiconViewSearchTextFieldKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                lexiconViewSearchTextFieldKeyTyped(evt);
            }
        });

        lexiconViewSearchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/meetspeaklearn/grammarmanager/searchIconTiny.png"))); // NOI18N

        javax.swing.GroupLayout lexiconViewSearchPanelLayout = new javax.swing.GroupLayout(lexiconViewSearchPanel);
        lexiconViewSearchPanel.setLayout(lexiconViewSearchPanelLayout);
        lexiconViewSearchPanelLayout.setHorizontalGroup(
            lexiconViewSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lexiconViewSearchPanelLayout.createSequentialGroup()
                .addComponent(lexiconViewSearchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lexiconViewSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lexiconViewSearchPanelLayout.setVerticalGroup(
            lexiconViewSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lexiconViewSearchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lexiconViewSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lexiconViewSearchPanelLayout.createSequentialGroup()
                        .addComponent(lexiconViewSearchButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lexiconViewSearchTextField))
                .addContainerGap())
        );

        lexiconViewSearchAddDeletePanel.add(lexiconViewSearchPanel, java.awt.BorderLayout.NORTH);

        lexiconViewLeftPanel.add(lexiconViewSearchAddDeletePanel, java.awt.BorderLayout.NORTH);

        lexiconViewLexemeScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        lexiconViewLexemeListControl.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lexiconViewLexemeListControl.setPreferredSize(new java.awt.Dimension(200, 30000));
        lexiconViewLexemeListControl.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lexiconViewLexemeListControlValueChanged(evt);
            }
        });
        lexiconViewLexemeScrollPane.setViewportView(lexiconViewLexemeListControl);

        lexiconViewLeftPanel.add(lexiconViewLexemeScrollPane, java.awt.BorderLayout.CENTER);

        lexiconViewPanel.add(lexiconViewLeftPanel, java.awt.BorderLayout.WEST);

        lexicalDimensionsPanel.setBackground(new java.awt.Color(0, 204, 204));
        lexicalDimensionsPanel.setName(""); // NOI18N
        lexicalDimensionsPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                lexicalDimensionsPanelStateChanged(evt);
            }
        });

        lexemeDimensionPanel.setBackground(new java.awt.Color(204, 204, 255));
        lexemeDimensionPanel.setName("lexeme"); // NOI18N
        lexemeDimensionPanel.setLayout(new java.awt.BorderLayout());

        lexemeDimensionCenterPanel.setBackground(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout lexemeDimensionCenterPanelLayout = new javax.swing.GroupLayout(lexemeDimensionCenterPanel);
        lexemeDimensionCenterPanel.setLayout(lexemeDimensionCenterPanelLayout);
        lexemeDimensionCenterPanelLayout.setHorizontalGroup(
            lexemeDimensionCenterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1577, Short.MAX_VALUE)
        );
        lexemeDimensionCenterPanelLayout.setVerticalGroup(
            lexemeDimensionCenterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 353, Short.MAX_VALUE)
        );

        lexemeDimensionPanel.add(lexemeDimensionCenterPanel, java.awt.BorderLayout.CENTER);

        lexemeDimensionTopPanel.setBackground(new java.awt.Color(204, 204, 255));

        lexemeStringLabel.setText("String:");
        lexemeDimensionTopPanel.add(lexemeStringLabel);

        lexemeStringTextField.setBackground(new java.awt.Color(204, 204, 255));
        lexemeStringTextField.setText("jTextField1");
        lexemeStringTextField.setEnabled(false);
        lexemeStringTextField.setMinimumSize(new java.awt.Dimension(200, 24));
        lexemeStringTextField.setPreferredSize(new java.awt.Dimension(300, 24));
        lexemeDimensionTopPanel.add(lexemeStringTextField);

        lexemeIsAbstractCheckBox.setBackground(new java.awt.Color(204, 204, 255));
        lexemeIsAbstractCheckBox.setText("Abstract?");
        lexemeIsAbstractCheckBox.setEnabled(false);
        lexemeDimensionTopPanel.add(lexemeIsAbstractCheckBox);

        lexemeIsDynamicCheckBox.setBackground(new java.awt.Color(204, 204, 255));
        lexemeIsDynamicCheckBox.setText("Dynamic?");
        lexemeIsDynamicCheckBox.setEnabled(false);
        lexemeDimensionTopPanel.add(lexemeIsDynamicCheckBox);

        lexemeEditButton.setText("Edit");
        lexemeEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lexemeEditButtonActionPerformed(evt);
            }
        });
        lexemeDimensionTopPanel.add(lexemeEditButton);

        lexemeDimensionPanel.add(lexemeDimensionTopPanel, java.awt.BorderLayout.PAGE_START);

        lexemeDimensionBottomPanel.setBackground(new java.awt.Color(204, 204, 255));

        lexemeDimensionCancelButton.setText("Cancel Edit");
        lexemeDimensionCancelButton.setEnabled(false);
        lexemeDimensionCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lexemeDimensionCancelButtonActionPerformed(evt);
            }
        });
        lexemeDimensionBottomPanel.add(lexemeDimensionCancelButton);

        lexemeDimensionSaveButton.setText("Save Changes");
        lexemeDimensionSaveButton.setEnabled(false);
        lexemeDimensionSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lexemeDimensionSaveButtonActionPerformed(evt);
            }
        });
        lexemeDimensionBottomPanel.add(lexemeDimensionSaveButton);

        lexemeDimensionPanel.add(lexemeDimensionBottomPanel, java.awt.BorderLayout.PAGE_END);

        lexicalDimensionsPanel.addTab("Lexeme", lexemeDimensionPanel);

        nounDimensionPanel.setBackground(new java.awt.Color(0, 204, 204));
        nounDimensionPanel.setName("noun"); // NOI18N
        nounDimensionPanel.setLayout(new java.awt.BorderLayout());

        nounTabbedPanel.setBackground(new java.awt.Color(0, 204, 204));
        nounTabbedPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                nounTabbedPanelStateChanged(evt);
            }
        });

        nounGrammarPanel.setBackground(new java.awt.Color(0, 204, 204));

        javax.swing.GroupLayout nounGrammarPanelLayout = new javax.swing.GroupLayout(nounGrammarPanel);
        nounGrammarPanel.setLayout(nounGrammarPanelLayout);
        nounGrammarPanelLayout.setHorizontalGroup(
            nounGrammarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1572, Short.MAX_VALUE)
        );
        nounGrammarPanelLayout.setVerticalGroup(
            nounGrammarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 291, Short.MAX_VALUE)
        );

        nounTabbedPanel.addTab("Grammatical Properties", nounGrammarPanel);

        nounSemanticNetworkPanel.setBackground(new java.awt.Color(0, 204, 204));
        nounSemanticNetworkPanel.setLayout(new java.awt.GridLayout(1, 2, 10, 10));

        lexiconSemanticNetworkMainPanel.setBackground(new java.awt.Color(0, 204, 204));
        lexiconSemanticNetworkMainPanel.setPreferredSize(new java.awt.Dimension(600, 1200));
        lexiconSemanticNetworkMainPanel.setLayout(new java.awt.GridLayout(1, 2, 10, 10));

        lexiconSemanticNetworkScrollPanel.setBackground(new java.awt.Color(0, 204, 204));
        lexiconSemanticNetworkScrollPanel.setPreferredSize(new java.awt.Dimension(700, 2000));
        lexiconSemanticNetworkScrollPanel.setLayout(new java.awt.GridLayout(4, 1, 10, 10));

        thingsThatExistPanel.setLayout(new java.awt.BorderLayout());

        thingsThatExistList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        thingsThatExistList.setMaximumSize(new java.awt.Dimension(200, 10000));
        thingsThatExistList.setMinimumSize(new java.awt.Dimension(200, 300));
        thingsThatExistList.setPreferredSize(new java.awt.Dimension(200, 10000));
        thingsThatExistList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                thingsThatExistListValueChanged(evt);
            }
        });
        nounIsANounListScrollPane.setViewportView(thingsThatExistList);

        thingsThatExistPanel.add(nounIsANounListScrollPane, java.awt.BorderLayout.CENTER);

        thingsThatExistLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        thingsThatExistLabel.setText("Things that exist that I am not");
        thingsThatExistPanel.add(thingsThatExistLabel, java.awt.BorderLayout.PAGE_START);

        lexiconSemanticNetworkIsAPanel.add(thingsThatExistPanel);

        thingsThatExistButtonPanel.setLayout(new java.awt.BorderLayout());

        removeThingsThatIAmButton.setText("<- Remove");
        removeThingsThatIAmButton.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        removeThingsThatIAmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeThingsThatIAmButtonActionPerformed(evt);
            }
        });
        thingsThatExistButtonPanel.add(removeThingsThatIAmButton, java.awt.BorderLayout.CENTER);

        addThingsThatIAmButton.setText("Add ->");
        addThingsThatIAmButton.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        addThingsThatIAmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addThingsThatIAmButtonActionPerformed(evt);
            }
        });
        thingsThatExistButtonPanel.add(addThingsThatIAmButton, java.awt.BorderLayout.PAGE_START);

        lexiconSemanticNetworkIsAPanel.add(thingsThatExistButtonPanel);

        thingsThatIAmPanel.setLayout(new java.awt.BorderLayout());

        thingsThatIAmList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        thingsThatIAmList.setMaximumSize(new java.awt.Dimension(200, 300));
        thingsThatIAmList.setMinimumSize(new java.awt.Dimension(200, 300));
        thingsThatIAmList.setPreferredSize(new java.awt.Dimension(200, 10000));
        thingsThatIAmList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                thingsThatIAmListValueChanged(evt);
            }
        });
        thingsThatIAmScrollPanel.setViewportView(thingsThatIAmList);

        thingsThatIAmPanel.add(thingsThatIAmScrollPanel, java.awt.BorderLayout.CENTER);

        thingsThatIAmLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        thingsThatIAmLabel.setText("Things that I am");
        thingsThatIAmPanel.add(thingsThatIAmLabel, java.awt.BorderLayout.PAGE_START);

        lexiconSemanticNetworkIsAPanel.add(thingsThatIAmPanel);

        thingsThatIAmByInferencePanel.setLayout(new java.awt.BorderLayout());

        thingsThatIAmThroughInferenceList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        thingsThatIAmThroughInferenceList.setMaximumSize(new java.awt.Dimension(200, 300));
        thingsThatIAmThroughInferenceList.setMinimumSize(new java.awt.Dimension(200, 300));
        thingsThatIAmThroughInferenceList.setPreferredSize(new java.awt.Dimension(200, 10000));
        thingsThatIAmThroughInferenceList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                thingsThatIAmThroughInferenceListValueChanged(evt);
            }
        });
        thingsThatIAmThroughInferenceScrollPanel.setViewportView(thingsThatIAmThroughInferenceList);

        thingsThatIAmByInferencePanel.add(thingsThatIAmThroughInferenceScrollPanel, java.awt.BorderLayout.CENTER);

        thingsThatIAmThroughInferenceLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        thingsThatIAmThroughInferenceLabel.setText("Things that I am through inference");
        thingsThatIAmByInferencePanel.add(thingsThatIAmThroughInferenceLabel, java.awt.BorderLayout.PAGE_START);

        lexiconSemanticNetworkIsAPanel.add(thingsThatIAmByInferencePanel);

        lexiconSemanticNetworkScrollPanel.add(lexiconSemanticNetworkIsAPanel);

        javax.swing.GroupLayout lexiconSemanticNetworkHasPanelLayout = new javax.swing.GroupLayout(lexiconSemanticNetworkHasPanel);
        lexiconSemanticNetworkHasPanel.setLayout(lexiconSemanticNetworkHasPanelLayout);
        lexiconSemanticNetworkHasPanelLayout.setHorizontalGroup(
            lexiconSemanticNetworkHasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1553, Short.MAX_VALUE)
        );
        lexiconSemanticNetworkHasPanelLayout.setVerticalGroup(
            lexiconSemanticNetworkHasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 292, Short.MAX_VALUE)
        );

        lexiconSemanticNetworkScrollPanel.add(lexiconSemanticNetworkHasPanel);

        javax.swing.GroupLayout lexiconSemanticNetworkIsAbleToPanelLayout = new javax.swing.GroupLayout(lexiconSemanticNetworkIsAbleToPanel);
        lexiconSemanticNetworkIsAbleToPanel.setLayout(lexiconSemanticNetworkIsAbleToPanelLayout);
        lexiconSemanticNetworkIsAbleToPanelLayout.setHorizontalGroup(
            lexiconSemanticNetworkIsAbleToPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1553, Short.MAX_VALUE)
        );
        lexiconSemanticNetworkIsAbleToPanelLayout.setVerticalGroup(
            lexiconSemanticNetworkIsAbleToPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 292, Short.MAX_VALUE)
        );

        lexiconSemanticNetworkScrollPanel.add(lexiconSemanticNetworkIsAbleToPanel);

        lexiconSemanticNetworkIsModifiedByPanel.setLayout(new java.awt.GridLayout(1, 2, 10, 0));

        adjectiveToNounAssignmentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Assign Adjectives to Noun"));
        adjectiveToNounAssignmentPanel.setPreferredSize(new java.awt.Dimension(150, 400));
        adjectiveToNounAssignmentPanel.setLayout(new java.awt.BorderLayout(4, 0));

        assignAdjectivesButtonPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Adjective Filters"));
        assignAdjectivesButtonPanel.setPreferredSize(new java.awt.Dimension(120, 169));
        assignAdjectivesButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 16));

        addAdjectives.setText("Add");
        addAdjectives.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAdjectivesActionPerformed(evt);
            }
        });
        assignAdjectivesButtonPanel.add(addAdjectives);

        selectAdjectivesToAssignButton.setText("Replace");
        selectAdjectivesToAssignButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAdjectivesToAssignButtonActionPerformed(evt);
            }
        });
        assignAdjectivesButtonPanel.add(selectAdjectivesToAssignButton);

        excludeAdjectivesButton.setText("Exclude");
        excludeAdjectivesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excludeAdjectivesButtonActionPerformed(evt);
            }
        });
        assignAdjectivesButtonPanel.add(excludeAdjectivesButton);

        adjectiveToNounAssignmentPanel.add(assignAdjectivesButtonPanel, java.awt.BorderLayout.WEST);

        adjectiveTablePanel.setLayout(new java.awt.BorderLayout());
        adjectiveToNounAssignmentPanel.add(adjectiveTablePanel, java.awt.BorderLayout.CENTER);

        lexiconSemanticNetworkIsModifiedByPanel.add(adjectiveToNounAssignmentPanel);

        categoryToNounAssignmentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Assign Categories to Noun"));
        categoryToNounAssignmentPanel.setMaximumSize(new java.awt.Dimension(32767, 400));
        categoryToNounAssignmentPanel.setPreferredSize(new java.awt.Dimension(519, 400));

        categoriesThatExistPanel.setMaximumSize(new java.awt.Dimension(200, 400));
        categoriesThatExistPanel.setPreferredSize(new java.awt.Dimension(200, 400));
        categoriesThatExistPanel.setLayout(new java.awt.BorderLayout());

        categoriesScrollPane.setMaximumSize(new java.awt.Dimension(200, 250));
        categoriesScrollPane.setPreferredSize(new java.awt.Dimension(200, 250));

        categoriesList.setPreferredSize(new java.awt.Dimension(180, 800));
        categoriesScrollPane.setViewportView(categoriesList);

        categoriesThatExistPanel.add(categoriesScrollPane, java.awt.BorderLayout.CENTER);

        adjectivalCategoriesIParktakeInLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        adjectivalCategoriesIParktakeInLabel.setText("Adjectival Categories");
        categoriesThatExistPanel.add(adjectivalCategoriesIParktakeInLabel, java.awt.BorderLayout.PAGE_START);

        categoryToNounAssignmentPanel.add(categoriesThatExistPanel);

        categoriesIPartakeInButtonPanel.setMaximumSize(new java.awt.Dimension(200, 400));
        categoriesIPartakeInButtonPanel.setLayout(new java.awt.BorderLayout());

        removeCategoriesIPartakeInButton.setText("<- Remove");
        removeCategoriesIPartakeInButton.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        removeCategoriesIPartakeInButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeCategoriesIPartakeInButtonActionPerformed(evt);
            }
        });
        categoriesIPartakeInButtonPanel.add(removeCategoriesIPartakeInButton, java.awt.BorderLayout.CENTER);

        addCategoriesIPartakeInButton.setText("Add ->");
        addCategoriesIPartakeInButton.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        addCategoriesIPartakeInButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCategoriesIPartakeInButtonActionPerformed(evt);
            }
        });
        categoriesIPartakeInButtonPanel.add(addCategoriesIPartakeInButton, java.awt.BorderLayout.PAGE_START);

        categoryToNounAssignmentPanel.add(categoriesIPartakeInButtonPanel);

        categoriesIPartakeInPanel.setMaximumSize(new java.awt.Dimension(200, 400));
        categoriesIPartakeInPanel.setPreferredSize(new java.awt.Dimension(200, 400));
        categoriesIPartakeInPanel.setLayout(new java.awt.BorderLayout());

        categoriesIPartakeInLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        categoriesIPartakeInLabel.setText("Applicable Adjectival Categories");
        categoriesIPartakeInPanel.add(categoriesIPartakeInLabel, java.awt.BorderLayout.PAGE_START);

        categoriesIPartakeInScrollPane.setMaximumSize(new java.awt.Dimension(200, 250));
        categoriesIPartakeInScrollPane.setPreferredSize(new java.awt.Dimension(200, 250));

        categoriesIPartakeInList.setMaximumSize(new java.awt.Dimension(180, 300));
        categoriesIPartakeInList.setMinimumSize(new java.awt.Dimension(180, 250));
        categoriesIPartakeInList.setPreferredSize(new java.awt.Dimension(180, 800));
        categoriesIPartakeInList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                categoriesIPartakeInListValueChanged(evt);
            }
        });
        categoriesIPartakeInScrollPane.setViewportView(categoriesIPartakeInList);

        categoriesIPartakeInPanel.add(categoriesIPartakeInScrollPane, java.awt.BorderLayout.LINE_END);

        categoryToNounAssignmentPanel.add(categoriesIPartakeInPanel);

        lexiconSemanticNetworkIsModifiedByPanel.add(categoryToNounAssignmentPanel);

        lexiconSemanticNetworkScrollPanel.add(lexiconSemanticNetworkIsModifiedByPanel);

        lexiconSemanticNetworkMainPanel.add(lexiconSemanticNetworkScrollPanel);

        lexiconSemanticNetworkScrollPane.setViewportView(lexiconSemanticNetworkMainPanel);

        nounSemanticNetworkPanel.add(lexiconSemanticNetworkScrollPane);

        nounTabbedPanel.addTab("Semantic Network", nounSemanticNetworkPanel);

        nounAdjectivesPanel.setBackground(new java.awt.Color(0, 204, 204));

        javax.swing.GroupLayout nounAdjectivesPanelLayout = new javax.swing.GroupLayout(nounAdjectivesPanel);
        nounAdjectivesPanel.setLayout(nounAdjectivesPanelLayout);
        nounAdjectivesPanelLayout.setHorizontalGroup(
            nounAdjectivesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1572, Short.MAX_VALUE)
        );
        nounAdjectivesPanelLayout.setVerticalGroup(
            nounAdjectivesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 291, Short.MAX_VALUE)
        );

        nounTabbedPanel.addTab("Adjectives", nounAdjectivesPanel);

        nounAttributesPanel.setBackground(new java.awt.Color(0, 204, 204));

        javax.swing.GroupLayout nounAttributesPanelLayout = new javax.swing.GroupLayout(nounAttributesPanel);
        nounAttributesPanel.setLayout(nounAttributesPanelLayout);
        nounAttributesPanelLayout.setHorizontalGroup(
            nounAttributesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1572, Short.MAX_VALUE)
        );
        nounAttributesPanelLayout.setVerticalGroup(
            nounAttributesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 291, Short.MAX_VALUE)
        );

        nounTabbedPanel.addTab("Attributes", nounAttributesPanel);

        nounAbilitiesPanel.setBackground(new java.awt.Color(0, 204, 204));
        nounAbilitiesPanel.setLayout(new java.awt.GridLayout(1, 2, 10, 10));
        nounTabbedPanel.addTab("Abilities", nounAbilitiesPanel);

        nounDimensionPanel.add(nounTabbedPanel, java.awt.BorderLayout.CENTER);

        nounBottomPanel.setBackground(new java.awt.Color(0, 204, 204));

        nounCancelEditButton.setText("Cancel Edit");
        nounCancelEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nounCancelEditButtonActionPerformed(evt);
            }
        });
        nounBottomPanel.add(nounCancelEditButton);

        nounSaveChangesButton.setText("Save Changes");
        nounSaveChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nounSaveChangesButtonActionPerformed(evt);
            }
        });
        nounBottomPanel.add(nounSaveChangesButton);

        nounDimensionPanel.add(nounBottomPanel, java.awt.BorderLayout.SOUTH);

        nounTopPanel.setBackground(new java.awt.Color(0, 204, 204));
        nounTopPanel.setLayout(new java.awt.GridLayout(2, 1));

        nounTopTopPanel.setBackground(new java.awt.Color(0, 204, 204));

        nounLexemeLabel.setText("Lexeme:");
        nounLexemeLabel.setPreferredSize(new java.awt.Dimension(41, 24));
        nounTopTopPanel.add(nounLexemeLabel);

        nounLexemeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        nounLexemeComboBox.setPreferredSize(new java.awt.Dimension(200, 24));
        nounLexemeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nounLexemeComboBoxActionPerformed(evt);
            }
        });
        nounTopTopPanel.add(nounLexemeComboBox);

        nounCountabilityComboBox.setPreferredSize(new java.awt.Dimension(120, 24));
        nounCountabilityComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nounCountabilityComboBoxActionPerformed(evt);
            }
        });
        nounTopTopPanel.add(nounCountabilityComboBox);

        nounTypeComboBox.setPreferredSize(new java.awt.Dimension(100, 24));
        nounTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nounTypeComboBoxActionPerformed(evt);
            }
        });
        nounTopTopPanel.add(nounTypeComboBox);

        nounPluralTextField.setPreferredSize(new java.awt.Dimension(200, 24));
        nounTopTopPanel.add(nounPluralTextField);

        nounAlwaysPluralCheckBox.setBackground(new java.awt.Color(0, 204, 204));
        nounAlwaysPluralCheckBox.setText("Always plural?");
        nounAlwaysPluralCheckBox.setMinimumSize(new java.awt.Dimension(80, 23));
        nounAlwaysPluralCheckBox.setPreferredSize(new java.awt.Dimension(95, 24));
        nounAlwaysPluralCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nounAlwaysPluralCheckBoxActionPerformed(evt);
            }
        });
        nounTopTopPanel.add(nounAlwaysPluralCheckBox);

        nounEditButton.setText("Add or Edit");
        nounEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nounEditButtonActionPerformed(evt);
            }
        });
        nounTopTopPanel.add(nounEditButton);

        viewOntologyGeneralizeDiagramButton.setText("Generalization View");
        viewOntologyGeneralizeDiagramButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewOntologyGeneralizeDiagramButtonActionPerformed(evt);
            }
        });
        nounTopTopPanel.add(viewOntologyGeneralizeDiagramButton);

        nounTopPanel.add(nounTopTopPanel);

        nounBottomTopPanel.setBackground(new java.awt.Color(0, 204, 204));

        masculineRadioButton.setBackground(new java.awt.Color(0, 204, 204));
        genderButtonGroup.add(masculineRadioButton);
        masculineRadioButton.setText("Masculine");
        masculineRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masculineRadioButtonActionPerformed(evt);
            }
        });
        nounBottomTopPanel.add(masculineRadioButton);

        feminineRadioButton.setBackground(new java.awt.Color(0, 204, 204));
        genderButtonGroup.add(feminineRadioButton);
        feminineRadioButton.setText("Feminine");
        feminineRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feminineRadioButtonActionPerformed(evt);
            }
        });
        nounBottomTopPanel.add(feminineRadioButton);

        neuterRadioButton.setBackground(new java.awt.Color(0, 204, 204));
        genderButtonGroup.add(neuterRadioButton);
        neuterRadioButton.setText("Neuter");
        neuterRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                neuterRadioButtonActionPerformed(evt);
            }
        });
        nounBottomTopPanel.add(neuterRadioButton);

        animateRadioButton.setBackground(new java.awt.Color(0, 204, 204));
        genderButtonGroup.add(animateRadioButton);
        animateRadioButton.setText("Animate");
        animateRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                animateRadioButtonActionPerformed(evt);
            }
        });
        nounBottomTopPanel.add(animateRadioButton);

        inanimateRadioButton.setBackground(new java.awt.Color(0, 204, 204));
        genderButtonGroup.add(inanimateRadioButton);
        inanimateRadioButton.setText("Inanimate");
        inanimateRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inanimateRadioButtonActionPerformed(evt);
            }
        });
        nounBottomTopPanel.add(inanimateRadioButton);

        commonRadioButton.setBackground(new java.awt.Color(0, 204, 204));
        genderButtonGroup.add(commonRadioButton);
        commonRadioButton.setText("Common");
        commonRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commonRadioButtonActionPerformed(evt);
            }
        });
        nounBottomTopPanel.add(commonRadioButton);

        nounTopPanel.add(nounBottomTopPanel);

        nounDimensionPanel.add(nounTopPanel, java.awt.BorderLayout.NORTH);

        lexicalDimensionsPanel.addTab("Noun", nounDimensionPanel);

        adjectiveDimensionMainPanel.setName("adjective"); // NOI18N
        adjectiveDimensionMainPanel.setLayout(new java.awt.BorderLayout());

        adjectiveDimensionPanel.setName("adjective"); // NOI18N
        adjectiveDimensionPanel.setLayout(new java.awt.BorderLayout());

        adjDimLeftPanel.setMaximumSize(new java.awt.Dimension(377, 2147483647));
        adjDimLeftPanel.setPreferredSize(new java.awt.Dimension(500, 233));
        adjDimLeftPanel.setLayout(new java.awt.BorderLayout());

        adjDimBottomPanel.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        adjDimBottomPanel.setMinimumSize(new java.awt.Dimension(300, 110));
        adjDimBottomPanel.setPreferredSize(new java.awt.Dimension(300, 340));
        adjDimBottomPanel.setLayout(new java.awt.BorderLayout());

        adjDefPanel.setLayout(new java.awt.BorderLayout());

        adjDefPrompt.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        adjDefPrompt.setText("Add a definition (optional):");
        adjDefPromptPane.add(adjDefPrompt);

        adjDefPanel.add(adjDefPromptPane, java.awt.BorderLayout.NORTH);

        adjDefScrollPane.setViewportView(adjDefTextPane);

        adjDefPanel.add(adjDefScrollPane, java.awt.BorderLayout.CENTER);

        adjDimBottomPanel.add(adjDefPanel, java.awt.BorderLayout.CENTER);

        saveAdjectiveChangesButton.setText("Save Changes");
        saveAdjectiveChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAdjectiveChangesButtonActionPerformed(evt);
            }
        });
        adjCancelSavePanel.add(saveAdjectiveChangesButton);

        adjDimBottomPanel.add(adjCancelSavePanel, java.awt.BorderLayout.SOUTH);

        adjCatPanel.setLayout(new java.awt.GridLayout(2, 2, 4, 4));

        selCatLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        selCatLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        selCatLabel.setText("Set the category:");
        selCatLabel.setPreferredSize(new java.awt.Dimension(100, 20));
        adjCatCategoryPanel.add(selCatLabel);

        selCatComboBox.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        selCatComboBox.setPreferredSize(new java.awt.Dimension(120, 20));
        selCatComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selCatComboBoxActionPerformed(evt);
            }
        });
        adjCatCategoryPanel.add(selCatComboBox);

        adjCatPanel.add(adjCatCategoryPanel);

        setAdjGenderLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        setAdjGenderLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        setAdjGenderLabel.setText("Set the gender:");
        setAdjGenderLabel.setPreferredSize(new java.awt.Dimension(100, 20));
        adjCatGenderPanel.add(setAdjGenderLabel);

        setAdjGenderComboBox.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        setAdjGenderComboBox.setPreferredSize(new java.awt.Dimension(120, 20));
        setAdjGenderComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setAdjGenderComboBoxActionPerformed(evt);
            }
        });
        adjCatGenderPanel.add(setAdjGenderComboBox);

        adjCatPanel.add(adjCatGenderPanel);

        setPluralityLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        setPluralityLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        setPluralityLabel.setText("Plurality:");
        setPluralityLabel.setPreferredSize(new java.awt.Dimension(100, 20));
        adjCatPluralityPanel.add(setPluralityLabel);

        setPluralityComboBox.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        setPluralityComboBox.setPreferredSize(new java.awt.Dimension(120, 20));
        setPluralityComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setPluralityComboBoxActionPerformed(evt);
            }
        });
        adjCatPluralityPanel.add(setPluralityComboBox);

        adjCatPanel.add(adjCatPluralityPanel);

        setQuantifierTypeLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        setQuantifierTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        setQuantifierTypeLabel.setText("Quantifier type:");
        setQuantifierTypeLabel.setPreferredSize(new java.awt.Dimension(100, 20));
        adjCatQuantifierTypePanel.add(setQuantifierTypeLabel);

        setQuantifierTypeComboBox.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        setQuantifierTypeComboBox.setPreferredSize(new java.awt.Dimension(120, 20));
        setQuantifierTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setQuantifierTypeComboBoxActionPerformed(evt);
            }
        });
        adjCatQuantifierTypePanel.add(setQuantifierTypeComboBox);

        adjCatPanel.add(adjCatQuantifierTypePanel);

        adjDimBottomPanel.add(adjCatPanel, java.awt.BorderLayout.PAGE_START);

        adjDimLeftPanel.add(adjDimBottomPanel, java.awt.BorderLayout.SOUTH);

        newAdjectiveButton.setText("New Adjective");
        newAdjectiveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newAdjectiveButtonActionPerformed(evt);
            }
        });
        adjButtonPanel.add(newAdjectiveButton);

        deleteAdjectiveButton.setText("Delete Adjective");
        deleteAdjectiveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAdjectiveButtonActionPerformed(evt);
            }
        });
        adjButtonPanel.add(deleteAdjectiveButton);

        adjDimLeftPanel.add(adjButtonPanel, java.awt.BorderLayout.NORTH);

        adjectiveDimensionPanel.add(adjDimLeftPanel, java.awt.BorderLayout.WEST);

        adjDimRightPanel1.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        adjDimRightPanel1.setMinimumSize(new java.awt.Dimension(300, 76));
        adjDimRightPanel1.setPreferredSize(new java.awt.Dimension(300, 700));
        adjDimRightPanel1.setLayout(new java.awt.BorderLayout());

        adjNonConflictsPanel.setLayout(new java.awt.BorderLayout());

        adjNonConflictsList.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        adjNonConflictsScrollPane.setViewportView(adjNonConflictsList);

        adjNonConflictsPanel.add(adjNonConflictsScrollPane, java.awt.BorderLayout.CENTER);

        nonConflictsLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        nonConflictsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nonConflictsLabel.setText("non-antonyms");
        adjNonConflictsPanel.add(nonConflictsLabel, java.awt.BorderLayout.NORTH);

        adjDimRightPanel1.add(adjNonConflictsPanel, java.awt.BorderLayout.WEST);

        adjConflictTransferPanel.setMaximumSize(new java.awt.Dimension(80, 32767));
        adjConflictTransferPanel.setMinimumSize(new java.awt.Dimension(80, 0));
        adjConflictTransferPanel.setPreferredSize(new java.awt.Dimension(80, 410));

        javax.swing.GroupLayout adjConflictTransferPanelLayout = new javax.swing.GroupLayout(adjConflictTransferPanel);
        adjConflictTransferPanel.setLayout(adjConflictTransferPanelLayout);
        adjConflictTransferPanelLayout.setHorizontalGroup(
            adjConflictTransferPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 261, Short.MAX_VALUE)
        );
        adjConflictTransferPanelLayout.setVerticalGroup(
            adjConflictTransferPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        adjDimRightPanel1.add(adjConflictTransferPanel, java.awt.BorderLayout.CENTER);

        adjConflictsPanel.setLayout(new java.awt.BorderLayout());

        adjConflictsList.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        adjConflictsScrollPane.setViewportView(adjConflictsList);

        adjConflictsPanel.add(adjConflictsScrollPane, java.awt.BorderLayout.CENTER);

        nonConflictsLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        nonConflictsLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nonConflictsLabel1.setText("antonyms");
        adjConflictsPanel.add(nonConflictsLabel1, java.awt.BorderLayout.NORTH);

        adjDimRightPanel1.add(adjConflictsPanel, java.awt.BorderLayout.EAST);

        adjectiveDimensionPanel.add(adjDimRightPanel1, java.awt.BorderLayout.CENTER);

        adjDimRightPanel2.setMaximumSize(new java.awt.Dimension(300, 32767));
        adjDimRightPanel2.setPreferredSize(new java.awt.Dimension(300, 410));

        javax.swing.GroupLayout adjDimRightPanel2Layout = new javax.swing.GroupLayout(adjDimRightPanel2);
        adjDimRightPanel2.setLayout(adjDimRightPanel2Layout);
        adjDimRightPanel2Layout.setHorizontalGroup(
            adjDimRightPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        adjDimRightPanel2Layout.setVerticalGroup(
            adjDimRightPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 410, Short.MAX_VALUE)
        );

        adjectiveDimensionPanel.add(adjDimRightPanel2, java.awt.BorderLayout.EAST);

        adjectiveDimensionMainPanel.add(adjectiveDimensionPanel, java.awt.BorderLayout.CENTER);

        adjDimTopPanel.setPreferredSize(new java.awt.Dimension(300, 10));
        adjectiveDimensionMainPanel.add(adjDimTopPanel, java.awt.BorderLayout.PAGE_START);

        lexicalDimensionsPanel.addTab("Adjective", adjectiveDimensionMainPanel);

        lexiconViewPanel.add(lexicalDimensionsPanel, java.awt.BorderLayout.CENTER);

        mainTabbedPane.addTab("Lexicon View", lexiconViewPanel);

        lexiconAdjectivalCategoriesPanel.setBackground(new java.awt.Color(204, 204, 255));
        lexiconAdjectivalCategoriesPanel.setLayout(new java.awt.GridLayout(1, 2, 6, 0));

        adjectivalCategoriesPropertiesPanel.setBackground(new java.awt.Color(204, 204, 255));
        adjectivalCategoriesPropertiesPanel.setLayout(new java.awt.BorderLayout());
        lexiconAdjectivalCategoriesPanel.add(adjectivalCategoriesPropertiesPanel);

        adjectivalCategoriesTabbedPane.setBackground(new java.awt.Color(204, 204, 255));
        lexiconAdjectivalCategoriesPanel.add(adjectivalCategoriesTabbedPane);

        mainTabbedPane.addTab("Lexicon Adjectival Categories View", lexiconAdjectivalCategoriesPanel);

        centerPanel.add(mainTabbedPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

        fileDropDownMenu.setText("File");

        saveWorkingDataMenuItem.setText("Save Working Data");
        saveWorkingDataMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveWorkingDataMenuItemMouseClicked(evt);
            }
        });
        saveWorkingDataMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveWorkingDataMenuItemActionPerformed(evt);
            }
        });
        fileDropDownMenu.add(saveWorkingDataMenuItem);

        fileExitMenuItem.setText("Exit");
        fileExitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileExitMenuItemActionPerformed(evt);
            }
        });
        fileDropDownMenu.add(fileExitMenuItem);

        topMenuBar.add(fileDropDownMenu);

        linguisticDropDownMenu.setText("Linguistic Tools");

        linguisticAddLanguageMenuItem.setText("Add Language");
        linguisticAddLanguageMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linguisticAddLanguageMenuItemActionPerformed(evt);
            }
        });
        linguisticDropDownMenu.add(linguisticAddLanguageMenuItem);

        linguisticAddLexiconMenuItem.setText("Add Lexicon");
        linguisticAddLexiconMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linguisticAddLexiconMenuItemActionPerformed(evt);
            }
        });
        linguisticDropDownMenu.add(linguisticAddLexiconMenuItem);

        assignAcademicLevelsMenuItem.setText("Assign Academic Levels");
        assignAcademicLevelsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assignAcademicLevelsMenuItemActionPerformed(evt);
            }
        });
        linguisticDropDownMenu.add(assignAcademicLevelsMenuItem);

        exportCompiledExampleMenuItem.setText("Export Compiled Example");
        exportCompiledExampleMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportCompiledExampleMenuItemActionPerformed(evt);
            }
        });
        linguisticDropDownMenu.add(exportCompiledExampleMenuItem);

        topMenuBar.add(linguisticDropDownMenu);

        maintenanceDropDownMenu.setText("Maintenance");

        openMaitenanceMenuItem.setText("Open Maintenance Panel");
        openMaitenanceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMaitenanceMenuItemActionPerformed(evt);
            }
        });
        maintenanceDropDownMenu.add(openMaitenanceMenuItem);

        topMenuBar.add(maintenanceDropDownMenu);

        aboutDropDownMenu.setText("About");
        aboutDropDownMenu.setName(""); // NOI18N
        topMenuBar.add(aboutDropDownMenu);

        setJMenuBar(topMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fileExitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileExitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_fileExitMenuItemActionPerformed

    private void linguisticAddLanguageMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linguisticAddLanguageMenuItemActionPerformed
        // TODO add your handling code here:
        if (createLanguageDialog == null) {
            createLanguageDialog = new CreateLanguage();
            createLanguageDialog.setLocationRelativeTo(this);
        }
        
        createLanguageDialog.setVisible(true);
    }//GEN-LAST:event_linguisticAddLanguageMenuItemActionPerformed

    private void saveWorkingDataMenuItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveWorkingDataMenuItemMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_saveWorkingDataMenuItemMouseClicked

    private void saveWorkingDataMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveWorkingDataMenuItemActionPerformed
        // TODO add your handling code here:
        String result = WorkingDataManager.currentWorkingDataManager.SaveLanguages();
        result += "\n" + WorkingDataManager.currentWorkingDataManager.SaveLexica();
        showMessageDialog(this, result);
    }//GEN-LAST:event_saveWorkingDataMenuItemActionPerformed

    private void workingLanguageComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workingLanguageComboBoxActionPerformed
        int index = workingLanguageComboBox.getSelectedIndex();
        
        if (index != -1) {
            String languageCodeAsString = workingLanguageComboBox.getItemAt(index);
            WorkingDataManager.workingLanguage = Language.lookupLanguageCode(languageCodeAsString);
            languageViewTopPanelTitleLabel.setText(WorkingDataManager.workingLanguage.toHumanReadableString());
            System.out.println("Changed working language to: " + languageCodeAsString + "\n");
        }
    }//GEN-LAST:event_workingLanguageComboBoxActionPerformed

    private void linguisticAddLexiconMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linguisticAddLexiconMenuItemActionPerformed
        // TODO add your handling code here:
        if (createLexiconDialog == null) {
            createLexiconDialog = new CreateLexicon();
            createLexiconDialog.setLocationRelativeTo(this);
        }
        
        createLexiconDialog.setVisible(true);
    }//GEN-LAST:event_linguisticAddLexiconMenuItemActionPerformed

    private void workingLexiconComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workingLexiconComboBoxActionPerformed
        int cbIndex = workingLexiconComboBox.getSelectedIndex();

        if (cbIndex == -1) return;

        String lexiconName = (String) workingLexiconComboBox.getSelectedItem();
        String[] lexicaNames = WorkingDataManager.currentWorkingDataManager.lexicaNames(WorkingDataManager.workingLanguage);
        int lexicaIndex = 0;
        
        for (int i = 0; i < lexicaNames.length; i++)
            if (lexiconName.compareToIgnoreCase(lexicaNames[i]) == 0) {
                lexicaIndex = i;
                break;
            }
        
        WorkingDataManager.workingLexicon = WorkingDataManager.currentWorkingDataManager.lexicon(lexiconName);
        lexiconViewTopPanelTitleLabel.setText(lexiconName);
    }//GEN-LAST:event_workingLexiconComboBoxActionPerformed

    private void viewPanelAddLexemeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewPanelAddLexemeButtonActionPerformed
        NewLexeme newLexemeDialog = new NewLexeme(this, WorkingDataManager.workingLexicon);
        newLexemeDialog.setLocationRelativeTo(this);
        newLexemeDialog.setVisible(true);
    }//GEN-LAST:event_viewPanelAddLexemeButtonActionPerformed

    // Select Lexeme
    
    public void selectLexeme (Lex lexeme) {
        // TODO: Handle null lexeme!!!!
        selectedLexeme =  lexeme;
        viewPanelDeleteLexemeButton.setEnabled(lexeme != null);
        viewPanelRenameLexeme.setEnabled(lexeme != null);
        lexemeNameLabel.setText((lexeme != null) ? selectedLexeme.getLexeme() : "<Select a Lexeme>");
        lexemeStringTextField.setText((lexeme != null) ? selectedLexeme.getLexeme() : "<Select a Lexeme>");
        
        if (lexeme != null) {
            lexemeIsAbstractCheckBox.setEnabled(true);
            lexemeIsAbstractCheckBox.setSelected(selectedLexeme.getAbstraction());
            
            lexemeIsDynamicCheckBox.setEnabled(true);
            lexemeIsDynamicCheckBox.setSelected(selectedLexeme.getDynamic());
            
            lexemeEditButton.setEnabled(true);
            nounEditorManager = new NounEditorManager(selectedLexeme);
            
            selectedNoun = selectedLexeme.getNoun();
            
            semanticNetworkNounIsAManager.updateThingsThatExistButIAmNot();
            semanticNetworkNounIsAManager.updateThingsThatIAm();
            
            if (selectedNoun != null) {
                categoriesIPartakeInList.setModel(new DefaultComboBoxModel<String>(selectedNoun.getApplicableAdjectivalCategoryNamesAsSortedArray()));
                ArrayList<Adjective> inheritedAdjectives = new ArrayList<Adjective>();
                inheritedAdjectives.addAll(selectedNoun.getAllInheritedAdjectives());
                nounAdjectiveTable.setAdjectives(inheritedAdjectives);
            } else {
                nounAdjectiveTable.setAdjectives(new ArrayList<Adjective>());
            }
            
            String currentlySelected = lexiconViewLexemeListControl.getSelectedValue();
            
            if ((currentlySelected == null) ? true : (currentlySelected.compareTo(lexeme.getLexeme()) != 0)) {
                lexiconViewLexemeListControl.setSelectedValue(lexeme.getLexeme(), true);
            }
            
            ArrayList<Adjective> adjs = selectedLexeme.getAdjectives();
            adjPanelList.clear();
            adjPanelList.setAdjectives(adjs);
            
            boolean updatedAdjectives = false;
            
            if (adjs != null) {
                if (adjs.size() > 0) {
                    selectedAdjective = adjs.get(0);
                    adjPanelList.selectAdjective(selectedAdjective);
                    System.err.println("Getting gender of adjective");
                    setAdjGenderComboBox.setSelectedItem(selectedAdjective.getGender().toString());
                    updateAdjectiveConflicts(selectedAdjective);
                    updatedAdjectives = true;
                }
            }
            
            if (! updatedAdjectives) {
                selectedAdjective = null;
                adjPanelList.clear();
                setAdjGenderComboBox.setSelectedItem(Gender.UNKNOWN.toString());
                updateAdjectiveConflicts(null);
            }
        } else {
            lexemeIsAbstractCheckBox.setEnabled(false);
            lexemeIsDynamicCheckBox.setEnabled(false);
            lexemeEditButton.setEnabled(false);
            nounEditorManager = null;
            selectedNoun = null;
            semanticNetworkNounIsAManager.removeAll();
            selectedAdjective = null;
            adjPanelList.clear();
            categoriesIPartakeInList.setModel(new DefaultComboBoxModel<String>(new String[0]));
            updateAdjectiveConflicts(null);
        }
    }
    
    private void lexiconViewLexemeListControlValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lexiconViewLexemeListControlValueChanged
        String selectedLexemeName = lexiconViewLexemeListControl.getSelectedValue();
        
        if (selectedLexemeName != null) {
            selectLexeme(WorkingDataManager.workingLexicon.get(selectedLexemeName));
        } else {
            lexemeNameLabel.setText("");
            lexemeStringTextField.setText("");
            lexemeIsAbstractCheckBox.setSelected(false);
            lexemeIsDynamicCheckBox.setSelected(false);
            lexemeEditButton.setEnabled(false);
            // nounEditorManager = new NounEditorManager(null);
        }
        
        if (nounEditorManager != null)
            nounEditorManager.setEditing(false);
    }//GEN-LAST:event_lexiconViewLexemeListControlValueChanged

    private void viewPanelDeleteLexemeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewPanelDeleteLexemeButtonActionPerformed
        if (selectedLexeme != null) {
             if (showConfirmDialog(this, "Delete lexeme \"" + selectedLexeme.getLexeme()
                    + "\"?\nDeleting will remove this lexeme and all associated data!",
                    "Delete Lexeme", YES_NO_OPTION)
                     ==
                     YES_OPTION) {
                WorkingDataManager.workingLexicon.remove(selectedLexeme.getLexeme());
                selectLexeme(null);
                viewPanelDeleteLexemeButton.setEnabled(false);
                viewPanelRenameLexeme.setEnabled(false);
                updateView();
             }
        }
    }//GEN-LAST:event_viewPanelDeleteLexemeButtonActionPerformed

    // Edit Lexeme
    private void lexemeEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lexemeEditButtonActionPerformed
        lexemeEditButton.setEnabled(false);
        lexemeDimensionCancelButton.setEnabled(true);
        lexemeDimensionSaveButton.setEnabled(true);
        lexemeStringTextField.setEnabled(true);
        lexemeIsAbstractCheckBox.setEnabled(true);
        lexemeIsDynamicCheckBox.setEnabled(true);
    }//GEN-LAST:event_lexemeEditButtonActionPerformed

    // Cancel Lexeme Edit
    private void lexemeDimensionCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lexemeDimensionCancelButtonActionPerformed
        lexemeEditButton.setEnabled(true);
        lexemeDimensionCancelButton.setEnabled(false);
        lexemeDimensionSaveButton.setEnabled(false);
        lexemeStringTextField.setEnabled(false);
        lexemeIsAbstractCheckBox.setEnabled(false);
        lexemeIsDynamicCheckBox.setEnabled(false);
        
        lexemeStringTextField.setText(selectedLexeme.getLexeme());
        lexemeIsAbstractCheckBox.setSelected(selectedLexeme.getAbstraction());
        lexemeIsDynamicCheckBox.setSelected(selectedLexeme.getDynamic());
    }//GEN-LAST:event_lexemeDimensionCancelButtonActionPerformed

    // Save Lexeme Edit
    private void lexemeDimensionSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lexemeDimensionSaveButtonActionPerformed
        lexemeEditButton.setEnabled(true);
        lexemeDimensionCancelButton.setEnabled(false);
        lexemeDimensionSaveButton.setEnabled(false);
        lexemeStringTextField.setEnabled(false);
        lexemeIsAbstractCheckBox.setEnabled(false);
        lexemeIsDynamicCheckBox.setEnabled(false);
        
        if (WorkingDataManager.workingLexicon != null) {
            if (selectedLexeme != null) {
                WorkingDataManager.workingLexicon.remove(selectedLexeme.getLexeme());
                selectedLexeme.setLexeme(lexemeStringTextField.getText());
                selectedLexeme.setAbstraction(lexemeIsDynamicCheckBox.isSelected());
                selectedLexeme.setDynamic(lexemeIsDynamicCheckBox.isSelected());
                WorkingDataManager.workingLexicon.intern(selectedLexeme.getLexeme(), selectedLexeme);
                updateView();
                selectedLexeme = WorkingDataManager.workingLexicon.get(selectedLexeme.getLexeme());
                viewPanelDeleteLexemeButton.setEnabled(true);
                viewPanelRenameLexeme.setEnabled(true);
                lexemeNameLabel.setText(selectedLexeme.getLexeme());
                lexemeStringTextField.setText(selectedLexeme.getLexeme());
                lexemeIsAbstractCheckBox.setSelected(selectedLexeme.getAbstraction());
                lexemeIsDynamicCheckBox.setSelected(selectedLexeme.getDynamic());
                lexemeEditButton.setEnabled(true);
                lexiconViewLexemeListControl.setSelectedValue(selectedLexeme.getLexeme(), true);
            }
        }
    }//GEN-LAST:event_lexemeDimensionSaveButtonActionPerformed

    private void nounCountabilityComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nounCountabilityComboBoxActionPerformed
        if (nounEditorManager == null) {
            nounEditorManager = new NounEditorManager(selectedLexeme);
        }
        
        nounEditorManager.setCountability((String) nounCountabilityComboBox.getSelectedItem());
    }//GEN-LAST:event_nounCountabilityComboBoxActionPerformed

    private void nounLexemeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nounLexemeComboBoxActionPerformed
        if (nounEditorManager == null) {
            nounEditorManager = new NounEditorManager(selectedLexeme);
        }
        
        nounEditorManager.setLexeme((String) nounLexemeComboBox.getSelectedItem());
    }//GEN-LAST:event_nounLexemeComboBoxActionPerformed

    private void nounTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nounTypeComboBoxActionPerformed
        if (nounEditorManager == null) {
            nounEditorManager = new NounEditorManager(selectedLexeme);
        }
        
        nounEditorManager.setNounType((String) nounTypeComboBox.getSelectedItem());
    }//GEN-LAST:event_nounTypeComboBoxActionPerformed

    private void nounAlwaysPluralCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nounAlwaysPluralCheckBoxActionPerformed
        if (nounEditorManager == null) {
            nounEditorManager = new NounEditorManager(selectedLexeme);
        }
        
        nounEditorManager.setAlwaysPlural(nounAlwaysPluralCheckBox.isSelected());
    }//GEN-LAST:event_nounAlwaysPluralCheckBoxActionPerformed

    private void masculineRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masculineRadioButtonActionPerformed
        if (nounEditorManager == null) {
            nounEditorManager = new NounEditorManager(selectedLexeme);
        }
        
        if (masculineRadioButton.isSelected())
            nounEditorManager.setGender(Gender.MASCULINE);
    }//GEN-LAST:event_masculineRadioButtonActionPerformed

    private void feminineRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feminineRadioButtonActionPerformed
        if (nounEditorManager == null) {
            nounEditorManager = new NounEditorManager(selectedLexeme);
        }
        
        if (feminineRadioButton.isSelected())
            nounEditorManager.setGender(Gender.FEMININE);
    }//GEN-LAST:event_feminineRadioButtonActionPerformed

    private void neuterRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_neuterRadioButtonActionPerformed
        if (nounEditorManager == null) {
            nounEditorManager = new NounEditorManager(selectedLexeme);
        }
        
        if (neuterRadioButton.isSelected())
            nounEditorManager.setGender(Gender.NEUTER);
    }//GEN-LAST:event_neuterRadioButtonActionPerformed

    private void animateRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_animateRadioButtonActionPerformed
        if (nounEditorManager == null) {
            nounEditorManager = new NounEditorManager(selectedLexeme);
        }
        
        if (animateRadioButton.isSelected())
            nounEditorManager.setGender(Gender.ANIMATE);
    }//GEN-LAST:event_animateRadioButtonActionPerformed

    private void inanimateRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inanimateRadioButtonActionPerformed
        if (nounEditorManager == null) {
            nounEditorManager = new NounEditorManager(selectedLexeme);
        }
        
        if (inanimateRadioButton.isSelected())
            nounEditorManager.setGender(Gender.INANIMATE);
    }//GEN-LAST:event_inanimateRadioButtonActionPerformed

    private void commonRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commonRadioButtonActionPerformed
        if (selectedLexeme == null) return;
        
        if (nounEditorManager == null) {
            nounEditorManager = new NounEditorManager(selectedLexeme);
        }
        
        if (commonRadioButton.isSelected())
            nounEditorManager.setGender(Gender.COMMON);
    }//GEN-LAST:event_commonRadioButtonActionPerformed

    private void nounEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nounEditButtonActionPerformed
        if (selectedLexeme == null) return;
        
        if (nounEditorManager == null) {
            nounEditorManager = new NounEditorManager(selectedLexeme);
        }

        nounEditorManager.setEditing(true);
    }//GEN-LAST:event_nounEditButtonActionPerformed

    private void nounCancelEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nounCancelEditButtonActionPerformed
        if (selectedLexeme == null) return;
        
        nounEditorManager = new NounEditorManager(selectedLexeme);
        nounEditorManager.setEditing(false);
    }//GEN-LAST:event_nounCancelEditButtonActionPerformed

    private void nounSaveChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nounSaveChangesButtonActionPerformed
        if (selectedLexeme == null) return;
        
        if (nounEditorManager == null) {
            nounEditorManager = new NounEditorManager(selectedLexeme);
        }

        nounEditorManager.save();
        nounEditorManager.setEditing(true);
                
        semanticNetworkNounIsAManager.updateThingsThatExistButIAmNot();
        semanticNetworkNounIsAManager.updateThingsThatIAm();
    }//GEN-LAST:event_nounSaveChangesButtonActionPerformed

    public String[] getListControlItems(javax.swing.JList<String> listControl) {
        ListModel<String> model = listControl.getModel();
        int count = model.getSize();
        String[] result = new String[count];
        
        for (int i = 0; i < count; i++)
            result[i] = model.getElementAt(i);
        
        return result;
    }                                                 

    public void selectListControlItem(javax.swing.JList<String> listControl, String item) {
        ListModel<String> model = listControl.getModel();
        int count = model.getSize();
        
        for (int i = 0; i < count; i++)
            if (model.getElementAt(i).compareTo(item) == 0) {
                listControl.setSelectedIndex(i);
                return;
            }
    }                                            

    public void selectListControlItemFirstMatch(javax.swing.JList<String> listControl, String item) {
        ListModel<String> model = listControl.getModel();
        int count = model.getSize();
        int itemLength = item.length();
        
        if (itemLength == 0) {
            lexiconViewLexemeListControl.clearSelection();
            return;
        }
        
        for (int i = 0; i < count; i++) {
            String currentItem = model.getElementAt(i);
            int currentItemLength = currentItem.length();
            
            if (currentItemLength < itemLength) continue;
            
            String currentItemFirstNChars = currentItem.substring(0, itemLength);
            
            if (currentItemFirstNChars.compareToIgnoreCase(item) == 0) {
                listControl.setSelectedIndex(i);
                lexiconViewLexemeScrollPane.scrollRectToVisible(listControl.getCellBounds(i, i));
                return;
            }
        }
    }
    
    private void lexiconViewSearchTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lexiconViewSearchTextFieldKeyPressed
        int code = evt.getKeyCode();
        String searchText;
        int len;
        
        switch (code) {
            case 8: // backspace
                searchText = lexiconViewSearchTextField.getText();
                len = searchText.length();
                
                if (len > 0) {
                    searchText = searchText.substring(0, len - 1);
                }
            default:
                searchText = lexiconViewSearchTextField.getText() + evt.getKeyChar();
        }
        
        selectListControlItemFirstMatch(lexiconViewLexemeListControl, searchText);
    }//GEN-LAST:event_lexiconViewSearchTextFieldKeyPressed

    private void lexiconViewSearchTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lexiconViewSearchTextFieldKeyTyped
        int code = evt.getKeyCode();
        String searchText = lexiconViewSearchTextField.getText();
        String selectedItem;
        
        if (code == 13) {
            selectListControlItemFirstMatch(lexiconViewLexemeListControl, searchText);
            selectedItem = lexiconViewLexemeListControl.getSelectedValue();
            if (selectedItem != null) {
                selectLexeme(WorkingDataManager.workingLexicon.get(selectedItem));
            }
        } else if (searchText.compareTo("") == 0) {
            lexiconViewLexemeListControl.clearSelection();
            return;
        }
        
        selectListControlItemFirstMatch(lexiconViewLexemeListControl, searchText);
    }//GEN-LAST:event_lexiconViewSearchTextFieldKeyTyped

    private void lexicalDimensionsPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_lexicalDimensionsPanelStateChanged
        JPanel selectedPanel = (JPanel) lexicalDimensionsPanel.getSelectedComponent();
        String panelName = selectedPanel.getName();
        // lexemeNameLabel
        // partOfSpeechLabel
        
        if (selectedPanel == lexemeDimensionPanel) {
            if (selectedLexeme != null) {
                lexemeNameLabel.setText(selectedLexeme.getLexeme());
            } else {
                lexemeNameLabel.setText("");
            }
            
            partOfSpeechLabel.setText("");
        } else if (selectedPanel == nounDimensionPanel) {
            if (selectedLexeme != null) {
                lexemeNameLabel.setText(selectedLexeme.getLexeme());
            } else {
                lexemeNameLabel.setText("");
            }
            
            partOfSpeechLabel.setText("/ " + panelName);
        } else if (selectedPanel == adjectiveDimensionMainPanel) {
            if (selectedLexeme != null) {
                lexemeNameLabel.setText(selectedLexeme.getLexeme());
            } else {
                lexemeNameLabel.setText("");
            }
            
            partOfSpeechLabel.setText("/ " + panelName);
        }
    }//GEN-LAST:event_lexicalDimensionsPanelStateChanged

    private void nounTabbedPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_nounTabbedPanelStateChanged
        JPanel selectedPanel = (JPanel) nounTabbedPanel.getSelectedComponent();
            
        if (selectedPanel == nounSemanticNetworkPanel) {
            System.out.println("nounSemanticNetworkPanel\n");
            if (selectedLexeme != null) {
                selectedNoun = selectedLexeme.getNoun();
                semanticNetworkNounIsAManager.updateThingsThatExistButIAmNot();
                semanticNetworkNounIsAManager.updateThingsThatIAm();
            }
        }
    }//GEN-LAST:event_nounTabbedPanelStateChanged

    private void thingsThatExistListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_thingsThatExistListValueChanged
        int[] selectedIndices = thingsThatExistList.getSelectedIndices();
        
        addThingsThatIAmButton.setEnabled(selectedIndices.length > 0);
    }//GEN-LAST:event_thingsThatExistListValueChanged

    private void thingsThatIAmListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_thingsThatIAmListValueChanged
        int[] selectedIndices = thingsThatIAmList.getSelectedIndices();
        
        removeThingsThatIAmButton.setEnabled(selectedIndices.length > 0);
    }//GEN-LAST:event_thingsThatIAmListValueChanged

    private void addThingsThatIAmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addThingsThatIAmButtonActionPerformed
        List<String> selectedItems = thingsThatExistList.getSelectedValuesList();
        
        System.out.print("selectedItems: ");
        for (String item : selectedItems) {
            System.out.println(" " + item);
        }
        System.out.println("");
        
        semanticNetworkNounIsAManager.addThingsThatIAm(selectedItems);
    }//GEN-LAST:event_addThingsThatIAmButtonActionPerformed

    private void removeThingsThatIAmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeThingsThatIAmButtonActionPerformed
        List<String> selectedItems = thingsThatIAmList.getSelectedValuesList();
        semanticNetworkNounIsAManager.removeThingsThatIAm(selectedItems);
    }//GEN-LAST:event_removeThingsThatIAmButtonActionPerformed

    private void thingsThatIAmThroughInferenceListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_thingsThatIAmThroughInferenceListValueChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_thingsThatIAmThroughInferenceListValueChanged

    private void newAdjectiveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newAdjectiveButtonActionPerformed
        // New Adjective
        if (selectedLexeme == null) return;
        
        selectedAdjective = selectedLexeme.createAdjective();
        adjPanelList.addAdjective(selectedAdjective);
        updateAdjectiveConflicts(selectedAdjective);
    }//GEN-LAST:event_newAdjectiveButtonActionPerformed

    private void saveAdjectiveChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAdjectiveChangesButtonActionPerformed
        String result = WorkingDataManager.currentWorkingDataManager.SaveLanguages();
        result += "\n" + WorkingDataManager.currentWorkingDataManager.SaveLexica();
        showMessageDialog(this, result);
    }//GEN-LAST:event_saveAdjectiveChangesButtonActionPerformed

    private void selCatComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selCatComboBoxActionPerformed
        String categoryName = (String) selCatComboBox.getSelectedItem();
        
        if ((categoryName == null) || (adjCategoryManager == null)) return;
        
        AdjectivalCategory category = adjCategoryManager.getCategoryByName(categoryName);
        
        if (category == null) return;
        
        category.setCategory(selectedAdjective);
        adjPanelList.updateFromOutside(selectedAdjective);
        updateAdjectiveConflicts(selectedAdjective);
        
        switch (category.getType()) {
            case QUANTIFIER:
                setQuantifierTypeLabel.setVisible(true);
                setQuantifierTypeComboBox.setVisible(true);
            case ARTICLE:
            case DEMONSTRATIVE:
            case POSSESSIVE:
                setPluralityLabel.setVisible(true);
                setPluralityComboBox.setVisible(true);
                break;
        }
    }//GEN-LAST:event_selCatComboBoxActionPerformed

    private void deleteAdjectiveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAdjectiveButtonActionPerformed
        // Delete adjective
        if (selectedLexeme == null) return;
        if (selectedAdjective == null) return;
        
        selectedLexeme.removeAdjective(selectedAdjective);
        selectedAdjective = null;
        updateAdjectiveConflicts(selectedAdjective);
    }//GEN-LAST:event_deleteAdjectiveButtonActionPerformed

    private void viewPanelRenameLexemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewPanelRenameLexemeActionPerformed
        // RenameLexeme
        
        if (selectedLexeme == null) return;
        
        RenameLexeme form = new RenameLexeme(this, WorkingDataManager.workingLexicon, selectedLexeme);
        form.setLocationRelativeTo(this);
        form.setVisible(true);
    }//GEN-LAST:event_viewPanelRenameLexemeActionPerformed

    private void repairSemanticNetworkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repairSemanticNetworkButtonActionPerformed
        if (WorkingDataManager.workingLexicon != null) {
            WorkingDataManager.workingLexicon.repairSementicNetwork();
        }
    }//GEN-LAST:event_repairSemanticNetworkButtonActionPerformed

    private void viewOntologyGeneralizeDiagramButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewOntologyGeneralizeDiagramButtonActionPerformed
        if (selectedNoun != null) {
            OntologyGeneralizeRelationshipViewer view = new OntologyGeneralizeRelationshipViewer();
            view.setLocationRelativeTo(this);
            view.setVisible(true);
            view.attachDiagram(selectedNoun);
        }
    }//GEN-LAST:event_viewOntologyGeneralizeDiagramButtonActionPerformed

    private void removeCategoriesIPartakeInButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeCategoriesIPartakeInButtonActionPerformed
        if (selectedNoun == null) return;
        
        int[] indices = categoriesIPartakeInList.getSelectedIndices();
        int indexCount = indices.length;
        
        if (indexCount == 0) return;
        
        ArrayList<AdjectivalCategory> categories = selectedNoun.getApplicableAdjectivalCategoriesAsSortedArrayList();
        
        for (int i = 0; i < indexCount; i++) {
            selectedNoun.removeCustomApplicableAdjectivalCategory(categories.get(indices[i]));
        }
        
        categoriesIPartakeInList.setModel(new DefaultComboBoxModel<String>(selectedNoun.getSelectedAdjectivalCategoryNamesAsSortedArray()));
    }//GEN-LAST:event_removeCategoriesIPartakeInButtonActionPerformed

    private void addCategoriesIPartakeInButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCategoriesIPartakeInButtonActionPerformed
        System.out.println("addCategoriesIPartakeInButtonActionPerformed():");
        
        if (selectedNoun == null) {
            System.err.println("addCategoriesIPartakeInButtonActionPerformed(): selectedNoun is null");
            return;
        }
        
        int[] indices = categoriesList.getSelectedIndices();
        int indexCount = indices.length;
        
        if (indexCount == 0) {
            System.err.println("addCategoriesIPartakeInButtonActionPerformed(): categoriesList is empty");
            return;
        }

        Lexicon lexicon = WorkingDataManager.workingLexicon;
        AdjectivalCategoryManager adjCatManager = lexicon.getAdjCatManager();
        ArrayList<AdjectivalCategory> categories = adjCatManager.getCategoriesByPrecedence();
        
        for (int i = 0; i < indexCount; i++) {
            selectedNoun.addCustomApplicableAdjectivalCategory(categories.get(indices[i]));
        }
        
        categoriesIPartakeInList.setModel(new DefaultComboBoxModel<String>(selectedNoun.getSelectedAdjectivalCategoryNamesAsSortedArray()));
    }//GEN-LAST:event_addCategoriesIPartakeInButtonActionPerformed

    private void categoriesIPartakeInListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_categoriesIPartakeInListValueChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_categoriesIPartakeInListValueChanged

    private void repairAdjectivalCategoryManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repairAdjectivalCategoryManagerActionPerformed
        if (WorkingDataManager.workingLexicon != null) {
            WorkingDataManager.workingLexicon.repairAdjectivalCategories();
        }
    }//GEN-LAST:event_repairAdjectivalCategoryManagerActionPerformed

    private void openMaitenanceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMaitenanceMenuItemActionPerformed
        MaintenancePanel p = new MaintenancePanel();
        p.setLocationRelativeTo(this);
        p.setVisible(true);
    }//GEN-LAST:event_openMaitenanceMenuItemActionPerformed

    private void exportCompiledExampleMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportCompiledExampleMenuItemActionPerformed
        ExportCompiledExample form = new ExportCompiledExample();
        form.setLocationRelativeTo(this);
        form.setVisible(true);
    }//GEN-LAST:event_exportCompiledExampleMenuItemActionPerformed

    private void selectAdjectivesToAssignButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAdjectivesToAssignButtonActionPerformed
        ICollectionModifiedListener listener =
                new ICollectionModifiedListener() {
                    public void collectionModified(Collection stuff) {
                        if (stuff == null) return;
                        System.out.println("collectionModified(): stuff=" + stuff + ", size=" + stuff.size());
                        ArrayList<Adjective> adjs = new ArrayList<Adjective>();
                        adjs.addAll(stuff);
                        AdjectiveSet newSet = new AdjectiveSet();
                        newSet.addAll(stuff);
                        selectedNoun.setCustomAdjectiveSet(newSet);
                        nounAdjectiveTable.setAdjectives(selectedNoun.getAllInheritedAdjectives());
                    }
                };
        AdjectiveListSelectorFrame form = new AdjectiveListSelectorFrame();
        form.addListener(listener);
        form.setLocationRelativeTo(this);
        form.setVisible(true);
    }//GEN-LAST:event_selectAdjectivesToAssignButtonActionPerformed

    private void addAdjectivesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAdjectivesActionPerformed
        ICollectionModifiedListener listener =
                new ICollectionModifiedListener() {
                    public void collectionModified(Collection stuff) {
                        if (stuff == null) return;
                        HashSet<Adjective> customAdjs = selectedNoun.getCustomAdjectiveSet();
                        if (customAdjs == null) {
                            customAdjs = new AdjectiveSet();
                            selectedNoun.setCustomAdjectiveSet((AdjectiveSet) customAdjs);
                        }
                        customAdjs.addAll(stuff);
                        nounAdjectiveTable.setAdjectives(selectedNoun.getAllInheritedAdjectives());
                    }
                };
        AdjectiveListSelectorFrame form = new AdjectiveListSelectorFrame();
        form.addListener(listener);
        form.setLocationRelativeTo(this);
        form.setVisible(true);
    }//GEN-LAST:event_addAdjectivesActionPerformed

    private void excludeAdjectivesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excludeAdjectivesButtonActionPerformed
        if (selectedNoun == null) return;
        
        AdjectiveSet currentlyExcludedAdjectives
                = (selectedNoun.getCustomAdjectiveExclusionSet() != null)
                    ? selectedNoun.getCustomAdjectiveExclusionSet()
                    : new AdjectiveSet();
        
        HashSet<Adjective> adjectivesToExclude = new HashSet<Adjective>();
        
        ICollectionModifiedListener listener =
                new ICollectionModifiedListener() {
                    public void collectionModified(Collection stuff) {
                        System.out.println("excludeAdjectivesButtonActionPerformed.collectionModified:");
                        if (stuff == null) return;
                        currentlyExcludedAdjectives.clear();
                        
                        for (Object adj : stuff) {
                            System.out.println("  Adding " + adj + " to exclusions list.");
                            currentlyExcludedAdjectives.add((Adjective) adj);
                        }
                        
                        selectedNoun.setCustomAdjectiveExclusionSet(currentlyExcludedAdjectives);
                        nounAdjectiveTable.setAdjectives(selectedNoun.getAllInheritedAdjectives());
                    }
                };
        
        adjectivesToExclude.addAll(currentlyExcludedAdjectives);
        
        AdjectiveExclusionFrame frame
                = new AdjectiveExclusionFrame(selectedNoun.getAllInheritedAdjectivesIgnoringThisNounsExclusions(),
                        adjectivesToExclude);
        frame.addListener(listener);
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }//GEN-LAST:event_excludeAdjectivesButtonActionPerformed

    private void assignAcademicLevelsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assignAcademicLevelsMenuItemActionPerformed
        AssignAcademicLevelFrame frame = new AssignAcademicLevelFrame();
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }//GEN-LAST:event_assignAcademicLevelsMenuItemActionPerformed

    private void setAdjGenderComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setAdjGenderComboBoxActionPerformed
        String genderName = (String) setAdjGenderComboBox.getSelectedItem();
        Gender gender = Gender.fromString(genderName);
        
        if (selectedAdjective != null) {
            selectedAdjective.setGender(gender);
        }
    }//GEN-LAST:event_setAdjGenderComboBoxActionPerformed

    private void setQuantifierTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setQuantifierTypeComboBoxActionPerformed
        String quantifierTypeName = (String) setQuantifierTypeComboBox.getSelectedItem();
        
        if (quantifierTypeName == null) return;
        
        Adjective.QUANTIFIER_TYPE qt = Adjective.QUANTIFIER_TYPE.fromString(quantifierTypeName);
        
        if (selectedAdjective != null) {
            selectedAdjective.setQuantifierType(qt);
        }
    }//GEN-LAST:event_setQuantifierTypeComboBoxActionPerformed

    private void setPluralityComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setPluralityComboBoxActionPerformed
        String pluralityName = (String) setPluralityComboBox.getSelectedItem();
        
        if (pluralityName == null) return;
        
        Adjective.PLURALITY p = Adjective.PLURALITY.fromString(pluralityName);
        
        if (selectedAdjective != null) {
            selectedAdjective.setPlurality(p);
        }
    }//GEN-LAST:event_setPluralityComboBoxActionPerformed

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
            java.util.logging.Logger.getLogger(GrammarManangerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GrammarManangerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GrammarManangerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GrammarManangerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        // Language.testSerializationToXML();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GrammarManangerFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu aboutDropDownMenu;
    private javax.swing.JButton addAdjectives;
    private javax.swing.JButton addCategoriesIPartakeInButton;
    private javax.swing.JButton addThingsThatIAmButton;
    private javax.swing.JPanel adjButtonPanel;
    private javax.swing.JPanel adjCancelSavePanel;
    private javax.swing.JPanel adjCatCategoryPanel;
    private javax.swing.JPanel adjCatGenderPanel;
    private javax.swing.JPanel adjCatPanel;
    private javax.swing.JPanel adjCatPluralityPanel;
    private javax.swing.JPanel adjCatQuantifierTypePanel;
    private javax.swing.JPanel adjConflictTransferPanel;
    private javax.swing.JList<String> adjConflictsList;
    private javax.swing.JPanel adjConflictsPanel;
    private javax.swing.JScrollPane adjConflictsScrollPane;
    private javax.swing.JPanel adjDefPanel;
    private javax.swing.JLabel adjDefPrompt;
    private javax.swing.JPanel adjDefPromptPane;
    private javax.swing.JScrollPane adjDefScrollPane;
    private javax.swing.JTextPane adjDefTextPane;
    private javax.swing.JPanel adjDimBottomPanel;
    private javax.swing.JPanel adjDimLeftPanel;
    private javax.swing.JPanel adjDimRightPanel1;
    private javax.swing.JPanel adjDimRightPanel2;
    private javax.swing.JPanel adjDimTopPanel;
    private javax.swing.JList<String> adjNonConflictsList;
    private javax.swing.JPanel adjNonConflictsPanel;
    private javax.swing.JScrollPane adjNonConflictsScrollPane;
    private javax.swing.JLabel adjectivalCategoriesIParktakeInLabel;
    private javax.swing.JPanel adjectivalCategoriesPropertiesPanel;
    private javax.swing.JTabbedPane adjectivalCategoriesTabbedPane;
    private javax.swing.JPanel adjectiveDimensionMainPanel;
    private javax.swing.JPanel adjectiveDimensionPanel;
    private javax.swing.JPanel adjectiveTablePanel;
    private javax.swing.JPanel adjectiveToNounAssignmentPanel;
    private javax.swing.JRadioButton animateRadioButton;
    private javax.swing.JMenuItem assignAcademicLevelsMenuItem;
    private javax.swing.JPanel assignAdjectivesButtonPanel;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JPanel categoriesIPartakeInButtonPanel;
    private javax.swing.JLabel categoriesIPartakeInLabel;
    private javax.swing.JList<String> categoriesIPartakeInList;
    private javax.swing.JPanel categoriesIPartakeInPanel;
    private javax.swing.JScrollPane categoriesIPartakeInScrollPane;
    private javax.swing.JList<String> categoriesList;
    private javax.swing.JScrollPane categoriesScrollPane;
    private javax.swing.JPanel categoriesThatExistPanel;
    private javax.swing.JPanel categoryToNounAssignmentPanel;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JRadioButton commonRadioButton;
    private javax.swing.JButton deleteAdjectiveButton;
    private javax.swing.JButton excludeAdjectivesButton;
    private javax.swing.JMenuItem exportCompiledExampleMenuItem;
    private javax.swing.JRadioButton feminineRadioButton;
    private javax.swing.JMenu fileDropDownMenu;
    private javax.swing.JMenuItem fileExitMenuItem;
    private javax.swing.ButtonGroup genderButtonGroup;
    private javax.swing.JRadioButton inanimateRadioButton;
    private javax.swing.JPanel languagePropertiesPanel1;
    private javax.swing.JPanel languagePropertiesPanel2;
    private javax.swing.JPanel languagePropertiesPanel3;
    private javax.swing.JPanel languagePropertiesPanel4;
    private javax.swing.JPanel languageViewCenterPanel;
    private javax.swing.JPanel languageViewInfoPanel;
    private javax.swing.JPanel languageViewPanel;
    private javax.swing.JPanel languageViewTopPanel;
    private javax.swing.JLabel languageViewTopPanelTitleLabel;
    private javax.swing.JPanel lexemeDimensionBottomPanel;
    private javax.swing.JButton lexemeDimensionCancelButton;
    private javax.swing.JPanel lexemeDimensionCenterPanel;
    private javax.swing.JPanel lexemeDimensionPanel;
    private javax.swing.JButton lexemeDimensionSaveButton;
    private javax.swing.JPanel lexemeDimensionTopPanel;
    private javax.swing.JButton lexemeEditButton;
    private javax.swing.JCheckBox lexemeIsAbstractCheckBox;
    private javax.swing.JCheckBox lexemeIsDynamicCheckBox;
    private javax.swing.JLabel lexemeLabel;
    private javax.swing.JLabel lexemeNameLabel;
    private javax.swing.JLabel lexemeStringLabel;
    private javax.swing.JTextField lexemeStringTextField;
    private javax.swing.JTabbedPane lexicalDimensionsPanel;
    private javax.swing.JPanel lexiconAdjectivalCategoriesPanel;
    private javax.swing.JPanel lexiconSemanticNetworkHasPanel;
    private javax.swing.JPanel lexiconSemanticNetworkIsAPanel;
    private javax.swing.JPanel lexiconSemanticNetworkIsAbleToPanel;
    private javax.swing.JPanel lexiconSemanticNetworkIsModifiedByPanel;
    private javax.swing.JPanel lexiconSemanticNetworkMainPanel;
    private javax.swing.JScrollPane lexiconSemanticNetworkScrollPane;
    private javax.swing.JPanel lexiconSemanticNetworkScrollPanel;
    private javax.swing.JPanel lexiconViewAddDeletePanel;
    private javax.swing.JPanel lexiconViewLeftPanel;
    private javax.swing.JList<String> lexiconViewLexemeListControl;
    private javax.swing.JScrollPane lexiconViewLexemeScrollPane;
    private javax.swing.JPanel lexiconViewPanel;
    private javax.swing.JPanel lexiconViewSearchAddDeletePanel;
    private javax.swing.JButton lexiconViewSearchButton;
    private javax.swing.JPanel lexiconViewSearchPanel;
    private javax.swing.JTextField lexiconViewSearchTextField;
    private javax.swing.JPanel lexiconViewTopPanel;
    private javax.swing.JLabel lexiconViewTopPanelTitleLabel;
    private javax.swing.JMenuItem linguisticAddLanguageMenuItem;
    private javax.swing.JMenuItem linguisticAddLexiconMenuItem;
    private javax.swing.JMenu linguisticDropDownMenu;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JMenu maintenanceDropDownMenu;
    private javax.swing.JRadioButton masculineRadioButton;
    private javax.swing.JRadioButton neuterRadioButton;
    private javax.swing.JButton newAdjectiveButton;
    private javax.swing.JLabel nonConflictsLabel;
    private javax.swing.JLabel nonConflictsLabel1;
    private javax.swing.JPanel nounAbilitiesPanel;
    private javax.swing.JPanel nounAdjectivesPanel;
    private javax.swing.JCheckBox nounAlwaysPluralCheckBox;
    private javax.swing.JPanel nounAttributesPanel;
    private javax.swing.JPanel nounBottomPanel;
    private javax.swing.JPanel nounBottomTopPanel;
    private javax.swing.JButton nounCancelEditButton;
    private javax.swing.JComboBox<String> nounCountabilityComboBox;
    private javax.swing.JPanel nounDimensionPanel;
    private javax.swing.JButton nounEditButton;
    private javax.swing.JPanel nounGrammarPanel;
    private javax.swing.JScrollPane nounIsANounListScrollPane;
    private javax.swing.JComboBox<String> nounLexemeComboBox;
    private javax.swing.JLabel nounLexemeLabel;
    private javax.swing.JTextField nounPluralTextField;
    private javax.swing.JButton nounSaveChangesButton;
    private javax.swing.JPanel nounSemanticNetworkPanel;
    private javax.swing.JTabbedPane nounTabbedPanel;
    private javax.swing.JPanel nounTopPanel;
    private javax.swing.JPanel nounTopTopPanel;
    private javax.swing.JComboBox<String> nounTypeComboBox;
    private javax.swing.JMenuItem openMaitenanceMenuItem;
    private javax.swing.JLabel partOfSpeechLabel;
    private javax.swing.JButton removeCategoriesIPartakeInButton;
    private javax.swing.JButton removeThingsThatIAmButton;
    private javax.swing.JButton repairAdjectivalCategoryManager;
    private javax.swing.JButton repairSemanticNetworkButton;
    private javax.swing.JButton saveAdjectiveChangesButton;
    private javax.swing.JMenuItem saveWorkingDataMenuItem;
    private javax.swing.JComboBox<String> selCatComboBox;
    private javax.swing.JLabel selCatLabel;
    private javax.swing.JButton selectAdjectivesToAssignButton;
    private javax.swing.JComboBox<String> setAdjGenderComboBox;
    private javax.swing.JLabel setAdjGenderLabel;
    private javax.swing.JComboBox<String> setPluralityComboBox;
    private javax.swing.JLabel setPluralityLabel;
    private javax.swing.JComboBox<String> setQuantifierTypeComboBox;
    private javax.swing.JLabel setQuantifierTypeLabel;
    private javax.swing.JPanel thingsThatExistButtonPanel;
    private javax.swing.JLabel thingsThatExistLabel;
    private javax.swing.JList<String> thingsThatExistList;
    private javax.swing.JPanel thingsThatExistPanel;
    private javax.swing.JPanel thingsThatIAmByInferencePanel;
    private javax.swing.JLabel thingsThatIAmLabel;
    private javax.swing.JList<String> thingsThatIAmList;
    private javax.swing.JPanel thingsThatIAmPanel;
    private javax.swing.JScrollPane thingsThatIAmScrollPanel;
    private javax.swing.JLabel thingsThatIAmThroughInferenceLabel;
    private javax.swing.JList<String> thingsThatIAmThroughInferenceList;
    private javax.swing.JScrollPane thingsThatIAmThroughInferenceScrollPanel;
    private javax.swing.JMenuBar topMenuBar;
    private javax.swing.JPanel topPanel;
    private javax.swing.JButton viewOntologyGeneralizeDiagramButton;
    private javax.swing.JButton viewPanelAddLexemeButton;
    private javax.swing.JButton viewPanelDeleteLexemeButton;
    private javax.swing.JButton viewPanelRenameLexeme;
    private javax.swing.JComboBox<String> workingLanguageComboBox;
    private javax.swing.JLabel workingLanguageLabel;
    private javax.swing.JComboBox<String> workingLexiconComboBox;
    private javax.swing.JLabel workingLexiconLabel;
    // End of variables declaration//GEN-END:variables
}
