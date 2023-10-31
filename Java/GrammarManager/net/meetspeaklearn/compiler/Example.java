/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.compiler;

import net.meetspeaklearn.grammarmanager.Gender;
import net.meetspeaklearn.util.StringAggregator;
import java.util.*;

/**
 *
 * @author steve
 */
public class Example {
    private String name;
    private String description;
    private String creator;
    private ObjectManager objectManager;
    private AdjectiveCategoryMap adjCatMap;
    private HashSet<Noun> nouns;
    private HashSet<net.meetspeaklearn.grammarmanager.Adjective> filteredAdjectives;
    private NounMapper nounMapper;
    
    public Example() {
        
    }
    
    public Example(AdjectiveCategoryMap map, ArrayList<net.meetspeaklearn.grammarmanager.Noun> managerNouns) {
        objectManager = new ObjectManager();
        nounMapper = new NounMapper();
        this.filteredAdjectives = null;
        adjCatMap = map;
        map.setOm(objectManager);
        nouns = new HashSet<Noun>();
        HashSet<net.meetspeaklearn.grammarmanager.Adjective> importedAdjectives = importNouns(managerNouns);
        adjCatMap.add(importedAdjectives);
        adjCatMap.link(nounMapper);
    }
    
    public Example(ArrayList<net.meetspeaklearn.grammarmanager.Noun> managerNouns) {
        objectManager = new ObjectManager();
        nounMapper = new NounMapper();
        adjCatMap = new DefaultAdjectiveCategoryMap(objectManager);
        adjCatMap.setOm(objectManager);
        nouns = new HashSet<Noun>();
        this.filteredAdjectives = null;
        HashSet<net.meetspeaklearn.grammarmanager.Adjective> importedAdjectives = importNouns(managerNouns);
        adjCatMap.add(importedAdjectives);
        adjCatMap.link(nounMapper);
    }
    
    public Example(ArrayList<net.meetspeaklearn.grammarmanager.Noun> managerNouns,
            HashSet<net.meetspeaklearn.grammarmanager.Adjective> filteredAdjectives) {
        objectManager = new ObjectManager();
        nounMapper = new NounMapper();
        this.filteredAdjectives = filteredAdjectives;
        adjCatMap = new DefaultAdjectiveCategoryMap(objectManager);
        adjCatMap.setOm(objectManager);
        nouns = new HashSet<Noun>();
        HashSet<net.meetspeaklearn.grammarmanager.Adjective> importedAdjectives = importNouns(managerNouns);
        adjCatMap.add(importedAdjectives);
        adjCatMap.link(nounMapper);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public void setObjectManager(ObjectManager objectManager) {
        this.objectManager = objectManager;
    }

    public AdjectiveCategoryMap getAdjCatMap() {
        return adjCatMap;
    }

    public void setAdjCatMap(AdjectiveCategoryMap adjCatMap) {
        this.adjCatMap = adjCatMap;
    }

    public HashSet<Noun> getNouns() {
        return nouns;
    }

    public void setNouns(HashSet<Noun> nouns) {
        this.nouns = nouns;
    }
    
    public HashSet<net.meetspeaklearn.grammarmanager.Adjective> importNoun(net.meetspeaklearn.grammarmanager.Noun mNoun) {
        Gender mGender = mNoun.getGender();
        boolean mNounIsCountable = mNoun.isCountNoun();
        boolean mNounIsAlwaysPlural = mNoun.isAlwaysPlural();
        boolean mNounIsProper = mNoun.isProper();
        
        Noun cNoun = new Noun(objectManager);
        
        cNoun.setName(mNoun.getLexeme().getLexeme());
        cNoun.setGender(mGender);
        cNoun.setCountable(mNounIsCountable);
        cNoun.setProper(mNounIsProper);
        cNoun.setPluralForm(mNoun.getDeclination(net.meetspeaklearn.grammarmanager.PartOfSpeech.NUMBER.PLURAL));
        
        nouns.add(cNoun);
        nounMapper.associate(mNoun, cNoun);
        
        // Gather adjectives...
        HashSet<net.meetspeaklearn.grammarmanager.Adjective> intersection =
                new HashSet<net.meetspeaklearn.grammarmanager.Adjective>();
        
        if (filteredAdjectives != null) {
            for (net.meetspeaklearn.grammarmanager.Adjective inherited : mNoun.getAllInheritedAdjectives()) {
                if (filteredAdjectives.contains(inherited)) intersection.add(inherited);
            }
        } else {
            intersection.addAll(mNoun.getAllInheritedAdjectives());
        }
        
        for (net.meetspeaklearn.grammarmanager.Adjective thisNounsAdj : intersection)
            nounMapper.associate(mNoun, thisNounsAdj);
        
        return intersection;
    }
    
    public HashSet<net.meetspeaklearn.grammarmanager.Adjective> importNouns(ArrayList<net.meetspeaklearn.grammarmanager.Noun> managerNouns) {
        HashSet<net.meetspeaklearn.grammarmanager.Adjective> unionOfAdjectiveSets = new HashSet<net.meetspeaklearn.grammarmanager.Adjective>();
        
        for (net.meetspeaklearn.grammarmanager.Noun mNoun : managerNouns) {
            unionOfAdjectiveSets.addAll(importNoun(mNoun));
        }
        
        return unionOfAdjectiveSets;
    }
    
    public String toString () {
        StringAggregator aggregator = new StringAggregator(",", "  ");
        aggregator.push("Example: { name=\"" + name + "creator=\"" + creator + "\", description=\"" + description + "\"",
                ",", "}");
        
        objectManager.toStringHelper(aggregator);
        
        return aggregator.toString();
    }
    
    public String toJavaScipt() {
        StringAggregator aggregator = new StringAggregator(";\n", "    ");
        
        aggregator.add("let _ogma = new Ogma()");
        aggregator.add("_ogma.setName(\"" + name + "\")");
        aggregator.add("_ogma.setDescription(\"" + description + "\")");
        aggregator.add("_ogma.setCreator(\"" + creator + "\")");
        objectManager.toJavaScipt(aggregator);
        
        return aggregator.toString() + ";";
    }
}
