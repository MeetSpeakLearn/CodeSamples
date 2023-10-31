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
public class Adjective extends PartOfSpeech {
    private final static byte GENERIC = 0b00000000;
    private final static byte ARTICLE = 0b00000001;
    private final static byte DEMONSTRATIVE = 0b00000010;
    private final static byte DETERMINER = 0b00000010;
    private final static byte POSSESSIVE = 0b00000100;
    private final static byte QUANTIFIER = 0b00000101;
    
    private int adjectiveCategoryIndex;
    private int precedence;
    private int rankInCategory;
    private int[] conflictIndices;
    
    public Adjective() {
        super();
    }

    public Adjective(ObjectManager om) {
        super();
        this.om = om;
        om.add(this);
    }
    
    public Adjective(int index, ObjectManager om) {
        super(index, om);
    }

    public int getAdjectiveCategoryIndex() {
        return adjectiveCategoryIndex;
    }

    public void setAdjectiveCategoryIndex(int adjectiveCategoryIndex) {
        this.adjectiveCategoryIndex = adjectiveCategoryIndex;
    }

    public int getPrecedence() {
        return precedence;
    }

    public void setPrecedence(int precedence) {
        this.precedence = precedence;
    }

    public int getRankInCategory() {
        return rankInCategory;
    }

    public void setRankInCategory(int rankInCategory) {
        this.rankInCategory = rankInCategory;
    }

    public int[] getConflictIndices() {
        return conflictIndices;
    }

    public void setConflictIndices(int[] conflictIndices) {
        this.conflictIndices = conflictIndices;
    }
    
    public AdjectiveCategory getCategory() {
        return cat(adjectiveCategoryIndex);
    }
    
    public void setAdjectiveCategory(AdjectiveCategory category) {
        adjectiveCategoryIndex = category.getIndex();
    }
    
    public Adjective[] getConflicts() {
        if (conflictIndices == null) return new Adjective[0];
        
        int conflictCount = conflictIndices.length;
        Adjective[] result = new Adjective[conflictCount];
        
        for (int i = 0; i < conflictCount; i++) {
            result[i] = adj(conflictIndices[i]);
        }
        
        return result;
    }
    
    public void setConflicts(Adjective[] conflicts) {
        int conflictCount = conflicts.length;
        
        conflictIndices = new int[conflictCount];
        
        for (int i = 0; i < conflictCount; i++) {
            conflictIndices[i] = conflicts[i].getIndex();
        }
    }
    
    public void setConflicts(ArrayList<Adjective> conflicts) {
        int conflictCount = conflicts.size();
        
        conflictIndices = new int[conflictCount];
        
        for (int i = 0; i < conflictCount; i++) {
            conflictIndices[i] = conflicts.get(i).getIndex();
        }
    }
    
    public byte getType() {
        return (byte) (flags & TYPE_MASK);
    }
    
    public void setType(byte type) {
        flags = (byte) ((flags & ~TYPE_MASK) | type);
    }
    
    public void setType(net.meetspeaklearn.grammarmanager.Adjective.ADJECTIVE_TYPE adjType) {
        switch (adjType) {
            case GENERIC: setType(GENERIC); break;
            case ARTICLE: setType(ARTICLE); break;
            case DEMONSTRATIVE: setType(DEMONSTRATIVE); break;
            case DETERMINER: setType(DETERMINER); break;
            case POSSESSIVE: setType(POSSESSIVE); break;
            case QUANTIFIER: setType(QUANTIFIER); break;
            default:                
        }
    }
    
    public boolean isGeneric() {
        return (flags & TYPE_MASK) == GENERIC;
    }
    
    public boolean isArticle() {
        return (flags & TYPE_MASK) == ARTICLE;
    }
    
    public boolean isDemonstrative() {
        return (flags & TYPE_MASK) == DEMONSTRATIVE;
    }
    
    public boolean isDeterminer() {
        return (flags & TYPE_MASK) == DETERMINER;
    }
    
    public boolean isPossessive() {
        return (flags & TYPE_MASK) == POSSESSIVE;
    }
    
    public boolean isQuantifier() {
        return (flags & TYPE_MASK) == QUANTIFIER;
    }
    
    public void toStringHelper(StringAggregator aggregator) {
        aggregator.push("Adjective: {", ",", "}");
        aggregator.add("name=\"" + name + "\"");
        aggregator.add("flags=" + flags);
        aggregator.add("adjectiveCategoryIndex=\"" + adjectiveCategoryIndex + "\"");
        aggregator.add("precedence=" + precedence);
        aggregator.add("rankInCategory=" + rankInCategory);
        aggregator.add("conflictIndices=[" + StringAggregator.aggregate(conflictIndices) + "]");
        aggregator.pop();
    }
    
    public void toJavaScipt(StringAggregator aggregator) {
        StringAggregator subAggregator = new StringAggregator("", true);
        
        subAggregator.push("\n_ogma.addAdjective(", ", ", ")");
        
        subAggregator.add("\"" + name + "\", ");
        subAggregator.add(Long.toString(flags) + ", ");
        subAggregator.add(Long.toString(adjectiveCategoryIndex) + ", ");        
        subAggregator.add(Long.toString(precedence) + ", ");       
        subAggregator.add(Long.toString(rankInCategory) + ", ");
        
        if (conflictIndices != null) {
            subAggregator.add("[" + StringAggregator.aggregate(conflictIndices) + "]");
        } else {
            subAggregator.add("[]");
        }
        
        subAggregator.pop();
        
        aggregator.add(subAggregator.toString());
    }
}
