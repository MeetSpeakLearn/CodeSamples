/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.util;

import java.util.HashMap;

/**
 *
 * @author steve
 */
public class PropertySet implements IPropertySet {
    private HashMap<String, PropertyValue> keyValueMap;
    
    public PropertySet() {
        keyValueMap = new HashMap<String, PropertyValue>();
    }

    public HashMap<String, PropertyValue> getKeyValueMap() {
        return keyValueMap;
    }

    public void setKeyValueMap(HashMap<String, PropertyValue> keyValueMap) {
        this.keyValueMap = keyValueMap;
    }
    
    public boolean isProperty(String propertyName) {
        return keyValueMap.containsKey(propertyName);
    }
    
    public Object getPropertyValue(String propertyName) throws PropertySetException {
        if (! keyValueMap.containsKey(propertyName)) {
            System.err.println("Attempt to get the value of non-property " + propertyName);
            throw new PropertySetException("Attempt to get the value of non-property " + propertyName);
        }
        
        System.err.println("Attempting to get the value of a property " + propertyName);
        
        PropertyValue propertyValue = keyValueMap.get(propertyName);
        
        return propertyValue.getValue();
    }
    
    public void setPropertyValue(String propertyName, String value) throws PropertySetException {
        System.out.println("setPropertyValue for string");
        if (keyValueMap.containsKey(propertyName)) {
            PropertyValue propertyValue = keyValueMap.get(propertyName);
            
            if (propertyValue.getType() != PropertyValue.VALUE_TYPE.STRING)
                throw new PropertySetException("Attempt to get the value of string property " + propertyName + " to a non-string value");
            
            propertyValue.setValue(value);
        } else {
            keyValueMap.put(propertyName, new StringPropertyValue(value));
        }
    }
    
    public void setPropertyValue(String propertyName, int value) throws PropertySetException {
        if (keyValueMap.containsKey(propertyName)) {
            PropertyValue propertyValue = keyValueMap.get(propertyName);
            
            if (propertyValue.getType() != PropertyValue.VALUE_TYPE.INT)
                throw new PropertySetException("Attempt to get the value of int property " + propertyName + " to a non-int value");
            
            propertyValue.setValue(value);
        } else {
            keyValueMap.put(propertyName, new IntegerPropertyValue(value));
        }
    }
    
    public void setPropertyValue(String propertyName, float value) throws PropertySetException {
        if (keyValueMap.containsKey(propertyName)) {
            PropertyValue propertyValue = keyValueMap.get(propertyName);
            
            if (propertyValue.getType() != PropertyValue.VALUE_TYPE.FLOAT)
                throw new PropertySetException("Attempt to get the value of float property " + propertyName + " to a non-float value");
            
            propertyValue.setValue(value);
        } else {
            keyValueMap.put(propertyName, new FloatPropertyValue(value));
        }
    }
    
    public void setPropertyValue(String propertyName, double value) throws PropertySetException {
        if (keyValueMap.containsKey(propertyName)) {
            PropertyValue propertyValue = keyValueMap.get(propertyName);
            
            if (propertyValue.getType() != PropertyValue.VALUE_TYPE.DOUBLE)
                throw new PropertySetException("Attempt to get the value of double property " + propertyName + " to a non-double value");
            
            propertyValue.setValue(value);
        } else {
            keyValueMap.put(propertyName, new DoublePropertyValue(value));
        }
    }
    
    public void setPropertyValue(String propertyName, boolean value) throws PropertySetException {
        if (keyValueMap.containsKey(propertyName)) {
            PropertyValue propertyValue = keyValueMap.get(propertyName);
            
            if (propertyValue.getType() != PropertyValue.VALUE_TYPE.BOOLEAN)
                throw new PropertySetException("Attempt to get the value of boolean property " + propertyName + " to a non-boolean value");
            
            propertyValue.setValue(value);
        } else {
            keyValueMap.put(propertyName, new BooleanPropertyValue(value));
        }
    }
}
