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
 * Note: A merged adjective does not permanently exist within the overall
 * structure of the ontology. It is a transient object that is used to
 * map ontology Adjectives to compiled adjectives.
 */
public class MergedAdjective extends Adjective {
    private ArrayList<AdjectivalCategory> categories;
    private String mergedCategoryName;
    
    private HashSet<Adjective> subjects;
    
    public MergedAdjective() {
        super();
        categories = new ArrayList<AdjectivalCategory>();
        subjects = new HashSet<Adjective>();
    }

    public ArrayList<AdjectivalCategory> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<AdjectivalCategory> categories) {
        this.categories = categories;
    }

    public String getMergedCategoryName() {
        return mergedCategoryName;
    }

    public void setMergedCategoryName(String mergedCategoryName) {
        this.mergedCategoryName = mergedCategoryName;
    }
    
    public MergedAdjective add(Adjective subject) {
        subjects.add(subject);
        return this;
    }
    
    public HashSet<Adjective> getDirectSubjects() {
        return subjects;
    }
    
    public HashSet<Adjective> getAllSubjects() {
        HashSet<Adjective> allSubjects = new HashSet<Adjective>();
        
        for (Adjective subject : subjects) {
            if (subject instanceof MergedAdjective) {
                allSubjects.addAll(((MergedAdjective) subject).getAllSubjects());
            }
            else {
                allSubjects.add(subject);
            }
        }
        
        return allSubjects;
    }
}
