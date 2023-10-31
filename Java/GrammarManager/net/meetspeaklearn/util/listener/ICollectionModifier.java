/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.util.listener;

/**
 *
 * @author steve
 */
public interface ICollectionModifier {
    public void addListener(ICollectionModifiedListener listener);
    public void removeListener(ICollectionModifiedListener listener);
}
