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
public interface IListItemSelected {
    public void addListener(IListItemSelectedListener listener);
    public void removeListener(IListItemSelectedListener listener);
}
