/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.util;

/**
 *
 * @author steve
 */

public abstract class PropertyValue {
    public static enum VALUE_TYPE {
        STRING, INT, FLOAT, DOUBLE, BOOLEAN
    }
    
    protected VALUE_TYPE type;
    
    public VALUE_TYPE getType() {
        return type;
    }
    
    public abstract Object getValue();
    
    public abstract void setValue(String value);
    public abstract void setValue(int value);
    public abstract void setValue(float value);
    public abstract void setValue(double value);
    public abstract void setValue(boolean value);
    
    public String toString() {
        return getValue().toString();
    }
}
