/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import java.util.ArrayList;
import static net.meetspeaklearn.grammarmanager.WorkingDataManager.currentWorkingDataManager;
import static net.meetspeaklearn.grammarmanager.WorkingDataManager.workingLexicon;

/**
 *
 * @author steve
 */
public class AdjectiveMaintenanceInformation {
    private AdjectiveMaintenancePanel panel;
    private Adjective adj = null;
    private boolean hasCategory = false;
    private boolean categoryIsOrphaned = false;
    private AdjectivalCategory category;
    private boolean hasConflictingAdjectives = false;
    private Adjective[] conflictsArray;
    private boolean otherAdjectivesConflict = false;
    private boolean anotherCategoryPointsToThis = false;
    
    public AdjectiveMaintenanceInformation(Adjective adj) {
        this.adj = adj;
        category = adj.getCategory();
        this.panel = null;
    }
    
    public AdjectiveMaintenanceInformation(Adjective adj, AdjectiveMaintenancePanel panel) {
        this.adj = adj;
        category = adj.getCategory();
        this.panel = panel;
    }
    
    public void destroyAdjective() {
        if (hasCategory) {
            if (otherAdjectivesConflict) {
                for (Adjective other : category.getAdjectives()) {
                    if (other != adj) {
                        Adjective[] otherConflicts = category.getConflictsAsArray(other);
                        for (Adjective conflictingAdj : otherConflicts) {
                            if (conflictingAdj == adj) {
                                category.removeMutalConflict(adj, other);
                            }
                        }
                    }
                }
            }
        }
        
        if (anotherCategoryPointsToThis) {
            AdjectivalCategoryManager currentManager = workingLexicon.getAdjCatManager();
            ArrayList<AdjectivalCategory> categories = currentManager.getCategoriesByPrecedence();

            if (categories != null) {

                for (AdjectivalCategory cat : categories) {
                    if (cat.getAdjectives().contains(adj)) {
                        cat.removeCategory(adj);
                    }
                }
            }
        }
        
        adj.getLexeme().destroyAdjective(adj);
    }
    
    public void evaluateStatus() {
        if (category == null) {
            hasCategory = false;
            categoryIsOrphaned = false;
        } else {
            categoryIsOrphaned = category.isOrphaned();
            
            conflictsArray = category.getConflictsAsArray(adj);
            
            if (conflictsArray != null) {
                if (conflictsArray.length > 0) {
                    hasConflictingAdjectives = true;
                }
            }
            
            for (Adjective other : category.getAdjectives()) {
                if (other != adj) {
                    Adjective[] otherConflicts = category.getConflictsAsArray(other);
                    for (Adjective conflictingAdj : otherConflicts) {
                        if (conflictingAdj == adj) {
                            otherAdjectivesConflict = true;
                            break;
                        }
                    }
                    
                    if (otherAdjectivesConflict) break;
                }
            }
        }
        
        // If categoryIsOrphaned find out if some other category points to this adj.
        
        if (categoryIsOrphaned) {
            AdjectivalCategoryManager currentManager = workingLexicon.getAdjCatManager();
            ArrayList<AdjectivalCategory> categories = currentManager.getCategoriesByPrecedence();

            if (categories != null) {

                for (AdjectivalCategory cat : categories) {
                    if (cat.getAdjectives().contains(adj)) {
                        anotherCategoryPointsToThis = true;
                        break;
                    }
                }
            }
        }
        
        System.err.println(this.toString());
        
        if (panel != null) panel.updateStatus(this);
    }

    public Adjective getAdj() {
        return adj;
    }

    public boolean isHasCategory() {
        return hasCategory;
    }

    public boolean isCategoryIsOrphaned() {
        return categoryIsOrphaned;
    }

    public AdjectivalCategory getCategory() {
        return category;
    }

    public boolean isHasConflictingAdjectives() {
        return hasConflictingAdjectives;
    }

    public Adjective[] getConflictsArray() {
        return conflictsArray;
    }

    public boolean isOtherAdjectivesConflict() {
        return otherAdjectivesConflict;
    }

    public boolean isAnotherCategoryPointsToThis() {
        return anotherCategoryPointsToThis;
    }
    
    public String toString() {
        String result = "AdjectiveMaintenanceInformation:{";
        result += "adj=" + adj + ", ";
        result += "hasCategory=" + hasCategory + ", ";
        result += "categoryIsOrphaned=" + categoryIsOrphaned + ", ";
        result += "hasConflictingAdjectives=" + hasConflictingAdjectives + ", ";
        result += "otherAdjectivesConflict=" + otherAdjectivesConflict + ", ";
        result += "anotherCategoryPointsToThis=" + anotherCategoryPointsToThis + "}";
        
        return result;
    }
}
