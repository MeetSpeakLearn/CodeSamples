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
public class AdjectiveCategoryMap {
    public static interface ManagerToCompilerCategoryAdapter {
        public Set<net.meetspeaklearn.grammarmanager.Adjective> merge(Set<net.meetspeaklearn.grammarmanager.Adjective> adjectives);
    }
    
    protected HashMap<String, String> managerNameToExampleNameMap;
    protected HashMap<String, ArrayList<String>> exampleNameToOrderedManagerNameMap;
    protected ArrayList<String> orderedExampleNameList;
    protected ArrayList<AdjectiveCategory> orderedCategoryList;
    protected HashMap<String, AdjectiveCategory> exampleNameToCategoryMap;
    protected HashMap<net.meetspeaklearn.grammarmanager.Adjective, Adjective> managerAdjectiveToCompilerAdjectiveMap;
    protected HashMap<Adjective, net.meetspeaklearn.grammarmanager.Adjective> compilerAdjectiveToManagerAdjectiveMap;
    protected ObjectManager om;
    
    public AdjectiveCategoryMap (ObjectManager om) {
        this.om = om;
        managerAdjectiveToCompilerAdjectiveMap = new HashMap<net.meetspeaklearn.grammarmanager.Adjective, Adjective>();
        compilerAdjectiveToManagerAdjectiveMap = new HashMap<Adjective, net.meetspeaklearn.grammarmanager.Adjective>();
    }

    public ObjectManager getOm() {
        return om;
    }

    public void setOm(ObjectManager om) {
        this.om = om;
    }
    
    private int computeCompilerAdjectivePrecendenceFromManagerCategoryName(String mName) {
        String cName = managerNameToExampleNameMap.get(mName);
        int index = orderedExampleNameList.indexOf(cName);
        
        if (index == -1) {
            System.err.println("AdjectiveCategoryMap.computeCompilerAdjectivePrecendenceFromManagerCategoryName(" + mName
                    + "): No compiler adjective category map declared for manager adjectival category \"" + mName + "\".");
            return -1;
        }
        
        return index;
    }
    
    private int computeCompilerAdjectiveRank(String cName, String mName) {
        ArrayList<String> orderedManagerNameList = exampleNameToOrderedManagerNameMap.get(cName);
        return orderedManagerNameList.indexOf(mName);
    }
    
    public void add(String exampleName, List<String> orderedManagerNames) {
        add(exampleName, orderedManagerNames.toArray(new String[0]));
    }
    
    public void add(String exampleName, String[] orderedManagerNames) {
        if (orderedExampleNameList == null) {
            orderedExampleNameList = new ArrayList<String>();
        }
        
        if (orderedCategoryList == null) {
            orderedCategoryList = new ArrayList<AdjectiveCategory>();
        }
        
        orderedExampleNameList.add(exampleName);
        
        AdjectiveCategory adjCat = new AdjectiveCategory(om);
        adjCat.setName(exampleName);
        adjCat.setPrecedence(orderedCategoryList.size());
        orderedCategoryList.add(adjCat);
        exampleNameToCategoryMap.put(exampleName, adjCat);
        
        if (managerNameToExampleNameMap == null) {
            managerNameToExampleNameMap = new HashMap<String, String>();
        }
        
        if (exampleNameToOrderedManagerNameMap == null) {
            exampleNameToOrderedManagerNameMap = new HashMap<String, ArrayList<String>>();
        }
        
        for (String managerName : orderedManagerNames) {
            managerNameToExampleNameMap.put(managerName, exampleName);
            if (! exampleNameToOrderedManagerNameMap.containsKey(exampleName)) {
                exampleNameToOrderedManagerNameMap.put(exampleName, new ArrayList<String>());
            }
            ArrayList<String> orderedManagerNamesForExample = exampleNameToOrderedManagerNameMap.get(exampleName);
            orderedManagerNamesForExample.add(managerName);
        }
    }
    
    public void add(net.meetspeaklearn.grammarmanager.Adjective managerAdjective) {
        Adjective compilerAdjective = managerAdjectiveToCompilerAdjectiveMap.get(managerAdjective);
        net.meetspeaklearn.grammarmanager.AdjectivalCategory mCategory = managerAdjective.getCategory();
        String managerCategoryName = mCategory.getName();
        String compilerCategoryName = managerNameToExampleNameMap.get(managerCategoryName);
        
        if (compilerAdjective == null) {
            compilerAdjective = new Adjective(om);
            compilerAdjective.setName(managerAdjective.getText());
            compilerAdjective.setPrecedence(computeCompilerAdjectivePrecendenceFromManagerCategoryName(managerCategoryName));
            compilerAdjective.setRankInCategory(computeCompilerAdjectiveRank(compilerCategoryName, managerCategoryName));
            compilerAdjective.setGender(managerAdjective.getGender());
            compilerAdjective.setType(managerAdjective.getType());
            compilerAdjective.setPlurality(managerAdjective.getPlurality());
            managerAdjectiveToCompilerAdjectiveMap.put(managerAdjective, compilerAdjective);
            compilerAdjectiveToManagerAdjectiveMap.put(compilerAdjective, managerAdjective);
        }
        
        
        if (compilerCategoryName == null) {
            System.err.println("AdjectiveCategoryMap.add(" + managerAdjective
                    + "): No compiler adjective category map declared for manager adjectival category \"" + managerCategoryName + "\".");
            return;
        }
        
        AdjectiveCategory cCategory = exampleNameToCategoryMap.get(compilerCategoryName);
        
        if (cCategory == null) {
            System.err.println("AdjectiveCategoryMap.add(" + managerAdjective
                    + "): No compiler category found for manager adjectival category \"" + managerCategoryName + "\".");
            return;
        }
        
        cCategory.addAdjective(compilerAdjective);
        compilerAdjective.setAdjectiveCategoryIndex(orderedCategoryList.indexOf(cCategory));
    }
    
    public void add(Collection<net.meetspeaklearn.grammarmanager.Adjective> managerAdjectives) {
        for (net.meetspeaklearn.grammarmanager.Adjective mAdj : managerAdjectives) {
            add(mAdj);
        }
    }
    
    public void link(NounMapper nounMapper) {
        ArrayList<Adjective> adjectives = om.getAdjectives();
        
        for (Adjective cAdj : adjectives) {
            net.meetspeaklearn.grammarmanager.Adjective mAdj = compilerAdjectiveToManagerAdjectiveMap.get(cAdj);
            net.meetspeaklearn.grammarmanager.Adjective[] mConflicts = mAdj.getConflictsAsArray();
            ArrayList<Adjective> cConflicts = new ArrayList<Adjective>();
            
            for (net.meetspeaklearn.grammarmanager.Adjective mConflict : mConflicts) {
                Adjective cConflict = managerAdjectiveToCompilerAdjectiveMap.get(mConflict);
                
                // It is not impossible (and may even be likely) that some conflicting adjectives
                // are not in our set of adjectives. This is OK because the adjectives in our set
                // wear chosen based on their relationships to a set of nouns. If an adjective in
                // the manager set of conflicts is not in our set, we simply leave it out.
                
                if (cConflict != null) {
                    cConflicts.add(cConflict);
                }
            }
            
            int cConflictCount = cConflicts.size();
            int mConflictsArray[] = new int[cConflictCount];
            
            for (int i = 0; i < cConflictCount; i++)
                mConflictsArray[i] = cConflicts.get(i).getIndex();
            
            Arrays.sort(mConflictsArray);
            
            cAdj.setConflictIndices(mConflictsArray);
        }
        
        for (net.meetspeaklearn.grammarmanager.Noun mNoun : nounMapper.getManagerNouns()) {
            Noun cNoun = nounMapper.getAssociatedCompilerNoun(mNoun);
            Set<net.meetspeaklearn.grammarmanager.Adjective> mNounAdjectives = nounMapper.getNounAdjectives(mNoun);
            
            if (mNounAdjectives != null) {            
                for (net.meetspeaklearn.grammarmanager.Adjective mAdj : mNounAdjectives) {
                    Adjective cAdj = managerAdjectiveToCompilerAdjectiveMap.get(mAdj);
                    nounMapper.associate(cNoun, cAdj);
                }
            }
        }
        
        ArrayList<Adjective> orderedCompilerAdjects = om.getAdjectives();
        
        for (Noun cNoun : nounMapper.getCompilerNouns()) {
            Set<Adjective> cAdjectiveSet = nounMapper.getNounAdjectives(cNoun);
            
            if (cAdjectiveSet != null) {
                int count = cAdjectiveSet.size();
                int[] indexArray = new int[count];
                int i = 0;

                for (Adjective cAdj : cAdjectiveSet) {
                    int index = orderedCompilerAdjects.indexOf(cAdj);
                    indexArray[i++] = index;
                }

                Arrays.sort(indexArray);

                cNoun.setAdjectiveIndices(indexArray);
            }
        }
    }
}
