/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.oopl;

import net.meetspeaklearn.grammarmanager.Language;

/**
 *
 * @author steve
 */
public enum ValueType {
        NOT_A_TYPE("notatype"),
        UNKNOWN("unknown"),
        VOID("void"),
        BOOLEAN("boolean"),
        CHARACTER("character"),
        INTEGER("integer"),
        REAL("real"),
        VECTOR("vector"),
        STRING("string"),
        OBJECT("object");
        
        private String type;
        
        ValueType(String type) {
            this.type = type;
        }
        
        public String toString() {
            return this.type;
        }
        
        public static String[] valuesAsString() {
            ValueType[] valuesArray = ValueType.values();
            int count = valuesArray.length;
            String[] valuesAsStrings = new String[count];
            
            for (int i = 0; i < count; i++)
                valuesAsStrings[i] = valuesArray[i].toString();
            
            return valuesAsStrings;
        }
}
