/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.compiler;

/**
 *
 * @author steve
 */
public class ManagedObject {
    protected ObjectManager om;
    protected int index;
    protected boolean interned;
    
    public ManagedObject() {
        interned = false;
    }
    
    public ManagedObject(int index) {
        this();
        this.index = index;
    }
    
    public ManagedObject(int index, ObjectManager om) {
        this();
        this.index = index;
        this.om = om;
    }

    public ObjectManager getOm() {
        return om;
    }

    public void setOm(ObjectManager om) {
        this.om = om;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        interned = true;
    }

    public boolean isInterned() {
        return interned;
    }

    public void setInterned(boolean interned) {
        this.interned = interned;
    }
    
    public Noun noun(int i) {
        return om.getNoun(i);
    }
    
    public Adjective adj(int i) {
        return om.getAdj(i);
    }
    
    public AdjectiveCategory cat(int i) {
        return om.getAdjCat(i);
    }
    
}
