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
public enum AcademicLevel {
    UNASSIGNED("Unassigned"),
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");
    
    private String type;

    AcademicLevel(String type) {
        this.type = type;
    }
        
    public static String[] valuesAsStrings() {
        AcademicLevel[] valuesArray = AcademicLevel.values();
        int count = valuesArray.length;
        String[] valuesAsStrings = new String[count];

        for (int i = 0; i < count; i++)
            valuesAsStrings[i] = valuesArray[i].toString();

        return valuesAsStrings;
    }
    
    public static AcademicLevel fromString(String name) {
        AcademicLevel[] valuesArray = AcademicLevel.values();
        String[] valuesAsStrings = valuesAsStrings();
        int i = 0;
        
        for (String valueName : valuesAsStrings) {
            if (valueName.compareTo(name) == 0) {
                return valuesArray[i];
            }
            i += 1;
        }
        
        return UNASSIGNED;
    }

    public String toString() {
        return this.type;
    }
    
}
