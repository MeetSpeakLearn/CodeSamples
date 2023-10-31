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
public class BooleanPropertyValue extends PropertyValue {
    private boolean value;
    
    public BooleanPropertyValue() {
        type = PropertyValue.VALUE_TYPE.BOOLEAN;
        value = false;
    }
    
    public BooleanPropertyValue(boolean value) {
        this();
        this.value = value;
    }
    
    public Object getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = Boolean.parseBoolean(value);
    }
    
    public void setValue(int value) {
        this.value = (value != 0);
    }
    
    public void setValue(float value) {
        this.value = (value > 0);
    }
    
    public void setValue(double value) {
        this.value = (value > 0);
    }
    
    public void setValue(boolean value) {
        this.value = value;
    }
}
