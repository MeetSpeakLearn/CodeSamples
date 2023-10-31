/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import static net.meetspeaklearn.grammarmanager.PartOfSpeech.NUMBER;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;
import java.lang.ArrayIndexOutOfBoundsException;
import java.util.ArrayList;

/**
 *
 * @author steve
 */
public class Language extends GrammarObject {
    public enum LANGUAGE_CODE {
        UNKNOWN("unknown"),
        ENGLISH("en"),
        DEUTSCH("de"),
        FRANCAISE("fr");
        private String type;
        
        LANGUAGE_CODE(String type) {
            this.type = type;
        }
        
        public String toString() {
            return this.type;
        }
        
        public String toHumanReadableString() {
            switch (this) {
                case UNKNOWN: return "unknown";
                case ENGLISH: return "English";
                case DEUTSCH: return "Deutsch";
                case FRANCAISE: return "Française";
                default: return "unknown";
            }
        }
        
        public static String[] valuesAsString() {
            LANGUAGE_CODE[] valuesArray = LANGUAGE_CODE.values();
            int count = valuesArray.length;
            String[] valuesAsStrings = new String[count];
            
            for (int i = 0; i < count; i++)
                valuesAsStrings[i] = valuesArray[i].toString();
            
            return valuesAsStrings;
        }
        
        public static int indexOf(LANGUAGE_CODE target) {
            LANGUAGE_CODE[] valuesArray = LANGUAGE_CODE.values();
            int count = valuesArray.length;
            
            for (int i = 0; i < count; i++)
                if (valuesArray[i] == target) return i;
            
            return -1;
        }
    }
    
    public static final LANGUAGE_CODE lookupLanguageCode (String codeAsString) {
        if (codeAsString.compareToIgnoreCase("unknown") == 0) return LANGUAGE_CODE.UNKNOWN;
        if (codeAsString.compareToIgnoreCase("en") == 0) return LANGUAGE_CODE.ENGLISH;
        if (codeAsString.compareToIgnoreCase("de") == 0) return LANGUAGE_CODE.DEUTSCH;
        if (codeAsString.compareToIgnoreCase("fr") == 0) return LANGUAGE_CODE.FRANCAISE;

        return LANGUAGE_CODE.UNKNOWN;
    }
    
    public static enum GENDER_DIVISION_SYSTEM_TYPE {
        NONE("None"),
        MASCULINE_FEMININE("Masculine/Feminine"),
        MASCULINE_FEMININE_NEUTER("Masculine/Feminine/Neuter"),
        ANIMATE_INANIMATE("Animate/Inanimate"),
        COMMON_NEUTER("Common/Neuter");
        
        private String type;
        
        GENDER_DIVISION_SYSTEM_TYPE(String type) {
            this.type = type;
        }
        
        public String toString() {
            return this.type;
        }
        
        public static String[] valuesAsString() {
            GENDER_DIVISION_SYSTEM_TYPE[] valuesArray = GENDER_DIVISION_SYSTEM_TYPE.values();
            int count = valuesArray.length;
            String[] valuesAsStrings = new String[count];
            
            for (int i = 0; i < count; i++)
                valuesAsStrings[i] = valuesArray[i].toString();
            
            return valuesAsStrings;
        }
    }
    
    public static final GENDER_DIVISION_SYSTEM_TYPE lookupGenderSystemType (String typeAsString) {
        if (typeAsString.compareToIgnoreCase("unknown") == 0) return GENDER_DIVISION_SYSTEM_TYPE.NONE;
        if (typeAsString.compareToIgnoreCase("Masculine/Feminine") == 0) return GENDER_DIVISION_SYSTEM_TYPE.MASCULINE_FEMININE;
        if (typeAsString.compareToIgnoreCase("Masculine/Feminine/Neuter") == 0) return GENDER_DIVISION_SYSTEM_TYPE.MASCULINE_FEMININE_NEUTER;
        if (typeAsString.compareToIgnoreCase("Animate/Inanimate") == 0) return GENDER_DIVISION_SYSTEM_TYPE.ANIMATE_INANIMATE;
        if (typeAsString.compareToIgnoreCase("Common/Neuter") == 0) return GENDER_DIVISION_SYSTEM_TYPE.COMMON_NEUTER;
        return GENDER_DIVISION_SYSTEM_TYPE.NONE;
    }
    
    public enum GRAMMATICAL_NUMBER_SYSTEM_TYPE {
        NONE("None"),
        SINGULAR_PLURAL("Singular/Plural"),
        SINGULATIVE_COLLECTIVE("Singulative/Collective"),
        SINGULAR_DUAL_PLURAL("Singular/Dual/Plural"),
        SINGULAR_DUAL_TRIAL_PLURAL("Singular/Dual/Trial/Plural"),
        SINGULAR_DUAL_TRIAL_QUADRAL_PLURAL("Singular/Dual/Trial/Quadral/Plural");
        
        private String type;
        
        GRAMMATICAL_NUMBER_SYSTEM_TYPE(String type) {
            this.type = type;
        }
        
        public String toString() {
            return this.type;
        }
        
        public static String[] valuesAsString() {
            GRAMMATICAL_NUMBER_SYSTEM_TYPE[] valuesArray = GRAMMATICAL_NUMBER_SYSTEM_TYPE.values();
            int count = valuesArray.length;
            String[] valuesAsStrings = new String[count];
            
            for (int i = 0; i < count; i++)
                valuesAsStrings[i] = valuesArray[i].toString();
            
            return valuesAsStrings;
        }
    }
    
    public enum BRANCHING_TYPE {
        UNKNOWN("unknown"),
        LEFT("left-branching"),
        RIGHT("right-branching");
        private String type;
        
        BRANCHING_TYPE(String type) {
            this.type = type;
        }
        
        public String toString() {
            return this.type;
        }
    }
    
    public enum HEAD_DIRECTIONALITY_TYPE {
        UNKNOWN("unknown"),
        HEAD_INITIAL("head-initial"),
        HEAD_FINAL("head-final");
        private String type;
        
        HEAD_DIRECTIONALITY_TYPE(String type) {
            this.type = type;
        }
        
        public String toString() {
            return this.type;
        }
    }
    
    private LANGUAGE_CODE languageCode; // = LANGUAGE_CODE.UNKNOWN;
    private String name = null;
    private GENDER_DIVISION_SYSTEM_TYPE genderDivisionType = GENDER_DIVISION_SYSTEM_TYPE.NONE;
    private GRAMMATICAL_NUMBER_SYSTEM_TYPE grammaticalNumberSystemType;
    private ArrayList<String> adjectivalCatgeoriesByPrecedence;
    
    public static Language decode(XMLDecoder decoder) {
        return (Language) GrammarObject.decode(decoder);
    }
    
    public static void testSerializationToXML() {
        Language english = new Language(LANGUAGE_CODE.ENGLISH, GENDER_DIVISION_SYSTEM_TYPE.MASCULINE_FEMININE_NEUTER);
        String testFile = "language_test.xml";
        
        XMLEncoder encoder=null;
        
        try {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(testFile)));
        } catch (FileNotFoundException fileNotFound1) {
            System.err.println("testSerializationToXML(): Failed to create: " + testFile);
            return;
        }
        
        english.encode(encoder);
        encoder.close();
        
        XMLDecoder decoder = null;
        
        try {
            decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(testFile)));
        } catch (FileNotFoundException fileNotFound2) {
            System.err.println("testSerializationToXML(): Failed to create: " + testFile);
        }
        
        System.out.println("testSerializationToXML(): Succeeded!");
    }
    
    public Language() {
        this.goClass = GO_CLASS.LANGUAGE;
    }
    
    public Language(LANGUAGE_CODE languageCode) {
        this.goClass = GO_CLASS.LANGUAGE;
        this.languageCode = languageCode;
    }
    
    public Language(LANGUAGE_CODE languageCode, GENDER_DIVISION_SYSTEM_TYPE genderDivisionType) {
        this.goClass = GO_CLASS.LANGUAGE;
        this.languageCode = languageCode;
        this.genderDivisionType = genderDivisionType;
    }
    
    public Language(LANGUAGE_CODE languageCode,
            GENDER_DIVISION_SYSTEM_TYPE genderDivisionType,
            GRAMMATICAL_NUMBER_SYSTEM_TYPE grammaticalNumberSystemType) {
        this.goClass = GO_CLASS.LANGUAGE;
        this.languageCode = languageCode;
        this.genderDivisionType = genderDivisionType;
        this.grammaticalNumberSystemType = grammaticalNumberSystemType;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public LANGUAGE_CODE getLanguageCode() {
        return languageCode;
    }
    
    public void setLanguageCode(LANGUAGE_CODE languageCode) {
        this.languageCode = languageCode;
    }
    
    public String getLanguageCodeAsString() {
        return languageCode.toString();
    }
    
    public void setLanguageCodeAsString(String lcName) {
        languageCode = lookupLanguageCode(lcName);
    }
    
    public GENDER_DIVISION_SYSTEM_TYPE getGenderDivisionType() {
        return genderDivisionType;
    }
    
    public void setGenderDivisionType(GENDER_DIVISION_SYSTEM_TYPE gds) {
        genderDivisionType = gds;
    }
    
    public String getGenderDivisionTypeAsString() {
        return genderDivisionType.toString();
    }
    
    public void setGenderDivisionTypeAsString(String gdsName) {
        genderDivisionType = lookupGenderSystemType(gdsName);
    }

    public GRAMMATICAL_NUMBER_SYSTEM_TYPE getGrammaticalNumberSystemType() {
        return grammaticalNumberSystemType;
    }

    public void setGrammaticalNumberSystemType(GRAMMATICAL_NUMBER_SYSTEM_TYPE grammaticalNumberSystemType) {
        this.grammaticalNumberSystemType = grammaticalNumberSystemType;
    }

    public ArrayList<String> getAdjectivalCatgeoriesByPrecedence() {
        return adjectivalCatgeoriesByPrecedence;
    }

    public void setAdjectivalCatgeoriesByPrecedence(ArrayList<String> adjectivalCatgeoriesByPrecedence) {
        this.adjectivalCatgeoriesByPrecedence = adjectivalCatgeoriesByPrecedence;
    }
    
    public void addAdjectivalCategories(String[] rankedByPrecedence) {
        if (adjectivalCatgeoriesByPrecedence == null) {
            adjectivalCatgeoriesByPrecedence = new ArrayList<String>();
        } else {
            adjectivalCatgeoriesByPrecedence.clear();
        }
        
        for (String category : rankedByPrecedence) {
            adjectivalCatgeoriesByPrecedence.add(category);
        }
    }
    
    public String toString() {
        String checkedName = (name != null) ? name : "Unassigned";
        String checkedLanguageCode = getLanguageCodeAsString();
        String checkedGenderDivisionType = getGenderDivisionTypeAsString();
        
        return "Language:{name=\"" + checkedName +"\", languageCode="
                + checkedLanguageCode + ", genderDivisionType=" + checkedGenderDivisionType
                + "}";
    }
}
