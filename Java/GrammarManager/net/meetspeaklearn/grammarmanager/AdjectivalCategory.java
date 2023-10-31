/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import static net.meetspeaklearn.grammarmanager.WorkingDataManager.currentWorkingDataManager;
import static net.meetspeaklearn.grammarmanager.WorkingDataManager.workingLexicon;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author steve
 */
public class AdjectivalCategory {
    public static enum TYPE {
        UNKNOWN("Unknown"),
        ARTICLE("Article"),
        DEMONSTRATIVE("Demonstrative"),
        POSSESSIVE("Possessive"),
        QUANTIFIER("Quantifier"),
        DETERMINER("Determiner"),
        GENERIC("Generic");
        
        private String type;
        
        TYPE(String type) {
            this.type = type;
        }
        
        public String toString() {
            return this.type;
        }
        
        public static TYPE fromString(String name) {
            if (name.compareTo("Unknown") == 0) return UNKNOWN;
            if (name.compareTo("Article") == 0) return ARTICLE;
            if (name.compareTo("Demonstrative") == 0) return DEMONSTRATIVE;
            if (name.compareTo("Possessive") == 0) return POSSESSIVE;
            if (name.compareTo("Quantifier") == 0) return QUANTIFIER;
            if (name.compareTo("Determiner") == 0) return DETERMINER;
            if (name.compareTo("Generic") == 0) return GENERIC;
            
            return UNKNOWN;
        }
        
        public static String[] valuesAsString() {
            TYPE[] valuesArray = TYPE.values();
            int count = valuesArray.length;
            String[] valuesAsStrings = new String[count];
            
            for (int i = 0; i < count; i++)
                valuesAsStrings[i] = valuesArray[i].toString();
            
            return valuesAsStrings;
        }
        
        public static int indexOf(TYPE target) {
            TYPE[] valuesArray = TYPE.values();
            int count = valuesArray.length;
            
            for (int i = 0; i < count; i++)
                if (valuesArray[i] == target) return i;
            
            return -1;
        }
    }
    public enum EXCLUSIVITY {
        UNKNOWN("Unknown"),
        EXCLUSIVE("Exclusive"),
        NONEXCLUSIVE("Non Exclusive");
        
        private String type;
        
        EXCLUSIVITY(String type) {
            this.type = type;
        }
        
        public String toString() {
            return this.type;
        }
        
        public static EXCLUSIVITY fromString(String name) {
            if (name.compareTo("Unknown") == 0) return UNKNOWN;
            if (name.compareTo("Exclusive") == 0) return EXCLUSIVE;
            if (name.compareTo("Non Exclusive") == 0) return NONEXCLUSIVE;
            
            return UNKNOWN;
        }
        
        public static String[] valuesAsString() {
            EXCLUSIVITY[] valuesArray = EXCLUSIVITY.values();
            int count = valuesArray.length;
            String[] valuesAsStrings = new String[count];
            
            for (int i = 0; i < count; i++)
                valuesAsStrings[i] = valuesArray[i].toString();
            
            return valuesAsStrings;
        }
        
        public static int indexOf(EXCLUSIVITY target) {
            EXCLUSIVITY[] valuesArray = EXCLUSIVITY.values();
            int count = valuesArray.length;
            
            for (int i = 0; i < count; i++)
                if (valuesArray[i] == target) return i;
            
            return -1;
        }
    }
    
    private String name;
    private TYPE type;
    private EXCLUSIVITY exclusivity;
    private HashSet<Adjective> adjectives;
    private HashMap<Adjective, HashSet<Adjective>> conflicts;
    private HashSet<AdjectivalCategory> exclusions;
    
    public AdjectivalCategory() {
        type = TYPE.GENERIC;
        exclusivity = EXCLUSIVITY.NONEXCLUSIVE;
    }
    
    public AdjectivalCategory(String name) {
        type = TYPE.GENERIC;
        exclusivity = EXCLUSIVITY.NONEXCLUSIVE;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashSet<Adjective> getAdjectives() {
        return adjectives;
    }
    
    public HashSet<Adjective> getAdjectivesWithinAcademicRange(AcademicLevel lowerBound, AcademicLevel upperBound) {
        HashSet<Adjective> result = new HashSet<Adjective>();
        
        if (adjectives == null) return result;
        
        for (Adjective adj : adjectives) {
            AcademicLevel level = adj.getLexeme().getLevel();
            
            if ((level.compareTo(lowerBound) >= 0) && (level.compareTo(upperBound) <= 0))
                result.add(adj);
        }
        
        return result;
    }

    public void setAdjectives(HashSet<Adjective> adjectives) {
        this.adjectives = adjectives;
    }
    
    public ArrayList<String> getAdjectiveNames() {
        ArrayList<String> result = new ArrayList<String>();
        if (adjectives == null) return result;
        
        for (Adjective adj : adjectives)
            result.add(adj.getLexeme().getLexeme());
        
        return result;        
    }

    public HashMap<Adjective, HashSet<Adjective>> getConflicts() {
        return conflicts;
    }

    public void setConflicts(HashMap<Adjective, HashSet<Adjective>> conflicts) {
        this.conflicts = conflicts;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public HashSet<AdjectivalCategory> getExclusions() {
        return exclusions;
    }

    public void setExclusions(HashSet<AdjectivalCategory> exclusions) {
        this.exclusions = exclusions;
    }

    public EXCLUSIVITY getExclusivity() {
        return exclusivity;
    }

    public void setExclusivity(EXCLUSIVITY exclusivity) {
        this.exclusivity = exclusivity;
    }
    
    public void removeCategory(Adjective adj) {
        ensureAdjectives();
        
        if (adjectives.contains(adj)) {
            if (conflicts != null) {
                for (Adjective sibling : conflicts.keySet()) {
                    HashSet<Adjective> conflictSet = conflicts.get(sibling);
                    
                    if (conflictSet.add(adj)) {
                        conflictSet.remove(adj);
                    }
                }
            }
            
            adjectives.remove(adj);
            adj.setCategory(null);
        }
    }
    
    public void setCategory(Adjective adj) {
        if (adj == null) return;
        
        ensureAdjectives();
        
        AdjectivalCategory adjCat = adj.getCategory();
        
        if (adjCat != null) {
            adjCat.removeCategory(adj);
        }
        
        adjectives.add(adj);
        adj.setCategory(this);
    }
    
    private void ensureAdjectives() {
        if (adjectives == null)
            adjectives = new HashSet<Adjective>();
    }
    
    private void ensureConflicts() {
        if (conflicts == null)
            conflicts = new HashMap<Adjective, HashSet<Adjective>>();
    }
    
    private void ensureExclusions() {
        if (exclusions == null)
            exclusions = new HashSet<AdjectivalCategory>();
    }
    
    public void addMutalConflict(Adjective adj1, Adjective adj2) {
        ensureConflicts();
        
        String str1 = (adj1 == null) ? "null" : adj1.toString();
        String str2 = (adj2 == null) ? "null" : adj2.toString();
        System.out.println("addMutalConflict(): adj1=" + str1 + ", adj2=" + str2);
        
        HashSet<Adjective> setOfConflicts =  conflicts.get(adj1);
        
        if (setOfConflicts == null) {
            conflicts.put(adj1, setOfConflicts = new HashSet<>());
        }
        
        setOfConflicts.add(adj2);
        
        setOfConflicts =  conflicts.get(adj2);
        
        if (setOfConflicts == null) {
            conflicts.put(adj2, setOfConflicts = new HashSet<>());
        }
        
        setOfConflicts.add(adj1);
    }
    
    public void removeMutalConflict(Adjective adj1, Adjective adj2) {
        ensureConflicts();
        
        HashSet<Adjective> setOfConflicts =  conflicts.get(adj1);
        
        if (setOfConflicts != null) {
            setOfConflicts.remove(adj2);
        }
        
        setOfConflicts =  conflicts.get(adj2);
        
        if (setOfConflicts != null) {
            setOfConflicts.remove(adj1);
        }
    }
    
    public boolean conflicts(Adjective adj1, Adjective adj2) {
        if (conflicts == null) return false;
        
        HashSet<Adjective> setOfConflicts =  conflicts.get(adj1);
        
        if (setOfConflicts == null) return false;
        
        return setOfConflicts.contains(adj2);
    }
    
    public boolean mutuallyConflict(Adjective adj1, Adjective adj2) {
        ensureConflicts();
        
        HashSet<Adjective> setOfConflicts = conflicts.get(adj1);
        
        if (setOfConflicts == null) return false;        
        if (! setOfConflicts.contains(adj2)) return false;
        
        setOfConflicts = conflicts.get(adj2);
        
        if (setOfConflicts == null) return false;        
        
        return setOfConflicts.contains(adj1);
    }
    
    public Adjective[] getConflictsAsArray(Adjective adj) {
        Adjective[] result = new Adjective[0];
        
        if (conflicts == null) return result;
        
        HashSet<Adjective> adjConflicts = conflicts.get(adj);
        
        if (adjConflicts == null) return result;
        if (adjConflicts.size() == 0) return result;
        
        return adjConflicts.toArray(result);
    }
    
    public void setConflictsFromArray(Adjective adj, Adjective[] adjConflicts) {
        if (conflicts == null) {
            conflicts = new HashMap<Adjective, HashSet<Adjective>>();
        }
            
        for (Adjective current : adjConflicts) {
            if (current != adj)
                if (conflicts.get(adj) == null) {
                    HashSet<Adjective> newSet = new HashSet<Adjective>();
                    newSet.add(current);
                    conflicts.put(adj, newSet);
                } else {
                    conflicts.get(adj).add(current);
                }
        }
    }
    
    public Adjective[] getNonConflictsAsArray(Adjective adj) {
        ensureConflicts();
        
        Adjective[] result = new Adjective[0];
        
        System.out.println("getNonConflictsAsArray(): adj=" + adj
                + ", adjectives=" + ((adjectives == null) ? "null" : adjectives)
                + " adjectives.size()=" + ((adjectives == null) ? "?" : adjectives.size()));
        
        if (adjectives == null) return result;
        if (adjectives.size() == 0) return result;

        ArrayList<Adjective> listOfAdjectives = new ArrayList<Adjective>();
        
        listOfAdjectives.addAll(adjectives);
        listOfAdjectives.remove(adj);
        
        ArrayList<Adjective> nonConflicts = new ArrayList<Adjective>();
        HashSet<Adjective> adjConflicts = conflicts.get(adj);
        
        if (adjConflicts == null) return listOfAdjectives.toArray(result);
        if (adjConflicts.size() == 0) return listOfAdjectives.toArray(result);
        
        for (Adjective current : listOfAdjectives) {
            if (! adjConflicts.contains(current))
                nonConflicts.add(current);
        }
        
        return nonConflicts.toArray(result);
    }
    
    public boolean excludes(Adjective adj) {
        if (exclusions == null) return false;
        
        AdjectivalCategory cat = adj.getCategory();
        
        if (cat == null) return false;
        
        return exclusions.contains(cat);
    }
    
    public boolean isOrphaned() {
        AdjectivalCategoryManager currentManager = workingLexicon.getAdjCatManager();
        ArrayList<AdjectivalCategory> categories = currentManager.getCategoriesByPrecedence();
        
        if (categories == null) return true;
        
        for (AdjectivalCategory cat : categories)
            if (cat == this) return false;
        
        return true;
    }
    
    public String toString() {
        return name;
    }
}
