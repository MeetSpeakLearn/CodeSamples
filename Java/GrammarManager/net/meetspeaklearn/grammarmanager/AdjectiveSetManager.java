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
public class AdjectiveSetManager {
    private HashMap<String, AdjectiveSet> setMap;
    private HashMap<Adjective, ArrayList<AdjectiveSet>> adjectives;
    
    public AdjectiveSetManager() {
        setMap = new HashMap<String, AdjectiveSet>();
        adjectives = new HashMap<Adjective, ArrayList<AdjectiveSet>>();
    }

    public HashMap<String, AdjectiveSet> getSetMap() {
        return setMap;
    }

    public void setSetMap(HashMap<String, AdjectiveSet> setMap) {
        this.setMap = setMap;
    }

    public HashMap<Adjective, ArrayList<AdjectiveSet>> getAdjectives() {
        return adjectives;
    }

    public void setAdjectives(HashMap<Adjective, ArrayList<AdjectiveSet>> adjectives) {
        this.adjectives = adjectives;
    }
    
    public AdjectiveSet getSet(String name) {
        if (isRegisteredName(name))
            return setMap.get(name);
        
        AdjectiveSet set = new AdjectiveSet(this, name);
        
        setMap.put(name, set);
        
        return set;
    }
    
    public void removeSet(AdjectiveSet set) {
        String name = set.getName();
        
        setMap.remove(name);
        
        for (Adjective adj : set) {
            ArrayList<AdjectiveSet> foundSets = new ArrayList<AdjectiveSet>();
            for (AdjectiveSet cachedSet : adjectives.get(adj)) {
                if (cachedSet == set) {
                    foundSets.add(cachedSet);
                }
            }
            for (AdjectiveSet foundSet : foundSets) {
                adjectives.get(adj).remove(foundSet);
            }
            if (adjectives.get(adj).size() == 0) {
                adjectives.remove(adj);
            }
        }
    }
    
    public void addToSet(AdjectiveSet set, Adjective adj) {
        if (set.contains(adj)) return;        
        if (! setMap.containsKey(set.getName())) return;
        
        set.add(adj);
        
        ArrayList<AdjectiveSet> adjSets = adjectives.get(adj);
        
        if (adjSets == null) {
            adjSets = new ArrayList<AdjectiveSet>();
            adjSets.add(set);
            adjectives.put(adj, adjSets);
        } else {
            for (AdjectiveSet cachedSet : adjSets) {
                if (cachedSet == set) return;
            }
            
            adjSets.add(set);
        }
    }
    
    public void removeFromSet(AdjectiveSet set, Adjective adj) {
        if (! set.contains(adj)) return;
        if (! setMap.containsKey(set.getName())) return;
        
        set.remove(adj);
        
        ArrayList<AdjectiveSet> adjSets = adjectives.get(adj);
        
        if (adjSets == null) return;
        
        adjSets.remove(set);
    }
    
    public boolean isRegisteredName(String name) {
        return setMap.keySet().contains(name);
    }
    
    public boolean isRegisteredAdjective(Adjective adj) {
        return adjectives.keySet().contains(adj);
    }
    
}
