/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.compiler;
import net.meetspeaklearn.util.StringAggregator;

import java.util.*;

/**
 *
 * @author steve
 */
public class ObjectManager {
    private ArrayList<AdjectiveCategory> adjectiveCategories;
    private ArrayList<Adjective> adjectives;
    private ArrayList<Noun> nouns;
    
    public ObjectManager() {
        adjectiveCategories = new ArrayList<AdjectiveCategory>();
        adjectives = new ArrayList<Adjective>();
        nouns = new ArrayList<Noun>();
    }

    public ArrayList<AdjectiveCategory> getAdjectiveCategories() {
        return adjectiveCategories;
    }

    public void setAdjectiveCategories(ArrayList<AdjectiveCategory> adjectiveCategories) {
        this.adjectiveCategories = adjectiveCategories;
    }

    public ArrayList<Adjective> getAdjectives() {
        return adjectives;
    }

    public void setAdjectives(ArrayList<Adjective> adjectives) {
        this.adjectives = adjectives;
    }

    public ArrayList<Noun> getNouns() {
        return nouns;
    }

    public void setNouns(ArrayList<Noun> nouns) {
        this.nouns = nouns;
    }
    
    public int add(AdjectiveCategory cat) {
        if (cat.isInterned()) return cat.getIndex();
        
        cat.setIndex(adjectiveCategories.size());
        adjectiveCategories.add(cat);
        
        return cat.getIndex();
    }
    
    public AdjectiveCategory getAdjCat(int index) {
        if (index < 0) return null;
        
        int adjCatCount = adjectiveCategories.size();
        
        if (index >= adjCatCount) return null;
        
        return adjectiveCategories.get(index);
    }
    
    public int add(Adjective adj) {
        if (adj.isInterned()) return adj.getIndex();
        
        adj.setIndex(adjectives.size());
        adjectives.add(adj);
        
        return adj.getIndex();
    }
    
    public Adjective getAdj(int index) {
        if (index < 0) return null;
        
        int adjCount = adjectives.size();
        
        if (index >= adjCount) return null;
        
        return adjectives.get(index);
    }
    
    public int add(Noun noun) {
        if (noun.isInterned()) return noun.getIndex();
        
        noun.setIndex(nouns.size());
        nouns.add(noun);
        
        return noun.getIndex();
    }
    
    public Noun getNoun(int index) {
        if (index < 0) return null;
        
        int nounCount = nouns.size();
        
        if (index >= nounCount) return null;
        
        return nouns.get(index);
    }
    
    public void toStringHelper(StringAggregator aggregator) {
        aggregator.push("ObjectManager: {", ",", "}");
        
        aggregator.push("adjectiveCategories: (", ",", ")");
        
        for (AdjectiveCategory cat : adjectiveCategories) {
            cat.toStringHelper(aggregator);
        }
        
        aggregator.pop();
        
        aggregator.push("adjectives=(", ",", ")");
        
        for (Adjective adj : adjectives) {
            adj.toStringHelper(aggregator);
        }
        
        aggregator.pop();
        
        aggregator.push("nouns=(", ",", ")");
        
        for (Noun noun : nouns) {
            noun.toStringHelper(aggregator);
        }
        
        aggregator.pop();
        
        aggregator.pop();
    }
    
    public void toJavaScipt(StringAggregator aggregator) {
        for (AdjectiveCategory cat : adjectiveCategories) {
            cat.toJavaScipt(aggregator);
        }
        for (Adjective adj : adjectives) {
            adj.toJavaScipt(aggregator);
        }
        for (Noun noun : nouns) {
            noun.toJavaScipt(aggregator);
        }
    }
}
