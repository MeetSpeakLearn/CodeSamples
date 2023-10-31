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
public class FloatPropertyValue extends PropertyValue {
    private float value;
    
    public FloatPropertyValue() {
        type = PropertyValue.VALUE_TYPE.FLOAT;
        value = 0;
    }
    
    public FloatPropertyValue(float value) {
        this();
        this.value = value;
    }
    
    public Object getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = Float.parseFloat(value);
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    public void setValue(float value) {
        this.value = value;
    }
    
    public void setValue(double value) {
        this.value = (float) value;
    }
    
    public void setValue(boolean value) {
        this.value = (value) ? 1 : -1;
    }
}
