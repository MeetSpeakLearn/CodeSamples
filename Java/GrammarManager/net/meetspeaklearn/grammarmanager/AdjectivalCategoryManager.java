/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

/**
 *
 * @author steve
 */
public class AdjectivalCategoryManager {
    private HashMap<String, AdjectivalCategory> categoriesMap;
    private ArrayList<AdjectivalCategory> categoriesByPrecedence;
    
    public AdjectivalCategoryManager() {
    }
    
    public AdjectivalCategoryManager(String[] categoryNames) {
        categoriesMap = new HashMap<String, AdjectivalCategory>();
        categoriesByPrecedence = new ArrayList<AdjectivalCategory>();
        addCategories(categoryNames);
    }

    public HashMap<String, AdjectivalCategory> getCategoriesMap() {
        return categoriesMap;
    }

    public void setCategoriesMap(HashMap<String, AdjectivalCategory> categoriesMap) {
        this.categoriesMap = categoriesMap;
    }

    public ArrayList<AdjectivalCategory> getCategoriesByPrecedence() {
        return categoriesByPrecedence;
    }
    
    public ArrayList<String> getCategoryNamesByPrecedence() {
        ArrayList<String> categoryNames = new ArrayList<String>();
        
        if (categoriesByPrecedence == null) return categoryNames;
        
        for (AdjectivalCategory cat : categoriesByPrecedence)
            categoryNames.add(cat.getName());
        
        return categoryNames;
    }

    public void setCategoriesByPrecedence(ArrayList<AdjectivalCategory> categoriesByPrecedence) {
        this.categoriesByPrecedence = categoriesByPrecedence;
    }
    
    public void addCategories(String[] names) {
        for (String name : names) addCategory(name);
    }
    
    public void addCategories(ArrayList<String> names) {
        for (String name : names) addCategory(name);
    }
    
    public void addCategory(String name) {
        if ((categoriesMap == null) || (categoriesByPrecedence == null)) {
            categoriesMap = new HashMap<String, AdjectivalCategory>();
            categoriesByPrecedence = new ArrayList<AdjectivalCategory>();
        }
        
        AdjectivalCategory category = new AdjectivalCategory(name);
        
        categoriesMap.put(name, category);
        categoriesByPrecedence.add(category);
    }
    
    public ArrayList<String> getCategoryNames() {
        if (categoriesByPrecedence == null) return new ArrayList<String>();
        
        ArrayList<String> result = new ArrayList<String>();
        
        for (AdjectivalCategory cat : categoriesByPrecedence) {
            result.add(cat.getName());
        }
        
        return result;
    }
    
    public int getCategoryCount() {
        if (categoriesByPrecedence == null) return 0;
        
        return categoriesByPrecedence.size();
    }
    
    public AdjectivalCategory getCategoryByName(String name) {
        return categoriesMap.get(name);
    }
    
    public void mergeIfPossible(AdjectivalCategory orphan) {
        String orphanName = orphan.getName();
        AdjectivalCategory interned = getCategoryByName(orphanName);
        
        if (interned == null) return;
        
        HashSet<Adjective> nonorphanedAdjectives = interned.getAdjectives();
        
        if (nonorphanedAdjectives == null) {
            nonorphanedAdjectives = new HashSet<Adjective>();
            interned.setAdjectives(nonorphanedAdjectives);
        }
        
        if (orphan != interned) {
            HashSet<Adjective> orphanedAdjectives = orphan.getAdjectives();
            
            for (Adjective orphanedAdj : orphanedAdjectives) {
                if (! nonorphanedAdjectives.contains(orphanedAdj)) {
                    orphanedAdj.setCategory(interned);
                    System.err.println("Merging!\n");
                    nonorphanedAdjectives.add(orphanedAdj);
                }
            }
        }
    }
    
    public void synchronizeAdjectivalCategories(String[] names) {
        int namesCount = names.length;
        
        if (namesCount == 0) {
            if (categoriesMap != null) categoriesMap.clear();
            if (categoriesByPrecedence != null) categoriesByPrecedence.clear();
            return;
        }
        
        if ((categoriesMap == null) || (categoriesByPrecedence == null)) {
            addCategories(names);
            return;
        }
        
        ArrayList<AdjectivalCategory> editedCategoriesByPrecedence = new ArrayList<AdjectivalCategory>();
        
        for (String catName : names) {
            if (categoriesMap.containsKey(catName)) {
                editedCategoriesByPrecedence.add(categoriesMap.get(catName));
            } else {
                AdjectivalCategory newCategory = new AdjectivalCategory(catName);
                editedCategoriesByPrecedence.add(newCategory);
            }
        }
        
        categoriesMap.clear();
        categoriesByPrecedence.clear();
        
        categoriesByPrecedence.addAll(editedCategoriesByPrecedence);
        
        for (AdjectivalCategory adjCat : editedCategoriesByPrecedence) {
            categoriesMap.put(adjCat.getName(), adjCat);
        }
    }
    
    public void synchronizeAdjectivalCategories(ArrayList<String> names) {
        int namesCount = names.size();
        
        if (namesCount == 0) {
            if (categoriesMap != null) categoriesMap.clear();
            if (categoriesByPrecedence != null) categoriesByPrecedence.clear();
            return;
        }
        
        if ((categoriesMap == null) || (categoriesByPrecedence == null)) {
            addCategories(names);
            return;
        }
        
        ArrayList<AdjectivalCategory> editedCategoriesByPrecedence = new ArrayList<AdjectivalCategory>();
        
        for (String catName : names) {
            if (categoriesMap.containsKey(catName)) {
                editedCategoriesByPrecedence.add(categoriesMap.get(catName));
            } else {
                AdjectivalCategory newCategory = new AdjectivalCategory(catName);
                editedCategoriesByPrecedence.add(newCategory);
            }
        }
        
        categoriesMap.clear();
        categoriesByPrecedence.clear();
        
        categoriesByPrecedence.addAll(editedCategoriesByPrecedence);
        
        for (AdjectivalCategory adjCat : editedCategoriesByPrecedence) {
            categoriesMap.put(adjCat.getName(), adjCat);
        }
    }
    
    public HashSet<Adjective> getAllAdjectives() {
        HashSet<Adjective> allAdjectives = new HashSet<Adjective>();
        
        for (AdjectivalCategory cat : categoriesByPrecedence) {
            allAdjectives.addAll(cat.getAdjectives());
        }
        
        return allAdjectives;
    }
    
    public HashSet<Adjective> getAllAdjectivesWithinAcademicRange(AcademicLevel lowerBound, AcademicLevel upperBound) {
        HashSet<Adjective> allAdjectives = new HashSet<Adjective>();
        
        for (AdjectivalCategory cat : categoriesByPrecedence) {
            allAdjectives.addAll(cat.getAdjectivesWithinAcademicRange(lowerBound, upperBound));
        }
        
        return allAdjectives;
    }
    
    /*
    public void setAllAdjectivesOfCategoryToType (String categoryName, Adjective.ADJECTIVE_TYPE type) {
        AdjectivalCategory category = getCategoryByName(categoryName);
        
        if (category == null) return;
        
        for (Adjective adj : category.getAdjectives())
            adj.setType(type);
    }
*/
}
