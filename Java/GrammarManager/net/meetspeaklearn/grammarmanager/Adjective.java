/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import java.util.Comparator;
import java.util.HashSet;
import net.meetspeaklearn.util.*;

/**
 *
 * @author steve
 */
public class Adjective extends PartOfSpeech implements SemanticNode {
    private static AdjectiveComparator comparator = new AdjectiveComparator();
    
    private Lex lexeme;
    private AdjectivalCategory category;
    private PropertySet properties;
    
    public static enum ADJECTIVE_TYPE {
        UNKNOWN("Unkown"),
        ARTICLE("Article"),
        DEMONSTRATIVE("Demonstrative"),
        POSSESSIVE("Possessive"),
        QUANTIFIER("Quantifier"),
        DETERMINER("Determiner"),
        GENERIC("Generic");
        
        private String type;
        
        ADJECTIVE_TYPE(String type) {
            this.type = type;
        }
        
        public String toString() {
            return this.type;
        }
        
        public static String[] valuesAsString() {
            ADJECTIVE_TYPE[] valuesArray = ADJECTIVE_TYPE.values();
            int count = valuesArray.length;
            String[] valuesAsStrings = new String[count];
            
            for (int i = 0; i < count; i++)
                valuesAsStrings[i] = valuesArray[i].toString();
            
            return valuesAsStrings;
        }
        
        public static ADJECTIVE_TYPE fromString(String string) {
            for (ADJECTIVE_TYPE type : ADJECTIVE_TYPE.values()) {
                if (type.toString().compareTo(string) == 0) return type;
            }
            
            return ADJECTIVE_TYPE.GENERIC;
        }
    }
    
    public static enum QUANTIFIER_TYPE {
        UNKNOWN("Unknown"),
        COUNTABLE("Countable"),
        UNCOUNTABLE("Uncountable"),
        COUNTABLE_OR_UNCOUNTABLE("Countable or Uncountable");
        
        private String type;
        
        QUANTIFIER_TYPE(String type) {
            this.type = type;
        }
        
        public String toString() {
            return this.type;
        }
        
        public static String[] valuesAsString() {
            QUANTIFIER_TYPE[] valuesArray = QUANTIFIER_TYPE.values();
            int count = valuesArray.length;
            String[] valuesAsStrings = new String[count];
            
            for (int i = 0; i < count; i++)
                valuesAsStrings[i] = valuesArray[i].toString();
            
            return valuesAsStrings;
        }
        
        public static QUANTIFIER_TYPE fromString(String string) {
            for (QUANTIFIER_TYPE type : QUANTIFIER_TYPE.values()) {
                if (type.toString().compareTo(string) == 0) return type;
            }
            
            return QUANTIFIER_TYPE.UNKNOWN;
        }
    }
    
    public static enum PLURALITY {
        UNKNOWN("Unknown"),
        SINGULAR_ONLY("Singular Only"),
        PLURAL_ONLY("Plural Only"),
        SINGULAR_OR_PLURAL("Singular or Plural");
        
        private String type;
        
        PLURALITY(String type) {
            this.type = type;
        }
        
        public String toString() {
            return this.type;
        }
        
        public static String[] valuesAsString() {
            PLURALITY[] valuesArray = PLURALITY.values();
            int count = valuesArray.length;
            String[] valuesAsStrings = new String[count];
            
            for (int i = 0; i < count; i++)
                valuesAsStrings[i] = valuesArray[i].toString();
            
            return valuesAsStrings;
        }
        
        public static PLURALITY fromString(String string) {
            for (PLURALITY type : PLURALITY.values()) {
                if (type.toString().compareTo(string) == 0) return type;
            }
            
            return PLURALITY.UNKNOWN;
        }
    }
    
    public static class DummyAdjective extends Adjective {
        public DummyAdjective() {
        }
        
        public String getText() { return ""; }
    }
    
    public static AdjectiveComparator getComparator() {
        return comparator;
    }
    
    private static class AdjectiveComparator implements Comparator<Adjective> {
        private AdjectiveComparator() {}
        public int compare(Adjective o1, Adjective o2) {
            return o1.getLexeme().getLexeme().compareToIgnoreCase(o2.getLexeme().getLexeme());
        }
    }
    
    public static HashSet<Adjective> withinAcedemicRange(HashSet<Adjective> adjectives, AcademicLevel lowerBound, AcademicLevel upperBound) { //!!!
        HashSet<Adjective> result = new HashSet<Adjective>();
        
        for (Adjective adj : adjectives) {
            AcademicLevel level = adj.getLexeme().getLevel();
            
            if ((level.compareTo(lowerBound) >= 0) && (level.compareTo(upperBound) <= 0))
                result.add(adj);
        }
        
        return result;
    }
    
    public Adjective() {
        goClass = GrammarObject.GO_CLASS.ADJECTIVE;
        properties = new PropertySet();
    }
    
    public String getText() {
        if (lexeme == null)
            return "";
        else
            return lexeme.getLexeme();
    }

    public Lex getLexeme() {
        return lexeme;
    }

    public void setLexeme(Lex lexeme) {
        this.lexeme = lexeme;
    }

    public AdjectivalCategory getCategory() {
        return category;
    }

    public void setCategory(AdjectivalCategory category) {
        this.category = category;
    }

    public ADJECTIVE_TYPE getType() {
        if (category == null) return ADJECTIVE_TYPE.GENERIC;
        
        ADJECTIVE_TYPE type = ADJECTIVE_TYPE.GENERIC;
        AdjectivalCategory.TYPE acType = category.getType();
        
        switch (acType) {
            case UNKNOWN:
                type = ADJECTIVE_TYPE.UNKNOWN;
                break;
            case ARTICLE:
                type = ADJECTIVE_TYPE.ARTICLE;
                break;
            case DEMONSTRATIVE:
                type = ADJECTIVE_TYPE.DEMONSTRATIVE;
                break;
            case POSSESSIVE:
                type = ADJECTIVE_TYPE.POSSESSIVE;
                break;
            case QUANTIFIER:
                type = ADJECTIVE_TYPE.QUANTIFIER;
                break;
            case DETERMINER:
                type = ADJECTIVE_TYPE.DETERMINER;
                break;
            case GENERIC:
                type = ADJECTIVE_TYPE.GENERIC;
                break;
        }
        
        return type;
    }

    public PropertySet getProperties() {
        return properties;
    }

    public void setProperties(PropertySet properties) {
        this.properties = properties;
    }
    
    public QUANTIFIER_TYPE getQuantifierType() {
        try {
            QUANTIFIER_TYPE type = QUANTIFIER_TYPE.fromString((String) this.properties.getPropertyValue("QUANTIFIER_TYPE"));
            return type;
        } catch (net.meetspeaklearn.util.PropertySetException e) {
            return QUANTIFIER_TYPE.UNKNOWN;
        }
    }
    
    public void setQuantifierType(QUANTIFIER_TYPE value) {
        try {
            this.properties.setPropertyValue("QUANTIFIER_TYPE", value.toString());
        } catch (net.meetspeaklearn.util.PropertySetException e) {
            System.err.println("Error while trying to set QuantifierType");
        }
    }
    
    public PLURALITY getPlurality() {
        try {
            PLURALITY type = PLURALITY.fromString((String) this.properties.getPropertyValue("PLURALITY"));
            return type;
        } catch (net.meetspeaklearn.util.PropertySetException e) {
            return PLURALITY.SINGULAR_OR_PLURAL;
        }
    }
    
    public void setPlurality(PLURALITY value) {
        try {
            this.properties.setPropertyValue("PLURALITY", value.toString());
        } catch (net.meetspeaklearn.util.PropertySetException e) {
            System.err.println("Error while trying to set PluralityType");
        }
    }
    
    public void associate_with(PartOfSpeech pos) {
        // No op when the node and the part of speech are one and the same.
    }
    
    public PartOfSpeech get_part_of_speech() {
        return this;
    }
    
    public boolean isArticle() {
        if (category == null) return false;
        
        return (category.getType() == AdjectivalCategory.TYPE.ARTICLE);
    }
    
    public boolean isDeterminer() {
        if (category == null) return false;
        
        return (category.getType() == AdjectivalCategory.TYPE.DETERMINER);
    }
    
    public boolean isAdjective() {
        if (category == null) return false;
        
        return (category.getType() == AdjectivalCategory.TYPE.GENERIC);
    }
    
    public boolean conflicts(Adjective other) {
        if (category == null) return false;
        
        return category.conflicts(this, other);
    }
    
    public void addMutalConflict(Adjective other) {
        if (category == null) return;
        
        category.addMutalConflict(this, other);
    }
    
    public void removeMutalConflict(Adjective other) {
        if (category == null) return;
        
        category.removeMutalConflict(this, other);
    }
    
    public boolean mutuallyConflict(Adjective other) {
        if (category == null) return false;
        
        return category.mutuallyConflict(this, other);
    }
    
    public Adjective[] getConflictsAsArray() {
        if (category == null) return new Adjective[0];
        
        return category.getConflictsAsArray(this);
    }
    
    public void setConflictsFromArray(Adjective[] conflicts) {
        if (category == null) return;
        
        category.setConflictsFromArray(this, conflicts);
    }
    
    public Adjective[] getNonConflictsAsArray() {
        System.out.println("getNonConflictsAsArray(): category=" + ((category == null) ? "null" : category));
        if (category == null) return new Adjective[0];
        
        return category.getNonConflictsAsArray(this);
    }
    
    public boolean excludes(Adjective other) {
        if (category == null) return false;
        
        return category.excludes(other);
    }
    
    // Implementation of SemanticNode
    
    public void is_a(SemanticNode object) {
        // For now, this is a no-op.
        // Perhaps, in the future, I will add the "is_a_kind_of" relation,
        // in which case, we could have things like "Beautiful" is_a_kind_of
        // "complement".
    }
    
    public void has(SemanticNode object) {
        // No-op.
    }
    
    public void is_able_to(SemanticNode action) {
        // No-op.
    }
    
    public void is_modified_by(SemanticNode adjectiveOrAdverb) {
        // No-op for now, but we could consider allowing intensifiers
        // to modify adjectives. For example, "Ugly" is modified by "very".
    }
    
    public void remove_is_a(SemanticNode object) {
        // So long as is_a is a no-op, this is a no-op too.
    }
    
    public boolean is_an_object() {
        return false;
    }
    
    public boolean is_an_action() {
        return false;
    }
    
    public boolean is_an_adjective() {
        return true;
    }
    
    public boolean is_an_adverb() {
        return false;
    }
    
    public SemanticNode[] get_things_I_am() {
        return new SemanticNode[0];
    }
    
    public SemanticNode[] get_things_I_have() {
        return new SemanticNode[0];
    }
    
    public SemanticNode[] get_my_abilities() {
        return new SemanticNode[0];
    }
    public SemanticNode[] get_my_modifiers() {
        return new SemanticNode[0];
    }
    
    public SemanticNode[] get_things_I_specialize() {
        return new SemanticNode[0];
    }
    
    public SemanticNode[] get_thing_I_generalize() {
        return new SemanticNode[0];
    }
    
    public String toString() {
        String catAsString = (category == null) ? "null" : category.getName();
        return "adj: " + getText() + " of category " + catAsString;
    }
    
    public Gender getGender() {
        if (properties == null)  return Gender.UNKNOWN;
        
        try {
            String genderAsString = (String) properties.getPropertyValue("gender");
            Gender gender = Gender.fromString(genderAsString);
            return gender;
        } catch (PropertySetException pse) {
            System.err.println(pse.getMessage());
            return Gender.UNKNOWN;
        }
    }
    
    public void setGender(Gender gender) {
        if (properties == null)
            properties = new PropertySet();
        
        try {
            properties.setPropertyValue("gender", gender.toString());
        } catch (PropertySetException pse) {
            System.err.println(pse.getMessage());
        }
    }
    
    public String getDeclination(NUMBER number, Gender gender) {
        switch (getType()) {
            case GENERIC:
                return lexeme.getLexeme();
            case ARTICLE:
            case DEMONSTRATIVE:
                try {
                    if (properties != null) {
                        return (String) properties.getPropertyValue("plural");
                    }
                } catch (PropertySetException pse) {
                    pse.printStackTrace(System.err);
                }
                    
                return lexeme.getLexeme();
            case POSSESSIVE:
                try {
                    if (properties != null) {
                        PropertySet genderProperties = (PropertySet) properties.getPropertyValue("genderProperties");
                        Gender g = (Gender) genderProperties.getPropertyValue("gender");
                        PropertySet numberProperties = (PropertySet) genderProperties.getPropertyValue(number.toString());
                        
                        switch (g) {
                            case UNKNOWN: return (String) numberProperties.getPropertyValue(Gender.UNKNOWN.toString());
                            case NEUTER: return (String) numberProperties.getPropertyValue(Gender.NEUTER.toString());
                            case MASCULINE: return (String) numberProperties.getPropertyValue(Gender.MASCULINE.toString());
                            case FEMININE: return (String) numberProperties.getPropertyValue(Gender.FEMININE.toString());
                            default: return getText();
                        }
                    }
                } catch (PropertySetException pse) {
                    pse.printStackTrace(System.err);
                    return getText();
                }
            case QUANTIFIER:
                return lexeme.getLexeme();
        }
        
        return lexeme.getLexeme();
    }
}
