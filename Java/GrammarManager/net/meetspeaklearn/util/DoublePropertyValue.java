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
public class DoublePropertyValue extends PropertyValue {
    private double value;
    
    public DoublePropertyValue() {
        type = PropertyValue.VALUE_TYPE.DOUBLE;
        value = 0;
    }
    
    public DoublePropertyValue(double value) {
        this();
        this.value = value;
    }
    
    public Object getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = Double.parseDouble(value);
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    public void setValue(float value) {
        this.value = value;
    }
    
    public void setValue(double value) {
        this.value = value;
    }
    
    public void setValue(boolean value) {
        this.value = (value) ? 1 : -1;
    }
}
