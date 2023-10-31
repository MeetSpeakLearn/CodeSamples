/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import static net.meetspeaklearn.grammarmanager.Language.lookupLanguageCode;
import java.beans.XMLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import javafx.util.Pair;

/**
 *
 * @author steve
 */
public class Lexicon extends GrammarObject {
    public final static String COULD_NOT_RENAME = "Could not rename. Name already exists!";
    
    private String name;
    private Language.LANGUAGE_CODE languageCode;
    private PartOfSpeech.NUMBER[] grammaticalNumbers;
    private HashMap<String, Lex> stringToLexMap;
    private AdjectivalCategoryManager adjCatManager;
    
    public Lexicon() {
        this.goClass = GrammarObject.GO_CLASS.LEXICON;
    }
    
    public static Lexicon decode(XMLDecoder decoder) {
        return (Lexicon) GrammarObject.decode(decoder);
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Language.LANGUAGE_CODE getLanguageCode() {
        return this.languageCode;
    }
    
    public void setLanguageCode(Language.LANGUAGE_CODE languageCode) {
        this.languageCode = languageCode;
    }
    
    public String getLanguageCodeAsString() {
        return languageCode.toString();
    }
    
    public void setLanguageCodeAsString(String lcName) {
        languageCode = lookupLanguageCode(lcName);
    }

    public PartOfSpeech.NUMBER[] getGrammaticalNumbers() {
        return grammaticalNumbers;
    }

    public void setGrammaticalNumbers(PartOfSpeech.NUMBER[] grammaticalNumbers) {
        this.grammaticalNumbers = grammaticalNumbers;
    }
    
    public HashMap<String, Lex> getStringToLexMap() {
        return this.stringToLexMap;
    }
    
    public void setStringToLexMap(HashMap<String, Lex> value) {
        this.stringToLexMap = value;
    }

    public AdjectivalCategoryManager getAdjCatManager() {
        return adjCatManager;
    }

    public void setAdjCatManager(AdjectivalCategoryManager adjCatManager) {
        this.adjCatManager = adjCatManager;
    }
    
    public Lex instantiateLex() {
        Lex newLex = (Lex) GrammarObject.instantiate(GrammarObject.GO_CLASS.LEX);
        
        newLex.setLexicon(this);
        newLex.setLanguageCode(languageCode);
        newLex.setAbstraction(false);
        
        return newLex;
    }
    
    public Lex instantiateLex(String lexeme) {
        Lex newLex = instantiateLex();
        
        newLex.setLexeme(lexeme);
        
        return newLex;
    }
    
    public Lex intern(String key, Lex value) {
        if (key == null) {
            System.err.println("Lexicon.intern(): key is null.");
            return null;
        }
        if (value == null) {
            System.err.println("Lexicon.intern(): value is null.");
            return null;
        }
        
        if (stringToLexMap == null) {
             stringToLexMap = new HashMap<String, Lex>();
        }
        
        Lex internedValue = stringToLexMap.get(key);
        
        System.out.println("Lexicon.intern(): interned value is " + ((internedValue != null) ? "not null." : "null."));
        
        if (internedValue != null) return internedValue;
        
        stringToLexMap.put(key, value);
        
        return value;
    }
    
    public Lex get(String key) {
        if (stringToLexMap == null) return null;
        
        return stringToLexMap.get(key);
    }
    
    public Lex remove(String key) {
        if (stringToLexMap == null) return null;
        
        Lex internedValue = stringToLexMap.get(key);
        
        if (internedValue != null) {
            stringToLexMap.remove(key);
        }
        
        return internedValue;
    }
    
    public String rename(Lex lexeme, String newName) {
        String oldName = lexeme.getLexeme();
        
        if (oldName.compareTo(newName) == 0) return null;
        
        if (stringToLexMap.containsKey(newName))
            return COULD_NOT_RENAME;
        
        stringToLexMap.remove(oldName);
        lexeme.setLexeme(newName);
        
        stringToLexMap.put(newName, lexeme);
        
        return null;        
    }
    
    public boolean isInterned(String key) {
        if (stringToLexMap == null) {
            System.err.append("isInterned(): stringToLexMap is null.");
            return false;
        }
        
        return stringToLexMap.containsKey(key);
    }
    
    public String[] allLexemesAsStrings() {
        if (stringToLexMap == null) return new String[0];
        
        int count = stringToLexMap.size();
        String[] result;
        
        result = stringToLexMap.keySet().toArray(new String[0]);
        Arrays.sort(result, new IgnoreCaseStringComparator());
        
        return result;
    }
    
    public void repairSementicNetwork() {
        for (Lex lexeme : stringToLexMap.values()) {
            System.out.println("Cleaning " + lexeme);
            lexeme.cleanSementicNetwork();
        }
        for (Lex lexeme : stringToLexMap.values()) {
            System.out.println("Rebuilding " + lexeme);
            lexeme.rebuildSementicNetwork();
        }
    }
    
    public void repairAdjectivalCategories() {
        if (adjCatManager == null) return;
        
        ArrayList<Pair<Lex, Adjective>> shitlist = new ArrayList<Pair<Lex, Adjective>>();
        
        for (Lex lexeme : stringToLexMap.values()) {
            ArrayList<Adjective> adjectives = lexeme.getAdjectives();
            
            for (Adjective adj : adjectives) {
                AdjectivalCategory category = adj.getCategory();
                if (category != null) {
                    adjCatManager.mergeIfPossible(category);
                } else {
                    System.out.println("Adj " + adj.getLexeme().getLexeme() + " has no category.");
                    shitlist.add(new Pair<Lex, Adjective>(lexeme, adj));
                }
            }
        }
            
        for (Pair<Lex, Adjective> gonner : shitlist)
            gonner.getKey().destroyAdjective(gonner.getValue());
    }
    
    public ArrayList<Lex> unusedLexemes() {
        ArrayList<Lex> result = new ArrayList<Lex>();
        
        for (Lex lexeme : stringToLexMap.values()) {
            ArrayList<PartOfSpeech> partsOfSpeech = lexeme.getPartsOfSpeech();
            
            if ((partsOfSpeech != null) ? (partsOfSpeech.size() == 0) : true)
                result.add(lexeme);
        }
        
        Collections.sort(result, Lex.getComparator());
        
        return result;
    }
    
    public ArrayList<Adjective> orphanedAdjectives() {
        ArrayList<Adjective> orphans = new ArrayList<Adjective>();
        
        for (Lex lexeme : stringToLexMap.values()) {
            ArrayList<Adjective> adjectives = lexeme.getAdjectives();
            
            for (Adjective adj : adjectives) {
                if (adj.getCategory() == null) {
                    orphans.add(adj);
                } else {
                    AdjectivalCategory cat = adj.getCategory();
                    HashSet<Adjective> catAdjs = cat.getAdjectives();
                    
                    if (catAdjs == null) {
                        orphans.add(adj);
                    } else if (! catAdjs.contains(adj)) {
                        orphans.add(adj);
                    }
                }
            }
        }
        
        Collections.sort(orphans, Adjective.getComparator());
        
        return orphans;
    }
    
    public ArrayList<Noun> getAllNouns() {
        ArrayList<Noun> allNouns = new ArrayList<Noun>();
        
        for (String key : stringToLexMap.keySet()) {
            Lex lexeme = stringToLexMap.get(key);
            ArrayList<PartOfSpeech> partsOfSpeech = lexeme.getPartsOfSpeech();
            
            if (partsOfSpeech == null) continue;
            
            for (PartOfSpeech pos : partsOfSpeech) {
                if (pos.isA(GO_CLASS.NOUN)) {
                    allNouns.add((Noun) pos);
                }
            }
        }
        
        return allNouns;
    }
    
    public ArrayList<Noun> getAllNounsWithinAcademicLevelRange(AcademicLevel lowerLimit, AcademicLevel upperLimit) {
        ArrayList<Noun> allNouns = new ArrayList<Noun>();
        
        for (String key : stringToLexMap.keySet()) {
            Lex lexeme = stringToLexMap.get(key);
            AcademicLevel lexemeLevel = lexeme.getLevel();
            int relativeToLower = lexemeLevel.compareTo(lowerLimit);
            int relativeToUpper = lexemeLevel.compareTo(upperLimit);
            
            if ((relativeToLower >= 0) && (relativeToUpper <= 0)) {
                ArrayList<PartOfSpeech> partsOfSpeech = lexeme.getPartsOfSpeech();

                if (partsOfSpeech == null) continue;

                for (PartOfSpeech pos : partsOfSpeech) {
                    if (pos.isA(GO_CLASS.NOUN)) {
                        allNouns.add((Noun) pos);
                    }
                }
            }
        }
        
        return allNouns;
    }
    
    public ArrayList<Lex> getAllLexemes(AcademicLevel level) {
        ArrayList<Lex> collection = new ArrayList<Lex>();
        
        for (String key : stringToLexMap.keySet()) {
            Lex lexeme = stringToLexMap.get(key);
            if (lexeme.getLevel() == level)
                collection.add(lexeme);
        }
        
        return collection;
    }
    
    // public HashSet<Adjective> getAllIn
    
    /*
    public void makeEverythingSingular() {
        for (Lex lex : stringToLexMap.values()) {
            for (PartOfSpeech pos : lex.getAdjectives())
                pos.setNumber(PartOfSpeech.NUMBER.SINGULAR);
        }
    }
    */
    
    //*
    public void makeAllNounsCustom () {
        for (Noun n : getAllNouns()) {
            n.setAdjectivalCategoriesType(Noun.ADJECTIVAL_CATEGORIES_TYPE.CUSTOM);
            
            n.setCustomAdjectivalCategorySet(null);
        }
    }
    public void makeAllNounsCustomWithExclusions () {
        for (Noun n : getAllNouns()) {
            n.setAdjectivalCategoriesType(Noun.ADJECTIVAL_CATEGORIES_TYPE.CUSTOM_WITH_EXCLUSIONS);
            
            n.setCustomAdjectivalCategorySet(null);
        }
    }
    //*/
    
    public void associateAllLexemesWithLexicon() {
        for (Lex lex : stringToLexMap.values()) {
            lex.setLexicon(this);
        }
    }
    
    public String toString() {
        return "Lexicon:{name=\"" + name + "\", languageCode=" + getLanguageCodeAsString() + "}";
    }
}
