/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;
import java.lang.ArrayIndexOutOfBoundsException;
import java.util.*;

/**
 *
 * @author steve
 */
public class Lex extends GrammarObject {
    private static Lex.LexComparator comparator = new Lex.LexComparator();
    private static HashMap<String, Lex> STRING_TO_LEX_MAP = new HashMap<String, Lex>();
    
    private String lexeme;
    private Lexicon lexicon;
    private Language.LANGUAGE_CODE languageCode;
    private boolean abstraction;
    private boolean dynamic;
    private AcademicLevel level;
    private ArrayList<PartOfSpeech> partsOfSpeech;
    
    public static Lex.LexComparator getComparator() {
        return comparator;
    }

    private static class LexComparator implements Comparator<Lex> {
        private LexComparator() {}
        public int compare(Lex o1, Lex o2) {
            return o1.getLexeme().compareTo(o2.getLexeme());
        }
    }
    
    public static String[] toString(Collection<Lex> lexemes) {
        int count = lexemes.size();
        String[] result = new String[count];
        int i = 0;
        
        for (Lex lexeme : lexemes) {
            result[i++] = lexeme.getLexeme();
        }
        
        return result;
    }
    
    public Lex() {
        goClass = GrammarObject.GO_CLASS.LEX;
        level = AcademicLevel.UNASSIGNED;
    }
    
    public Lex(Lexicon lexicon) {
        this();
        this.lexicon = lexicon;
    }
    
    public static Lex decode(XMLDecoder decoder) {
        return (Lex) GrammarObject.decode(decoder);
    }
    
    public String getLexeme() {
        return this.lexeme;
    }
    
    public void setLexeme(String value) {
        this.lexeme = value;
    }

    public Lexicon getLexicon() {
        return lexicon;
    }

    public void setLexicon(Lexicon lexicon) {
        this.lexicon = lexicon;
    }
    
    public Language.LANGUAGE_CODE getLanguageCode() {
        return this.languageCode;
    }
    
    public void setLanguageCode(Language.LANGUAGE_CODE value) {
        this.languageCode = value;
    }
    
    public boolean getAbstraction() {
        return this.abstraction;
    }
    
    public void setAbstraction(boolean value) {
        this.abstraction = value;
    }
    
    public boolean getDynamic() {
        return this.dynamic;
    }
    
    public void setDynamic(boolean value) {
        this.dynamic = value;
    }

    public AcademicLevel getLevel() {
        return level;
    }

    public void setLevel(AcademicLevel level) {
        this.level = level;
    }
    
    public ArrayList<PartOfSpeech> getPartsOfSpeech() {
        return this.partsOfSpeech;
    }
    
    public void setPartsOfSpeech(ArrayList<PartOfSpeech> value) {
        this.partsOfSpeech = value;
    }
    
    // Nouns
    
    public Noun getNoun() {
        if (partsOfSpeech == null) return null;
        
        for (PartOfSpeech pos : partsOfSpeech) {
            if (pos.isA(GrammarObject.GO_CLASS.NOUN)) return (Noun) pos;
        }
        
        return null;
    }
    
    public Noun createNoun() {
        Noun newNoun = (Noun) GrammarObject.instantiate(GO_CLASS.NOUN);
        
        newNoun.setLexeme(this);
        
        if (partsOfSpeech == null)
            partsOfSpeech = new ArrayList<PartOfSpeech>();
        
        partsOfSpeech.add(newNoun);
        
        return newNoun;
    }
    
    public Noun updateNoun(Noun noun) {
        Noun existingNoun = getNoun();
        
        if (existingNoun == null) {
            if (partsOfSpeech == null)
                partsOfSpeech = new ArrayList<PartOfSpeech>();
            
            partsOfSpeech.add(noun);
            noun.setLexeme(this);
            return noun;
        } else {
            existingNoun.setBaseForm(noun.getBaseForm());
            existingNoun.setGender(noun.getGender());
            return existingNoun;
        }
    }
    
    public Noun updateNoun(Gender gender) {
        Noun existingNoun = getNoun();
        
        if (existingNoun != null) {
            existingNoun.setGender(gender);
            return existingNoun;
        }
        
        return null;
    }
    
    public Noun updateNoun(String baseForm) {
        Noun existingNoun = getNoun();
        
        if (existingNoun != null) {
            existingNoun.setBaseForm(baseForm);
            return existingNoun;
        }
        
        return null;
    }
    
    // Adjectives
    
    public Adjective getAdjective() {
        if (partsOfSpeech == null) return null;
        
        for (PartOfSpeech pos : partsOfSpeech) {
            if (pos.isA(GrammarObject.GO_CLASS.ADJECTIVE)) return (Adjective) pos;
        }
        
        return null;
    }
    
    public void destroyAdjective(Adjective adj) {
        if (partsOfSpeech == null) return;
        
        // It's not enough to remove the adjective from parts of speech.
        // Check if the adjective has relationships with other adjectives
        // and remove those relationships.
        // Next, remove it from it's category.
        
        AdjectivalCategory category = adj.getCategory();
        
        if (category != null) {
            category.removeCategory(adj);
        }
        
        partsOfSpeech.remove(adj);
    }
    
    public ArrayList<Adjective> getAdjectives() {
        ArrayList<Adjective> result = new ArrayList<Adjective>();
        
        if (partsOfSpeech == null) return result;
        
        System.out.println("getAdjectives(): lex=" + this);
        
        for (PartOfSpeech pos : partsOfSpeech) {
            if (pos.isA(GrammarObject.GO_CLASS.ADJECTIVE)) {
                System.out.println("  found adj: " + pos);
                result.add((Adjective) pos);
            }
        }
        
        return result;
    }
    
    public Adjective createAdjective() {
        Adjective newAdjective = (Adjective) GrammarObject.instantiate(GO_CLASS.ADJECTIVE);
        
        newAdjective.setLexeme(this);
        
        if (partsOfSpeech == null)
            partsOfSpeech = new ArrayList<PartOfSpeech>();
        
        partsOfSpeech.add(newAdjective);
        
        return newAdjective;
    }
    
    public void removeAdjective(Adjective adj) {
        if (partsOfSpeech != null) {
            partsOfSpeech.remove(adj);
        }
        
        AdjectivalCategory cat = adj.getCategory();
        
        if (cat != null) {
            cat.removeCategory(adj);
        }
    }
    
    public Adjective updateAdjective(Adjective adjective) {
        Adjective existingAdjective = getAdjective();
        
        if (existingAdjective == null) {
            if (partsOfSpeech == null)
                partsOfSpeech = new ArrayList<PartOfSpeech>();
            
            partsOfSpeech.add(adjective);
            adjective.setLexeme(this);
            return adjective;
        } else {
            existingAdjective.setBaseForm(adjective.getBaseForm());
            return existingAdjective;
        }
    }
    
    public Adjective updateAdjective(String baseForm) {
        Adjective existingAdjective = getAdjective();
        
        if (existingAdjective != null) {
            existingAdjective.setBaseForm(baseForm);
            return existingAdjective;
        }
        
        return null;
    }
    
    public void cleanSementicNetwork() {
        if (partsOfSpeech == null) return;
        
        for (PartOfSpeech partOfSpeech : partsOfSpeech) {
            if (partOfSpeech.isA(GrammarObject.GO_CLASS.NOUN)) {
                ((Noun) partOfSpeech).cleanSementicNetwork();
            }
        }
    }
    
    public void rebuildSementicNetwork() {
        if (partsOfSpeech == null) return;
        
        for (PartOfSpeech partOfSpeech : partsOfSpeech) {
            if (partOfSpeech.isA(GrammarObject.GO_CLASS.NOUN)) {
                System.out.println("Rebuilding: " + partOfSpeech);
                ((Noun) partOfSpeech).rebuildSementicNetwork();
            }
        }
    }
    
    public String toString() {
        return "Lex:{lexeme=\"" + lexeme + "\", languageCode=" + languageCode
                + ", abstraction=" + abstraction + "}";
    }
}
