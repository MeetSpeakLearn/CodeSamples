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
public class IntegerPropertyValue extends PropertyValue  {
    private int value;
    
    public IntegerPropertyValue() {
        type = PropertyValue.VALUE_TYPE.INT;
        value = 0;
    }
    
    public IntegerPropertyValue(int value) {
        this();
        this.value = value;
    }
    
    public Object getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = Integer.parseInt(value);
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    public void setValue(float value) {
        this.value = (int) Math.floor((double) value);
    }
    
    public void setValue(double value) {
        this.value = (int) Math.floor(value);
    }
    
    public void setValue(boolean value) {
        this.value = (value) ? 1 : 0;
    }
}
