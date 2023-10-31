/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.compiler;

import java.util.*;
import net.meetspeaklearn.util.StringAggregator;

/**
 *
 * @author steve
 */
public class AdjectiveCategory extends ManagedObject {
    private String name;
    private int precedence;
    private ArrayList<Integer> adjectiveIndices;
    
    public AdjectiveCategory() {
        super();
        adjectiveIndices = new ArrayList<Integer>();
    }

    public AdjectiveCategory(ObjectManager om) {
        super();
        this.om = om;
        om.add(this);
    }
    
    public AdjectiveCategory(int index, ObjectManager om) {
        super(index, om);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrecedence() {
        return precedence;
    }

    public void setPrecedence(int precedence) {
        this.precedence = precedence;
    }

    public ArrayList<Integer> getAdjectiveIndices() {
        return adjectiveIndices;
    }

    public void setAdjectiveIndices(ArrayList<Integer> adjectiveIndices) {
        this.adjectiveIndices = adjectiveIndices;
    }
    
    public void addAdjective(Adjective adjective) {
        if (adjectiveIndices == null) {
            adjectiveIndices =  new ArrayList<Integer>();
        }
        
        if (! adjectiveIndices.contains(adjective.getIndex())) {
            adjectiveIndices.add(adjective.getIndex());
        }
    }
    
    public ArrayList<Adjective> getAdjectives() {
        ArrayList<Adjective> result = new ArrayList<Adjective>();
        
        if (adjectiveIndices == null) {
            return result;
        }
        
        for (int index : adjectiveIndices)
            result.add(adj(index));
        
        return result;
    }
    
    public void toStringHelper(StringAggregator aggregator) {
        aggregator.push("AdjectiveCategory: {", ",", "}");
        
        aggregator.add("name=\"" + getName() + "\"");
        aggregator.add("precedence=\"" + getPrecedence() + "\"");
        
        if (adjectiveIndices != null) {
            aggregator.add("adjectiveIndices=[" + StringAggregator.aggregate(adjectiveIndices.toArray(new Integer[0])) + "]");
        } else
            aggregator.add("adjectiveIndices=[]");
        
        aggregator.pop();
    }
    
    public void toJavaScipt(StringAggregator aggregator) {
        StringAggregator subAggregator = new StringAggregator("", true);
        
        subAggregator.push("\n_ogma.addAdjectiveCategory(", ", ", ")");
        
        subAggregator.add("\"" + name + "\", ");
        
        subAggregator.add(Integer.toString(precedence) + ", ");
        
        if (adjectiveIndices != null) {
            subAggregator.add("[" + StringAggregator.aggregate(adjectiveIndices.toArray(new Integer[0])) + "]");
        } else {
            subAggregator.add("[]");
        }
        
        subAggregator.pop();
        
        aggregator.add(subAggregator.toString());
    }
}
