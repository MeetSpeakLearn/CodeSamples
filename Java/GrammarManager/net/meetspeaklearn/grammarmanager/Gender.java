/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

/**
 *
 * @author steve
 */
public enum Gender {
    UNKNOWN("Unknown"),
    MASCULINE("Masculine"),
    FEMININE("Feminine"),
    NEUTER("Neuter"),
    ANIMATE("Animate"),
    INANIMATE("Inanimate"),
    COMMON("Common");
    
    private String type;

    Gender(String type) {
        this.type = type;
    }
        
    public static String[] valuesAsStrings() {
        Gender[] valuesArray = Gender.values();
        int count = valuesArray.length;
        String[] valuesAsStrings = new String[count];

        for (int i = 0; i < count; i++)
            valuesAsStrings[i] = valuesArray[i].toString();

        return valuesAsStrings;
    }
    
    public static Gender fromString(String string) {
        Gender[] valuesArray = Gender.values();
        
        try {
            for (Gender gender : valuesArray) {
                if (gender.toString().compareTo(string) == 0)
                    return gender;
            }
        } catch (Exception e) {
            return Gender.UNKNOWN;
        }
        
        return Gender.UNKNOWN;
    }

    public String toString() {
        return this.type;
    }
}
