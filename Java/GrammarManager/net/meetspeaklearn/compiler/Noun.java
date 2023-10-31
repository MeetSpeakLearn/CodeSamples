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
public class Noun extends PartOfSpeech {
    private final static long COUNTABLE     = 0b0000000000000001;
    private final static long ALWAYS_PLURAL = 0b0000000000000010;
    private final static long PROPER        = 0b0000000000000100;
    
    private String pluralForm;
    private int[] adjectiveIndices;
    
    public Noun() {
        super();
    }

    public Noun(ObjectManager om) {
        super();
        this.om = om;
        om.add(this);
    }
    
    public Noun(int index, ObjectManager om) {
        super(index, om);
    }

    public String getPluralForm() {
        return pluralForm;
    }

    public void setPluralForm(String pluralForm) {
        this.pluralForm = pluralForm;
    }

    public int[] getAdjectiveIndices() {
        return adjectiveIndices;
    }

    public void setAdjectiveIndices(int[] adjectiveIndices) {
        this.adjectiveIndices = adjectiveIndices;
    }
    
    public boolean isCountable() {
        return (flags & TYPE_MASK) == COUNTABLE;
    }
    
    public void setCountable(boolean value) {
        if (value)
            flags |= COUNTABLE;
        else
            flags &= ~COUNTABLE;
    }
    
    public boolean isAlwaysPlural() {
        return (flags & TYPE_MASK) == ALWAYS_PLURAL;
    }
    
    public void setAlwaysPlural(boolean value) {
        if (value)
            flags |= ALWAYS_PLURAL;
        else
            flags &= ~ALWAYS_PLURAL;
    }
    
    public boolean isProper() {
        return (flags & TYPE_MASK) == PROPER;
    }
    
    public void setProper(boolean value) {
        if (value)
            flags |= PROPER;
        else
            flags &= ~PROPER;
    }
    
    public Adjective[] getAdjectives() {
        int count = adjectiveIndices.length;
        Adjective[] adjectives = new Adjective[count];
        
        for (int i = 0; i < count; i++) {
            adjectives[i] = adj(adjectiveIndices[i]);
        }
        
        return adjectives;
    }
    
    public void setAdjectives(Adjective[] adjectives) {
        int count = adjectives.length;
        adjectiveIndices = new int[count];
        
        for (int i = 0; i < count; i++)
            adjectiveIndices[i] = adjectives[i].getIndex();
    }
    
    public void setAdjectives(ArrayList<Adjective> adjectives) {
        int count = adjectives.size();
        adjectiveIndices = new int[count];
        
        for (int i = 0; i < count; i++)
            adjectiveIndices[i] = adjectives.get(i).getIndex();
    }
    
    public void toStringHelper(StringAggregator aggregator) {
        aggregator.push("Noun: {", ",", "}");
        aggregator.add("name=\"" + name + "\"");
        aggregator.add("flags=" + flags);
        aggregator.add("pluralForm=\"" + pluralForm + "\"");
        
        if (adjectiveIndices != null)
            aggregator.add("adjectiveIndices=[" + StringAggregator.aggregate(adjectiveIndices) + "]");
        else 
            aggregator.add("adjectiveIndices=[]");
        
        aggregator.pop();
    }

    public void toJavaScipt(StringAggregator aggregator) {
        StringAggregator subAggregator = new StringAggregator("", true);
        
        subAggregator.push("\n_ogma.addNoun(", ", ", ")");
        
        subAggregator.add("\"" + name + "\", ");
        subAggregator.add(Long.toString(flags) + ", ");
        subAggregator.add("\"" + pluralForm + "\", ");      
        
        if (adjectiveIndices != null) {
            subAggregator.add("[" + StringAggregator.aggregate(adjectiveIndices) + "]");
        } else {
            subAggregator.add("[]");
        }
        
        subAggregator.pop();
        
        aggregator.add(subAggregator.toString());
    }
}
