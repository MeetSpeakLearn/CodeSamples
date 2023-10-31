/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import java.util.ArrayList;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;
import java.lang.ArrayIndexOutOfBoundsException;

/**
 *
 * @author steve
 */
public class WorkingDataManager {
    public static WorkingDataManager currentWorkingDataManager = null;
    public static Language.LANGUAGE_CODE workingLanguage = Language.LANGUAGE_CODE.ENGLISH;
    public static Lexicon workingLexicon = null;
    
    
    public static final String LANGUAGES_PATH = "languages.xml";
    
    public static final String LANGUAGE_ALREADY_EXISTS = "Language already exists.";
    public static final String LANGUAGE_ADDED = "Language added.";
    public static final String LANGUAGE_REPLACED = "Language replaced.";
    
    public static final String LANGUAGES_SAVED = "Languages saved.";
    public static final String LANGUAGES_SAVE_FAILED = "Failed to save languages.";
    
    public static final String LANGUAGES_LOADED = "Languages loaded.";
    public static final String LANGUAGES_LOAD_FAILED = "Failed to load languages.";
    
    
    public static final String LEXICA_PATH = "lexica.xml";
    
    public static final String LEXICON_ALREADY_EXISTS = "Lexicon already exists.";
    public static final String LEXICON_ADDED = "Lexicon added.";
    public static final String LEXICON_REPLACED = "Lexicon replaced.";
    
    public static final String LEXICA_SAVED = "Lexica saved.";
    public static final String LEXICA_SAVE_FAILED = "Failed to save lexica.";
    
    public static final String LEXICA_LOADED = "Lexica loaded.";
    public static final String LEXICA_LOAD_FAILED = "Failed to load lexica.";
    
    private ArrayList<Language> languages = null;
    private ArrayList<Lexicon> lexica = null;
    
    public WorkingDataManager() {
        if (currentWorkingDataManager == null) {
            currentWorkingDataManager = this;
        }
        
        languages = new ArrayList<Language>();
        lexica = new ArrayList<Lexicon>();
    }
    
    // Languages
    
    public Language GetLanguage(Language.LANGUAGE_CODE code) {
        if (languages == null) return null;
        
        for (Language current : languages)
            if (current.getLanguageCode() == code)
                return current;
        
        return null;
    }
    
    public String AddLanguage(Language language) {
        int count = languages.size();
        Language current = null;
        
        for (int i = 0; i < count; i++) {
            current = languages.get(i);
            
            if (current.getLanguageCode() == language.getLanguageCode()) {
                return LANGUAGE_ALREADY_EXISTS;
            }
        }
        
        languages.add(language);
        
        return LANGUAGE_ADDED;
    }
    
    public String AddOrReplaceLanguage(Language language) {
        int count = languages.size();
        Language current = null;
        
        for (int i = 0; i < count; i++) {
            current = languages.get(i);
            
            if (current.getLanguageCode() == language.getLanguageCode()) {
                languages.set(i, language);
                return LANGUAGE_REPLACED;
            }
        }
        
        languages.add(language);
        
        return LANGUAGE_ADDED;
    }
    
    public String LoadLanguages() {
        XMLDecoder decoder = null;
        
        try {
            decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(LANGUAGES_PATH)));
        } catch (FileNotFoundException fileNotFound2) {
            System.err.println("LoadLanguages(): Failed to create: " + LANGUAGES_PATH);
            return LANGUAGES_LOAD_FAILED;
        }
        
        try {
            Language current = null;
            
            while (true) {
                current = (Language) decoder.readObject();
                System.out.println("Adding language: " + current);
                languages.add(current);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            
        }
        
        decoder.close();
        
        return LANGUAGES_LOADED;
    }
    
    public String SaveLanguages() {
        XMLEncoder encoder=null;
        
        try {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(LANGUAGES_PATH)));
        } catch (FileNotFoundException fileNotFound1) {
            System.err.println("SaveLanguages(): Failed to create: " + LANGUAGES_PATH);
            return LANGUAGES_SAVE_FAILED;
        }
        
        int languageCount = languages.size();
        
        for (int i = 0; i < languageCount; i++) {
            languages.get(i).encode(encoder);
        }
        
        encoder.close();
        
        return LANGUAGES_SAVED;
    }
    
    // Lexica
    
    public String AddLexicon(Lexicon lexicon) {
        int count = lexica.size();
        Lexicon current = null;
        
        for (int i = 0; i < count; i++) {
            current = lexica.get(i);
            
            if ((current.getLanguageCode() == lexicon.getLanguageCode())
                    && (current.getName().compareToIgnoreCase(lexicon.getName()) == 0)) {
                return LEXICON_ALREADY_EXISTS;
            }
        }
        
        lexica.add(lexicon);
        
        return LEXICON_ADDED;
    }
    
    public String AddOrReplaceLexicon(Lexicon lexicon) {
        int count = lexica.size();
        Lexicon current = null;
        
        for (int i = 0; i < count; i++) {
            current = lexica.get(i);
            
            if ((current.getLanguageCode() == lexicon.getLanguageCode())
                    && (current.getName().compareToIgnoreCase(lexicon.getName()) == 0)) {
                lexica.set(i, lexicon);
                return LEXICON_REPLACED;
            }
        }
        
        lexica.add(lexicon);
        
        return LEXICON_ADDED;
    }
    
    public String LoadLexica() {
        XMLDecoder decoder = null;
        
        try {
            decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(LEXICA_PATH)));
        } catch (FileNotFoundException fileNotFound2) {
            System.err.println("LoadLexica(): Failed to create: " + LEXICA_PATH);
            return LEXICA_LOAD_FAILED;
        }
        
        try {
            Lexicon current = null;
            
            while (true) {
                current = (Lexicon) decoder.readObject();
                System.out.println("Adding lexicon: " + current);
                lexica.add(current);
                workingLexicon = current;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            
        }
        
        decoder.close();
        
        return LEXICA_LOADED;
    }
    
    public String SaveLexica() {
        XMLEncoder encoder=null;
        
        try {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(LEXICA_PATH)));
        } catch (FileNotFoundException fileNotFound1) {
            System.err.println("SaveLexica(): Failed to create: " + LEXICA_PATH);
            return LEXICA_SAVE_FAILED;
        }
        
        int lexiconCount = lexica.size();
        
        for (int i = 0; i < lexiconCount; i++) {
            lexica.get(i).encode(encoder);
        }
        
        encoder.close();
        
        return LEXICA_SAVED;
    }
    
    public String[] lexicaNames() {
        int count = lexica.size();
        String[] result = new String[count];
        
        for (int i = 0; i < count; i++)
            result[i] = lexica.get(i).getName();
        
        return result;
    }
    
    public String[] lexicaNames(Language.LANGUAGE_CODE code) {
        int count = 0;
        int i = 0;
        
        for (Lexicon lex : lexica) {
            if (lex.getLanguageCode() == code) count++;
        }
        
        String[] result = new String[count];
        
        for (Lexicon lex : lexica) {
            if (lex.getLanguageCode() == code) result[i++] = lex.getName();
        }
        
        return result;
    }
    
    public Lexicon lexicon(String name) {
        for (Lexicon lex : lexica)
            if (lex.getName().compareToIgnoreCase(name) == 0)
                return lex;
        
        return null;
    }
}
