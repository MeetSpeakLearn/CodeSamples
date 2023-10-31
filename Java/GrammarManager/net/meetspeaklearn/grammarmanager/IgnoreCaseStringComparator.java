/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import java.util.Comparator;

/**
 *
 * @author steve
 */
public class IgnoreCaseStringComparator implements Comparator<String> {
    public int compare(String o1, String o2) {
            return o1.compareToIgnoreCase(o2);
    }

    public IgnoreCaseStringComparator() {

    }
}
