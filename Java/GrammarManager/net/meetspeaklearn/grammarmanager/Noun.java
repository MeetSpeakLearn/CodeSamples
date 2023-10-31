/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JPanel;

/**
 *
 * @author steve
 */
public class Noun extends ProtoNoun implements SemanticNode, ITreeNode {
    private static NounComparator comparator = new NounComparator();
    
    private static class NounComparator implements Comparator<Noun> {
        private NounComparator() {}
        public int compare(Noun o1, Noun o2) {
            return o1.getLexeme().getLexeme().compareToIgnoreCase(o2.getLexeme().getLexeme());
        }
    }
    
    public static NounComparator getComparator() {
        return comparator;
    }
    
    public enum COUNTABILITY_TYPE {
        UNKNOWN("Unknown"),
        COUNTABLE("Countable"),
        UNCOUNTABLE("Uncountable");
        private String type;
        
        COUNTABILITY_TYPE(String type) {
            this.type = type;
        }
        
        public static String[] valuesAsStrings() {
            COUNTABILITY_TYPE[] valuesArray = COUNTABILITY_TYPE.values();
            int count = valuesArray.length;
            String[] valuesAsStrings = new String[count];

            for (int i = 0; i < count; i++)
                valuesAsStrings[i] = valuesArray[i].toString();

            return valuesAsStrings;
        }
        
        public static COUNTABILITY_TYPE fromString(String name) {
            if (name.compareToIgnoreCase("Unknown") == 0) return COUNTABILITY_TYPE.UNKNOWN;
            if (name.compareToIgnoreCase("Countable") == 0) return COUNTABILITY_TYPE.COUNTABLE;
            if (name.compareToIgnoreCase("Uncountable") == 0) return COUNTABILITY_TYPE.UNCOUNTABLE;
            
            return COUNTABILITY_TYPE.UNKNOWN;
        }
        
        public String toString() {
            return this.type;
        }
    }
    public enum NOUN_TYPE {
        UNKNOWN("Unknown"),
        PROPER("Proper"),
        CONCRETE("Concrete"),
        ABSTRACT("Abstract");
        private String type;
        
        NOUN_TYPE(String type) {
            this.type = type;
        }
        
        public static String[] valuesAsStrings() {
            NOUN_TYPE[] valuesArray = NOUN_TYPE.values();
            int count = valuesArray.length;
            String[] valuesAsStrings = new String[count];

            for (int i = 0; i < count; i++)
                valuesAsStrings[i] = valuesArray[i].toString();

            return valuesAsStrings;
        }
        
        public static NOUN_TYPE fromString(String name) {
            if (name.compareToIgnoreCase("Unknown") == 0) return NOUN_TYPE.UNKNOWN;
            if (name.compareToIgnoreCase("Proper") == 0) return NOUN_TYPE.PROPER;
            if (name.compareToIgnoreCase("Concrete") == 0) return NOUN_TYPE.CONCRETE;
            if (name.compareToIgnoreCase("Abstract") == 0) return NOUN_TYPE.ABSTRACT;
            
            return NOUN_TYPE.UNKNOWN;
        }
        
        public String toString() {
            return this.type;
        }
    }
    
    public enum ADJECTIVAL_CATEGORIES_TYPE { ALL, CUSTOM, CUSTOM_WITH_EXCLUSIONS, DO_NOT_INHERIT }
    
    // Instance variables
    
    protected Gender gender;
    protected COUNTABILITY_TYPE countability;
    protected NOUN_TYPE nounType;
    protected boolean alwaysPlural;
    protected ADJECTIVAL_CATEGORIES_TYPE adjectivalCategoriesType;
    protected String customPlural;
    protected HashSet<SemanticNode> thingsIAm;
    protected HashSet<SemanticNode> thingsIHave;
    protected HashSet<SemanticNode> myAbilities;
    protected HashSet<SemanticNode> myModifiers;
    protected HashSet<SemanticNode> thingsIGeneralize;
    protected HashSet<AdjectivalCategory> customAdjectivalCategorySet;
    protected AdjectiveSet customAdjectiveSet;
    protected AdjectiveSet customAdjectiveExclusionSet;
    
    public Noun() {
        goClass = GrammarObject.GO_CLASS.NOUN;
        adjectivalCategoriesType = ADJECTIVAL_CATEGORIES_TYPE.CUSTOM_WITH_EXCLUSIONS;
    }
    
    public Noun(Lex lexeme) {
        super(lexeme);
        goClass = GrammarObject.GO_CLASS.NOUN;
        adjectivalCategoriesType = ADJECTIVAL_CATEGORIES_TYPE.CUSTOM_WITH_EXCLUSIONS;
        this.baseForm = lexeme.getLexeme();
        this.gender = Gender.UNKNOWN;
    }
    
    public Noun(Lex lexeme, Gender gender) {
        this(lexeme);
        goClass = GrammarObject.GO_CLASS.NOUN;
        adjectivalCategoriesType = ADJECTIVAL_CATEGORIES_TYPE.CUSTOM_WITH_EXCLUSIONS;
        this.gender = gender;
    }
    
    public Noun(Lex lexeme, String baseForm, Gender gender) {
        this(lexeme);
        goClass = GrammarObject.GO_CLASS.NOUN;
        adjectivalCategoriesType = ADJECTIVAL_CATEGORIES_TYPE.CUSTOM_WITH_EXCLUSIONS;
        this.baseForm = baseForm;
        this.gender = gender;
    }
    
    public void setToDefault() {
        this.gender = Gender.NEUTER;
        this.countability = COUNTABILITY_TYPE.COUNTABLE;
        this.nounType = NOUN_TYPE.CONCRETE;
        this.alwaysPlural = false;
    }
    
    public Gender getGender() {
        return this.gender;
    }
    
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    public COUNTABILITY_TYPE getCountability() {
        return this.countability;
    }
    
    public void setCountability(COUNTABILITY_TYPE value) {
        this.countability = value;
    }
    
    public boolean isCountNoun() {
        return this.countability == COUNTABILITY_TYPE.COUNTABLE;
    }
    
    public boolean isMassNoun() {
        return this.countability == COUNTABILITY_TYPE.UNCOUNTABLE;
    }
    
    public NOUN_TYPE getNounType() {
        return this.nounType;
    }
    
    public void setNounType(NOUN_TYPE value) {
        this.nounType = value;
    }
    
    public boolean isProper() {
        return this.nounType == NOUN_TYPE.PROPER;
    }
    
    public boolean isCommon() {
        return this.nounType != NOUN_TYPE.PROPER;
    }
    
    public boolean isAbstract() {
        return this.nounType == NOUN_TYPE.ABSTRACT;
    }
    
    public boolean isConcrete() {
        return this.nounType == NOUN_TYPE.CONCRETE;
    }
    
    public boolean isAlwaysPlural() {
        return this.alwaysPlural;
    }
    
    public void setAlwaysPlural(boolean value) {
        this.alwaysPlural = value;
    }
    
    public HashSet<SemanticNode> getThingsIAm() {
        return this.thingsIAm;
    }
    
    public void setThingsIAm(HashSet<SemanticNode> value) {
        this.thingsIAm = value;
    }
    
    public HashSet<SemanticNode> getThingsIHave() {
        return this.thingsIHave;
    }
    
    public void setThingsIHave(HashSet<SemanticNode> value) {
        this.thingsIHave = value;
    }
    
    public HashSet<SemanticNode> getMyAbilities() {
        return this.myAbilities;
    }
    
    public void setMyAbilities(HashSet<SemanticNode> value) {
        this.myAbilities = value;
    }
    
    public HashSet<SemanticNode> getMyModifiers() {
        return this.myModifiers;
    }
    
    public void setMyModifiers(HashSet<SemanticNode> value) {
        this.myModifiers = value;
    }

    public HashSet<SemanticNode> getThingsIGeneralize() {
        return thingsIGeneralize;
    }

    public void setThingsIGeneralize(HashSet<SemanticNode> thingsIGeneralize) {
        this.thingsIGeneralize = thingsIGeneralize;
    }

    public ADJECTIVAL_CATEGORIES_TYPE getAdjectivalCategoriesType() {
        return adjectivalCategoriesType;
    }

    public void setAdjectivalCategoriesType(ADJECTIVAL_CATEGORIES_TYPE adjectivalCategoriesType) {
        this.adjectivalCategoriesType = adjectivalCategoriesType;
    }

    public HashSet<AdjectivalCategory> getCustomAdjectivalCategorySet() {
        return customAdjectivalCategorySet;
    }

    public void setCustomAdjectivalCategorySet(HashSet<AdjectivalCategory> customAdjectivalCategorySet) {
        this.customAdjectivalCategorySet = customAdjectivalCategorySet;
    }

    public HashSet<Adjective> getCustomAdjectiveSet() {
        return customAdjectiveSet;
    }

    public void setCustomAdjectiveSet(AdjectiveSet customAdjectiveSet) {
        this.customAdjectiveSet = customAdjectiveSet;
    }

    public AdjectiveSet getCustomAdjectiveExclusionSet() {
        return customAdjectiveExclusionSet;
    }

    public void setCustomAdjectiveExclusionSet(AdjectiveSet customAdjectiveExclusionSet) {
        this.customAdjectiveExclusionSet = customAdjectiveExclusionSet;
    }

    public String getCustomPlural() {
        return customPlural;
    }

    public void setCustomPlural(String customPlural) {
        customPlural = customPlural.trim();
        
        if (customPlural.compareTo(English.pluralizeNoun(customPlural)) != 0) {
            this.customPlural = customPlural;
        } else if (customPlural.compareTo("") == 0) {
            this.customPlural = null;
        } else {
            this.customPlural = null;
        }
    }
    
    public HashSet<AdjectivalCategory> getInheritedCustomAdjectivalCategories() {
        HashSet<AdjectivalCategory> unionOfCategories = new HashSet<AdjectivalCategory>();
        ArrayList<SemanticNode> nodesToSearch = new ArrayList<SemanticNode>();
        HashSet<SemanticNode> nodesIveSearched = new HashSet<SemanticNode>();
        SemanticNode currentNode;
        Noun currentNodeAsNoun;
        nodesToSearch.add(this);
        
        while (! nodesToSearch.isEmpty()) {
            currentNode = nodesToSearch.get(0);
            nodesToSearch.remove(currentNode);
            
            if (nodesIveSearched.contains(currentNode)) continue;
            
            nodesIveSearched.add(currentNode);
            
            currentNodeAsNoun = (Noun) currentNode;
            HashSet<AdjectivalCategory> currentSet = currentNodeAsNoun.getCustomAdjectivalCategorySet();
            
            if (currentSet != null) {
                unionOfCategories.addAll(currentSet);
            }
            
            HashSet<SemanticNode> thingsCurrentIs = currentNodeAsNoun.getThingsIAm();
            
            if (thingsCurrentIs != null) {
                nodesToSearch.addAll(currentNodeAsNoun.getThingsIAm());
            }
        }
        
        return unionOfCategories;
    }
    
    public HashSet<Adjective> getInheritedCustomAdjectivesThroughCategories() {
        HashSet<AdjectivalCategory> inheritedCategories = getInheritedCustomAdjectivalCategories();
        HashSet<Adjective> inheritedAdjectives = new HashSet<Adjective>();
        
        for (AdjectivalCategory cat : inheritedCategories) {
            HashSet<Adjective> adjectives = cat.getAdjectives();
            
            if (adjectives == null) continue;
            
            for (Adjective adj : adjectives) {
                inheritedAdjectives.add(adj);
            }
        }
        
        return inheritedAdjectives;
    }
    
    public void addCustomApplicableAdjectivalCategory(AdjectivalCategory category) {
        System.err.println("addCustomApplicableAdjectivalCategory(): category=" + category.toString());
        if (adjectivalCategoriesType == ADJECTIVAL_CATEGORIES_TYPE.ALL) {
            if (customAdjectivalCategorySet != null)
                customAdjectivalCategorySet.clear();
            else
                customAdjectivalCategorySet = new HashSet<AdjectivalCategory>();
            
            customAdjectivalCategorySet.add(category);
            adjectivalCategoriesType = ADJECTIVAL_CATEGORIES_TYPE.CUSTOM_WITH_EXCLUSIONS;
        } else if ((adjectivalCategoriesType == ADJECTIVAL_CATEGORIES_TYPE.CUSTOM)
                || (adjectivalCategoriesType == ADJECTIVAL_CATEGORIES_TYPE.CUSTOM_WITH_EXCLUSIONS)) {
            Lexicon lexicon = WorkingDataManager.workingLexicon;
            AdjectivalCategoryManager adjCatManager = lexicon.getAdjCatManager();
                
            if (customAdjectivalCategorySet == null) {
                customAdjectivalCategorySet = new HashSet<AdjectivalCategory>();
            }
            
            customAdjectivalCategorySet.add(category);
        } else {
            adjectivalCategoriesType = ADJECTIVAL_CATEGORIES_TYPE.CUSTOM_WITH_EXCLUSIONS;
            
            Lexicon lexicon = WorkingDataManager.workingLexicon;
            AdjectivalCategoryManager adjCatManager = lexicon.getAdjCatManager();
                
            if (customAdjectivalCategorySet == null) {
                customAdjectivalCategorySet = new HashSet<AdjectivalCategory>();
            }
            
            customAdjectivalCategorySet.add(category);
        }
    }
    
    public void removeCustomApplicableAdjectivalCategory(AdjectivalCategory category) {
        if (customAdjectivalCategorySet != null) {
            customAdjectivalCategorySet.remove(category);
        }
    }
    
    public ArrayList<AdjectivalCategory> getApplicableAdjectivalCategories() {
        ArrayList<AdjectivalCategory> result;
        
        switch (adjectivalCategoriesType) {
            case ALL:
                Lexicon lexicon = WorkingDataManager.workingLexicon;
                AdjectivalCategoryManager adjCatManager = lexicon.getAdjCatManager();
                return adjCatManager.getCategoriesByPrecedence();
            case CUSTOM_WITH_EXCLUSIONS:
            case CUSTOM:
                HashSet<AdjectivalCategory> inheritedCategories = getInheritedCustomAdjectivalCategories();
                result = new ArrayList<AdjectivalCategory>();
                result.addAll(inheritedCategories);
                return result;
            case DO_NOT_INHERIT:
                return new ArrayList<AdjectivalCategory>();
            default:
                return new ArrayList<AdjectivalCategory>();
        }
    }
    
    public ArrayList<AdjectivalCategory> getApplicableAdjectivalCategoriesAsSortedArrayList() {
        ArrayList<AdjectivalCategory> categories = getApplicableAdjectivalCategories();
        int categoryCount = categories.size();
        Lexicon lexicon = WorkingDataManager.workingLexicon;
        AdjectivalCategoryManager adjCatManager = lexicon.getAdjCatManager();
        ArrayList<AdjectivalCategory> byPrecedence = adjCatManager.getCategoriesByPrecedence();
        int[] indexArray = new int[categoryCount];
        int i = 0;
        
        for (AdjectivalCategory cat : categories) {
            indexArray[i++] = byPrecedence.indexOf(cat);
        }
        
        Arrays.sort(indexArray);
        
        ArrayList<AdjectivalCategory> result = new ArrayList<AdjectivalCategory>();
        
        for (i = 0; i < categoryCount; i++)
            result.add(byPrecedence.get(indexArray[i]));
        
        return result;
    }
    
    public String[] getApplicableAdjectivalCategoryNamesAsSortedArray() {
        ArrayList<AdjectivalCategory> categories = getApplicableAdjectivalCategories();
        int categoryCount = categories.size();
        Lexicon lexicon = WorkingDataManager.workingLexicon;
        AdjectivalCategoryManager adjCatManager = lexicon.getAdjCatManager();
        ArrayList<AdjectivalCategory> byPrecedence = adjCatManager.getCategoriesByPrecedence();
        int[] indexArray = new int[categoryCount];
        int i = 0;
        
        for (AdjectivalCategory cat : categories) {
            indexArray[i++] = byPrecedence.indexOf(cat);
        }
        
        Arrays.sort(indexArray);
        
        String[] result = new String[categoryCount];
        
        for (i = 0; i < categoryCount; i++)
            result[i] = byPrecedence.get(indexArray[i]).getName();
        
        return result;
    }
    
    public String[] getSelectedAdjectivalCategoryNamesAsSortedArray() {
        ArrayList<AdjectivalCategory> categories = new ArrayList<AdjectivalCategory>();
        
        if (customAdjectivalCategorySet != null) {
            categories.addAll(customAdjectivalCategorySet);
        } else {
            customAdjectivalCategorySet = new HashSet<AdjectivalCategory>();
            System.err.println("getSelectedAdjectivalCategoryNamesAsSortedArray(): customAdjectivalCategorySet is null");
        }
        
        int categoryCount = categories.size();
        Lexicon lexicon = WorkingDataManager.workingLexicon;
        AdjectivalCategoryManager adjCatManager = lexicon.getAdjCatManager();
        ArrayList<AdjectivalCategory> byPrecedence = adjCatManager.getCategoriesByPrecedence();
        int[] indexArray = new int[categoryCount];
        int i = 0;
        
        for (AdjectivalCategory cat : categories) {
            indexArray[i++] = byPrecedence.indexOf(cat);
        }
        
        Arrays.sort(indexArray);
        
        String[] result = new String[categoryCount];
        
        for (i = 0; i < categoryCount; i++)
            result[i] = byPrecedence.get(indexArray[i]).getName();
        
        return result;
    }
    
    public HashSet<Adjective> getAllInheritedAdjectives() {
        return getAllInheritedAdjectives(new HashSet<Noun>(), false, AcademicLevel.UNASSIGNED, AcademicLevel.ADVANCED);
    }
    
    public HashSet<Adjective> getAllInheritedAdjectives(AcademicLevel higherBound) {
        return getAllInheritedAdjectives(new HashSet<Noun>(), false, AcademicLevel.BEGINNER, higherBound);
    }
    
    public HashSet<Adjective> getAllInheritedAdjectivesIgnoringThisNounsExclusions() {
        return getAllInheritedAdjectives(new HashSet<Noun>(), true, AcademicLevel.UNASSIGNED, AcademicLevel.ADVANCED);
    }
    
    public HashSet<Adjective> getAllInheritedAdjectives(HashSet<Noun> visitedNouns,
            boolean ignoreThisNounsExclusions,
            AcademicLevel lowerBound, AcademicLevel higherBound) {
        if (visitedNouns.contains(this))
            return new HashSet<Adjective>();
        
        System.out.println(lexeme.getLexeme() + ".getAllInheritedAdjectives()");
        
        visitedNouns.add(this);
        
        HashSet<Adjective> myAdjectives = new HashSet<Adjective>();
        Lexicon myLexicon = lexeme.getLexicon();
        AdjectivalCategoryManager categoryManager = myLexicon.getAdjCatManager();
        boolean exclusions = false;
        
        switch (adjectivalCategoriesType) {
            case ALL:
                if (categoryManager == null) break;
                return categoryManager.getAllAdjectivesWithinAcademicRange(lowerBound, higherBound);
            case CUSTOM_WITH_EXCLUSIONS:
                exclusions = true;
            case CUSTOM:
                myAdjectives = new HashSet<Adjective>();      
                
                if (customAdjectiveSet != null)
                    myAdjectives.addAll(customAdjectiveSet);
                
                if (customAdjectivalCategorySet != null) {
                    for (AdjectivalCategory cat : customAdjectivalCategorySet) {
                        myAdjectives.addAll(cat.getAdjectivesWithinAcademicRange(lowerBound, higherBound));
                    }
                }
                
                if (thingsIAm != null) {
                    for (SemanticNode node : thingsIAm) {
                        Noun parent = (Noun) node;
                        myAdjectives.addAll(parent.getAllInheritedAdjectives(visitedNouns, false, lowerBound, higherBound));
                    }
                }
                
                if (! ignoreThisNounsExclusions) {
                    if (exclusions) {
                        if (customAdjectiveExclusionSet != null) {
                            myAdjectives.removeAll(customAdjectiveExclusionSet);
                        }
                    }
                }
                
                return myAdjectives;
            case DO_NOT_INHERIT:
                myAdjectives = new HashSet<Adjective>();        
                myAdjectives.addAll(Adjective.withinAcedemicRange(customAdjectiveSet, lowerBound, lowerBound));
        }
        
        if (myAdjectives == null) {
            return new HashSet<Adjective>();
        } else {
            return myAdjectives;
        }
    }
    
    public void associate_with(PartOfSpeech pos) {
        // No op when the node and the part of speech are one and the same.
    }
    
    public PartOfSpeech get_part_of_speech() {
        return this;
    }
    
    public void is_a(SemanticNode object) {
        System.out.println("Noun: " + lexeme.getLexeme() + " is_a " + object);
        if (! object.is_an_object()) return;
        
        if (thingsIAm == null) thingsIAm = new HashSet<SemanticNode>();
        
        thingsIAm.add(object);

        Noun objectAsNoun = (Noun) object;
        
        if (objectAsNoun.thingsIGeneralize == null) {
            objectAsNoun.thingsIGeneralize = new HashSet<SemanticNode>();
        }
        
        System.out.println("Adding generalization.");
        objectAsNoun.thingsIGeneralize.add((SemanticNode) this);
    }
    
    public void has(SemanticNode object) {
        if (! object.is_an_object()) return;
        
        if (thingsIHave == null) thingsIHave = new HashSet<SemanticNode>();
        if (thingsIHave.contains(object)) return;
        
        thingsIHave.add(object);
    }
    
    public void is_able_to(SemanticNode action) {
        if (! action.is_an_action()) return;
        
        if (myAbilities == null) myAbilities = new HashSet<SemanticNode>();
        if (myAbilities.contains(action)) return;
        
        myAbilities.add(action);
    }
    
    public void is_modified_by(SemanticNode adjectiveOrAdverb) {
        if (! adjectiveOrAdverb.is_an_adjective()) return;
        
        if (myModifiers == null) myModifiers = new HashSet<SemanticNode>();
        if (myModifiers.contains(adjectiveOrAdverb)) return;
        
        myModifiers.add(adjectiveOrAdverb);
    }
    
    public void remove_is_a(SemanticNode object) {
        if (! object.is_an_object()) return;
        if (thingsIAm == null) return;
        
        Noun objectAsNoun = (Noun) object;
        
        if (objectAsNoun.thingsIGeneralize != null) {
            objectAsNoun.thingsIGeneralize.remove((SemanticNode) this);
            
            if (objectAsNoun.thingsIGeneralize.size() == 0) {
                objectAsNoun.thingsIGeneralize = null;
            }
        }
        
        thingsIAm.remove(object);
        
        if (thingsIAm.size() == 0) {
            thingsIAm = null;
        }
    }
    
    public boolean is_an_object() {
        return true;
    }
    
    public boolean is_an_action() {
        return false;
    }
    
    public boolean is_an_adjective() {
        return false;
    }
    
    public boolean is_an_adverb() {
        return false;
    }
    
    public SemanticNode[] get_things_I_am() {
        if (thingsIAm == null) return new SemanticNode[0];
        
        HashSet<SemanticNode> visitedNodes = new HashSet<SemanticNode>();
        visitedNodes.add(this);
        
        // thingsIAm
        HashSet<SemanticNode> ancestors = (HashSet<SemanticNode>) thingsIAm;
        ArrayList<SemanticNode> copyOfAncestors = new ArrayList<SemanticNode>();
        copyOfAncestors.addAll(ancestors);
        
        while (! copyOfAncestors.isEmpty()) {
            SemanticNode currentAncestor = copyOfAncestors.get(0);
            
            copyOfAncestors.remove(currentAncestor);
            
            if (! visitedNodes.contains(currentAncestor)) {
                visitedNodes.add(currentAncestor);
                HashSet<SemanticNode> ancestorsOfCurrentAncestor = ((Noun) currentAncestor).thingsIAm;
                
                if (ancestorsOfCurrentAncestor != null)
                    copyOfAncestors.addAll(ancestorsOfCurrentAncestor);
            }
        }
        
        return visitedNodes.toArray(new SemanticNode[0]);
    }
    
    public SemanticNode[] get_things_I_have() {
        SemanticNode[] theThingsIAm = get_things_I_am();
        int countOfTheThingsIAm = theThingsIAm.length;
        SemanticNode aThingIAm;
        HashSet<SemanticNode> theThingsIHave = new HashSet<SemanticNode>();
        
        if (thingsIHave != null) {
            theThingsIHave.addAll(thingsIHave);
        }

        for (int i = 0; i < countOfTheThingsIAm; i++) {
            aThingIAm = theThingsIAm[i];
            
            if (((Noun) aThingIAm).thingsIHave != null) {
                theThingsIHave.addAll(((Noun) aThingIAm).thingsIHave);
            }
        }
        
        return theThingsIHave.toArray(new SemanticNode[0]);
    }
    
    public SemanticNode[] get_my_abilities() {
        SemanticNode[] theThingsIAm = get_things_I_am();
        int countOfTheThingsIAm = theThingsIAm.length;
        SemanticNode aThingIAm;
        HashSet<SemanticNode> theThingsIHave = new HashSet<SemanticNode>();
        
        if (myAbilities != null) {
            theThingsIHave.addAll(myAbilities);
        }

        for (int i = 0; i < countOfTheThingsIAm; i++) {
            aThingIAm = theThingsIAm[i];
            
            if (((Noun) aThingIAm).myAbilities != null) {
                theThingsIHave.addAll(((Noun) aThingIAm).myAbilities);
            }
        }
        
        return theThingsIHave.toArray(new SemanticNode[0]);
    }
    
    public SemanticNode[] get_my_modifiers() {
        SemanticNode[] theThingsIAm = get_things_I_am();
        int countOfTheThingsIAm = theThingsIAm.length;
        SemanticNode aThingIAm;
        HashSet<SemanticNode> TheModifiersIHave = new HashSet<SemanticNode>();
        
        if (myModifiers != null) {
            TheModifiersIHave.addAll(myModifiers);
        }

        for (int i = 0; i < countOfTheThingsIAm; i++) {
            aThingIAm = theThingsIAm[i];
            
            if (((Noun) aThingIAm).myModifiers != null) {
                TheModifiersIHave.addAll(((Noun) aThingIAm).myModifiers);
            }
        }
        
        return TheModifiersIHave.toArray(new SemanticNode[0]);
    }
    
    public SemanticNode[] get_things_I_specialize() {
        if (thingsIAm == null) return new SemanticNode[0];
        return thingsIAm.toArray(new SemanticNode[0]);
    }
    
    public SemanticNode[] get_thing_I_generalize() {
        if (thingsIGeneralize == null) return new SemanticNode[0];
        return thingsIGeneralize.toArray(new SemanticNode[0]);
    }
    
    public void cleanSementicNetwork() {
        if (thingsIAm != null) thingsIAm.clear();
    }
    
    public void rebuildSementicNetwork() {
        if (thingsIGeneralize == null) return;
        
        for (SemanticNode node : thingsIGeneralize) {
            Noun noun = (Noun) node;
            System.out.println("Repairing: " + noun);
            noun.thingsIAm.add(this);
        }
    }
    
    public Object getContent() {
        return this;
    }
    
    public ArrayList<ITreeNode> getChildNodes() {
        ArrayList<ITreeNode> thingsIGeneralize = new ArrayList<ITreeNode>();
        
        if (this.thingsIGeneralize == null) return thingsIGeneralize;
        
        for (SemanticNode n : this.thingsIGeneralize) {
            thingsIGeneralize.add((ITreeNode) n);
        }
        
        return thingsIGeneralize;
    }
    
    public ArrayList<ITreeNode> getParentNodes() {
        ArrayList<ITreeNode> thingsISpecialize = new ArrayList<ITreeNode>();
        
        if (this.thingsIAm == null) return thingsISpecialize;
        
        for (SemanticNode n : this.thingsIAm) {
            thingsISpecialize.add((ITreeNode) n);
        }
        
        return thingsISpecialize;
    }
 
    public String getDeclination(NUMBER number) {
        switch (number) {
            case SINGULAR:
                return lexeme.getLexeme();
            case PLURAL:
                if (customPlural != null) return customPlural;
                if (this.alwaysPlural) return lexeme.getLexeme();
                if (this.countability == COUNTABILITY_TYPE.UNCOUNTABLE) {
                    return lexeme.getLexeme();
                } else {
                    return English.pluralizeNoun(lexeme.getLexeme());
                }
            default:
                return lexeme.getLexeme();
        }
    }
    
    public String toString() {
        return lexeme.getLexeme();
    }
}
