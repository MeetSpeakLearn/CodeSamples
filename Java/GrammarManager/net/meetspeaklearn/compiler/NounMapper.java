/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.compiler;

import java.util.*;

/**
 *
 * @author steve
 */
public class NounMapper {
    private HashMap<net.meetspeaklearn.grammarmanager.Noun, Noun> managerToCompilerMap;
    private HashMap<Noun, net.meetspeaklearn.grammarmanager.Noun> compilerToManagerMap;
    private HashMap<net.meetspeaklearn.grammarmanager.Noun, HashSet<net.meetspeaklearn.grammarmanager.Adjective>> managerNounToAdjectiveMap;
    private HashMap<Noun, HashSet<Adjective>> compilerNounToAdjectiveMap;
    
    public NounMapper() {
        managerToCompilerMap = new HashMap<net.meetspeaklearn.grammarmanager.Noun, Noun>();
        compilerToManagerMap = new HashMap<Noun, net.meetspeaklearn.grammarmanager.Noun>();
        managerNounToAdjectiveMap = new HashMap<net.meetspeaklearn.grammarmanager.Noun, HashSet<net.meetspeaklearn.grammarmanager.Adjective>>();
        compilerNounToAdjectiveMap = new HashMap<Noun, HashSet<Adjective>>();
    }
    
    public void associate(net.meetspeaklearn.grammarmanager.Noun managerNoun, Noun compilerNoun) {
        managerToCompilerMap.put(managerNoun, compilerNoun);
        compilerToManagerMap.put(compilerNoun, managerNoun);
    }
    
    public void associate(net.meetspeaklearn.grammarmanager.Noun managerNoun,
                            net.meetspeaklearn.grammarmanager.Adjective managerAdjective) {
        HashSet<net.meetspeaklearn.grammarmanager.Adjective> adjectiveSet = managerNounToAdjectiveMap.get(managerNoun);
        
        if (adjectiveSet == null) {
            adjectiveSet = new HashSet<net.meetspeaklearn.grammarmanager.Adjective>();
            managerNounToAdjectiveMap.put(managerNoun, adjectiveSet);
        }
        
        adjectiveSet.add(managerAdjective);
    }
    
    public Set<net.meetspeaklearn.grammarmanager.Noun> getManagerNouns() {
        return managerToCompilerMap.keySet();
    }
    
    public Set<Noun> getCompilerNouns() {
        return compilerToManagerMap.keySet();
    }
    
    public Set<net.meetspeaklearn.grammarmanager.Adjective> getNounAdjectives(net.meetspeaklearn.grammarmanager.Noun managerNoun) {
        return managerNounToAdjectiveMap.get(managerNoun);
    }
    
    public Set<Adjective> getNounAdjectives(Noun compilerNoun) {
        return compilerNounToAdjectiveMap.get(compilerNoun);
    }
    
    public Noun getAssociatedCompilerNoun(net.meetspeaklearn.grammarmanager.Noun managerNoun) {
        return managerToCompilerMap.get(managerNoun);
    }
    
    public void associate(Noun cNoun, Adjective cAdjective) {
        HashSet<Adjective> adjectiveSet = compilerNounToAdjectiveMap.get(cNoun);
        
        if (adjectiveSet == null) {
            adjectiveSet = new HashSet<Adjective>();
            compilerNounToAdjectiveMap.put(cNoun, adjectiveSet);
        }
        
        adjectiveSet.add(cAdjective);
    }
}
