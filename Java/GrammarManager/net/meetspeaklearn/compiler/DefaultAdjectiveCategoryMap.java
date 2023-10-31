/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 *
 * @author steve
 */
public class DefaultAdjectiveCategoryMap extends AdjectiveCategoryMap {
    private static String[] managerCategoryNames = new String[] {
        "article", "demonstrative", "possessive", "quantifier", "determiner",
        "subjective opinion", "objective opinion", "taste", "smell",
        "observed emotional state", "actual emotional state",
        "sentient state", "observed intellectual state",
        "actual intellectual state", "personal character opinion",
        "size", "weight", "physical activity", "speed", "odor",
        "physical quality", "shape", "age",
        "attribute of color", "color", "time", "relative distance",
        "place", "district", "nation", "community", "material", "type",
        "culinary purpose", "conveyance purpose", "purpose"};
    
    private static String[] compilerCategoryNames = new String[] {
        "article", "demonstrative", "determiner", "possessive", "quantifier", "opinion", "size", "physical quality",
        "shape", "age", "color", "origin", "material", "type", "purpose"
    };
    
    public static LinkedHashMap<String, ArrayList<String>> mapStructure;
    
    static {
        mapStructure = new LinkedHashMap<String, ArrayList<String>>();
        
        for (String name : compilerCategoryNames)
            mapStructure.put(name, new ArrayList<String>());
        
        Collections.addAll(mapStructure.get("article"),
                new String[] { "article" });
        
        Collections.addAll(mapStructure.get("demonstrative"),
                new String[] { "demonstrative" });
        
        Collections.addAll(mapStructure.get("determiner"),
                new String[] { "determiner" });
        
        Collections.addAll(mapStructure.get("possessive"),
                new String[] { "possessive" });
        
        Collections.addAll(mapStructure.get("quantifier"),
                new String[] { "quantifier" });
        
        Collections.addAll(mapStructure.get("opinion"),
                new String[] { "subjective opinion", "objective opinion",
                                "taste", "smell", "observed emotional state",
                                "actual emotional state", "sentient state",
                                "observed intellectual state", "actual intellectual state",
                                "personal character opinion" });
        
        Collections.addAll(mapStructure.get("size"),
                new String[] { "size", "weight" });
        
        Collections.addAll(mapStructure.get("physical quality"),
                new String[] { "speed", "odor", "physical quality" });
        
        Collections.addAll(mapStructure.get("shape"),
                new String[] { "shape" });
        
        Collections.addAll(mapStructure.get("age"),
                new String[] { "age" });
        
        Collections.addAll(mapStructure.get("color"),
                new String[] { "attribute of color", "color" });
        
        Collections.addAll(mapStructure.get("origin"),
                new String[] { "time", "relative distance", "place", "district", "nation", "community" });
        
        Collections.addAll(mapStructure.get("material"),
                new String[] { "material" });
        
        Collections.addAll(mapStructure.get("type"),
                new String[] { "type", "physical activity" });
        
        Collections.addAll(mapStructure.get("purpose"),
                new String[] { "culinary purpose", "conveyance purpose", "purpose" });
    }
    
    public static class CategoryAdapter implements ManagerToCompilerCategoryAdapter {
        private LinkedHashMap<String, ArrayList<net.meetspeaklearn.grammarmanager.Adjective>> mergedAdjectiveBuckets;
        private HashMap<String, LinkedHashMap<String, ArrayList<net.meetspeaklearn.grammarmanager.Adjective>>> unmergedTomergedAdjectiveBuckets;
        
        public CategoryAdapter() {
            mergedAdjectiveBuckets = new LinkedHashMap<String, ArrayList<net.meetspeaklearn.grammarmanager.Adjective>>();
            unmergedTomergedAdjectiveBuckets
                    = new HashMap<String, LinkedHashMap<String, ArrayList<net.meetspeaklearn.grammarmanager.Adjective>>>();
            
            mapStructure.forEach((cCat, mCats) -> {
                ArrayList<net.meetspeaklearn.grammarmanager.Adjective> emptyMergedList
                        = new ArrayList<net.meetspeaklearn.grammarmanager.Adjective>();
                mergedAdjectiveBuckets.put(cCat, emptyMergedList);
                
                mCats.forEach((mCat) -> {
                    LinkedHashMap<String, ArrayList<net.meetspeaklearn.grammarmanager.Adjective>> bucket
                            = new LinkedHashMap<String, ArrayList<net.meetspeaklearn.grammarmanager.Adjective>>();
                    bucket.put(mCat, emptyMergedList);
                    unmergedTomergedAdjectiveBuckets.put(mCat, bucket);
                });
            });
        }
        
        public Set<net.meetspeaklearn.grammarmanager.Adjective> merge(Set<net.meetspeaklearn.grammarmanager.Adjective> adjectives) {
            HashSet<net.meetspeaklearn.grammarmanager.Adjective> result =
                    new HashSet<net.meetspeaklearn.grammarmanager.Adjective>();
            // Place all adjectives into their corresponding compiler buckets.
            for (net.meetspeaklearn.grammarmanager.Adjective adj : adjectives) {
                net.meetspeaklearn.grammarmanager.AdjectivalCategory cat = adj.getCategory();
                String catName = cat.getName();
                LinkedHashMap<String, ArrayList<net.meetspeaklearn.grammarmanager.Adjective>> bucket = unmergedTomergedAdjectiveBuckets.get(catName);
                ArrayList<net.meetspeaklearn.grammarmanager.Adjective> compilerBucket = bucket.get(catName);
                compilerBucket.add(adj);
            }
            
            mergedAdjectiveBuckets.forEach((cCat, compilerBucket) -> {
                HashMap<net.meetspeaklearn.grammarmanager.Lex, ArrayList<net.meetspeaklearn.grammarmanager.Adjective>> lexemeMap
                        = new HashMap<net.meetspeaklearn.grammarmanager.Lex, ArrayList<net.meetspeaklearn.grammarmanager.Adjective>>();
                for (net.meetspeaklearn.grammarmanager.Adjective adj : compilerBucket) {
                    ArrayList<net.meetspeaklearn.grammarmanager.Adjective> mergedBucket
                            = lexemeMap.get(adj.getLexeme());
                    
                    if (mergedBucket == null) {
                        mergedBucket = new ArrayList<net.meetspeaklearn.grammarmanager.Adjective>();
                        lexemeMap.put(adj.getLexeme(), mergedBucket);
                    }
                    
                    mergedBucket.add(adj);
                }
                
                for (net.meetspeaklearn.grammarmanager.Lex lexeme : lexemeMap.keySet()) {
                    ArrayList<net.meetspeaklearn.grammarmanager.Adjective> unmergedAdjs = lexemeMap.get(lexeme);
                    
                    if (unmergedAdjs.size() > 1) {
                        net.meetspeaklearn.grammarmanager.MergedAdjective mergedAdj = new net.meetspeaklearn.grammarmanager.MergedAdjective();
                        mergedAdj.setLexeme(lexeme);
                        
                        for (net.meetspeaklearn.grammarmanager.Adjective unmergedAdj : unmergedAdjs) {
                            mergedAdj.add(unmergedAdj);
                        }

                        result.add(mergedAdj);
                    } else {
                        result.add(unmergedAdjs.get(0));
                    }
                }
            });
            
            return result;
        }
    }
    
    public DefaultAdjectiveCategoryMap (ObjectManager om) {
        super(om);
        
        managerNameToExampleNameMap = new HashMap<String, String>();
        exampleNameToOrderedManagerNameMap = new HashMap<String, ArrayList<String>>();
        orderedExampleNameList = new ArrayList<String>();
        orderedCategoryList = new ArrayList<AdjectiveCategory>();
        exampleNameToCategoryMap = new HashMap<String, AdjectiveCategory>();
        
        mapStructure.forEach((cCat, mCats) -> {
            add(cCat, mCats);
        });
        
        /*
        add("article", new String[] {"article"});
        add("demonstrative", new String[] {"demonstrative"});
        add("possessive", new String[] {"possessive"});
        add("quantifier", new String[] {"quantifier"});
        add("determiner", new String[] {"determiner", "possessive"});
        add("opinion", new String[] {"subjective opinion", "objective opinion",
            "taste", "smell", "observed emotional state",
            "actual emotional state", "sentient state",
            "observed intellectual state", "actual intellectual state",
            "personal character opinion"});
        add("size", new String[] {"size", "weight"});
        add("physical quality", new String[] {"physical activity", "speed", "odor", "physical quality"});
        add("shape", new String[] {"shape"});
        add("age", new String[] {"age"});
        add("color", new String[] {"attribute of color", "color"});
        add("origin", new String[] {"time", "relative distance",
            "place", "district", "nation", "community"});
        add("material", new String[] {"material"});
        add("type", new String[] {"type"});
        add("purpose", new String[] {"culinary purpose", "conveyance purpose", "purpose"});
        */
    }
    
}
