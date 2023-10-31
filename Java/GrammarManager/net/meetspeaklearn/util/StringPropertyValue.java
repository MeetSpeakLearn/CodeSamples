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
public class StringPropertyValue extends PropertyValue {
    private String value;
    
    public StringPropertyValue() {
        type = PropertyValue.VALUE_TYPE.STRING;
        value = null;
    }
    
    public StringPropertyValue(String value) {
        this();
        this.value = value;
    }
    
    public Object getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public void setValue(int value) {
        this.value = Integer.toString(value);
    }
    
    public void setValue(float value) {
        this.value = Float.toString(value);
    }
    
    public void setValue(double value) {
        this.value = Double.toString(value);
    }
    
    public void setValue(boolean value) {
        this.value = Boolean.toString(value);
    }
}
