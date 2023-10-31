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
public interface IPropertySet {
    public boolean isProperty(String propertyName);
    public Object getPropertyValue(String propertyName) throws PropertySetException;
    public void setPropertyValue(String propertyName, String value) throws PropertySetException;
    public void setPropertyValue(String propertyName, int value) throws PropertySetException;
    public void setPropertyValue(String propertyName, float value) throws PropertySetException;
    public void setPropertyValue(String propertyName, double value) throws PropertySetException;
    public void setPropertyValue(String propertyName, boolean value) throws PropertySetException;
}
