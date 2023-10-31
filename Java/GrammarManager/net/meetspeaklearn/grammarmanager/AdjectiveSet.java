/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import java.util.*;

/**
 *
 * @author steve
 */
public class AdjectiveSet extends HashSet<Adjective> {
    private String name;
    private AdjectiveSetManager manager;
    
    public AdjectiveSet() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AdjectiveSetManager getManager() {
        return manager;
    }

    public void setManager(AdjectiveSetManager manager) {
        this.manager = manager;
    }
    
    public void managedAdd(Adjective adj) {
        manager.addToSet(this, adj);
    }
    
    public void managedAddAll(Collection<Adjective> adjs) {
        for (Adjective adj : adjs) managedAdd(adj);
    }
    
    public void managedRemove(Adjective adj) {
        manager.removeFromSet(this, adj);
    }
    
    public AdjectiveSet(AdjectiveSetManager manager) {
        super();
        this.manager = manager;
    }
    
    public AdjectiveSet(AdjectiveSetManager manager, String name) {
        super();
        this.manager = manager;
        this.name = name;
    }
    
    public AdjectiveSet(AdjectiveSetManager manager, int n) {
        super(n);
        this.manager = manager;
    }
    
    public AdjectiveSet(AdjectiveSetManager manager, String name, int n) {
        super(n);
        this.name = name;
        this.manager = manager;
    }
}
