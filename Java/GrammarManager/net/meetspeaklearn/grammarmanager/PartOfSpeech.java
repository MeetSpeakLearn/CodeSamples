/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import static net.meetspeaklearn.grammarmanager.Language.GRAMMATICAL_NUMBER_SYSTEM_TYPE;

import java.beans.XMLDecoder;
import java.util.ArrayList;

/**
 *
 * @author steve
 */
public class PartOfSpeech extends GrammarObject {
    protected String baseForm;
    protected ArrayList<POSDefinition> definitions;
    
    public static enum NUMBER {
        UNKNOWN("unknown"),
        SINGULAR("singular"),
        DUAL("dual"),
        TRIAL("trial"),
        QUADRAL("quadral"),
        PAUCAL("paucal"),
        PLURAL("plural"),
        COLLECTIVE("collective");
        private String type;
        
        NUMBER(String type) {
            this.type = type;
        }
        
        public String toString() {
            return this.type;
        }
        
        public String toHumanReadableString() {
            switch (this) {
                case UNKNOWN: return "unknown";
                case SINGULAR: return "singular";
                case DUAL: return "dual";
                case TRIAL: return "trial";
                case QUADRAL: return "quadral";
                case PAUCAL: return "paucal";
                case PLURAL: return "plural";
                case COLLECTIVE: return "collective";
                default: return "unknown";
            }
        }
        
        public static String[] valuesAsString() {
            PartOfSpeech.NUMBER[] valuesArray = PartOfSpeech.NUMBER.values();
            int count = valuesArray.length;
            String[] valuesAsStrings = new String[count];
            
            for (int i = 0; i < count; i++)
                valuesAsStrings[i] = valuesArray[i].toString();
            
            return valuesAsStrings;
        }
        
        public static int indexOf(PartOfSpeech.NUMBER target) {
            PartOfSpeech.NUMBER[] valuesArray = PartOfSpeech.NUMBER.values();
            int count = valuesArray.length;
            
            for (int i = 0; i < count; i++)
                if (valuesArray[i] == target) return i;
            
            return -1;
        }
    }
    
    public static NUMBER[] getGrammaticalNumbers(GRAMMATICAL_NUMBER_SYSTEM_TYPE systemType) {
        NUMBER[] result;
        
        switch (systemType) {
            case NONE:
                result = new NUMBER[0];
                break;
            case SINGULAR_PLURAL:
                result = new NUMBER[2];
                result[0] = NUMBER.SINGULAR;
                result[1] = NUMBER.PLURAL;
                break;
            case SINGULATIVE_COLLECTIVE:
                result = new NUMBER[2];
                result[0] = NUMBER.SINGULAR;
                result[1] = NUMBER.COLLECTIVE;
                break;
            case SINGULAR_DUAL_PLURAL:
                result = new NUMBER[3];
                result[0] = NUMBER.SINGULAR;
                result[1] = NUMBER.DUAL;
                result[2] = NUMBER.PLURAL;
                break;
            case SINGULAR_DUAL_TRIAL_PLURAL:
                result = new NUMBER[4];
                result[0] = NUMBER.SINGULAR;
                result[1] = NUMBER.DUAL;
                result[2] = NUMBER.TRIAL;
                result[3] = NUMBER.PLURAL;
                break;
            case SINGULAR_DUAL_TRIAL_QUADRAL_PLURAL:
                result = new NUMBER[5];
                result[0] = NUMBER.SINGULAR;
                result[1] = NUMBER.DUAL;
                result[2] = NUMBER.TRIAL;
                result[3] = NUMBER.QUADRAL;
                result[4] = NUMBER.PLURAL;
                break;
            default:
                result = new NUMBER[2];
                result[0] = NUMBER.SINGULAR;
                result[1] = NUMBER.PLURAL;
                break;
        }
        
        return result;
    }
    
    public PartOfSpeech() {
        goClass = GrammarObject.GO_CLASS.PART_OF_SPEECH;
    }
    
    public String getBaseForm() {
        return this.baseForm;
    }
    
    public void setBaseForm(String baseForm) {
        this.baseForm = baseForm;
    }

    public ArrayList<POSDefinition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(ArrayList<POSDefinition> definitions) {
        this.definitions = definitions;
    }
    
    public String getDefinitionsAsString() {
        if (definitions == null) return "";
        if (definitions.isEmpty()) return "";
        
        ArrayList<String> defsAsString = new ArrayList<String>();
        
        definitions.forEach( (d) -> { defsAsString.add(d.toString()); });
        
        return String.join(";", defsAsString.toArray(new String[0]));
    }
    
    public static PartOfSpeech decode(XMLDecoder decoder) {
        return (PartOfSpeech) GrammarObject.decode(decoder);
    }
    
    public String getDeclination(NUMBER number) {
        return "Override me";
    }
}
