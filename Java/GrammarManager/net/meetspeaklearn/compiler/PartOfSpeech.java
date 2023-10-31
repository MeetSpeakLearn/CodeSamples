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
public class PartOfSpeech extends ManagedObject {
    protected final static long TYPE_MASK      = 0b0000000000001111;
    protected final static long GENDER_MASK    = 0b0000000001110000;
    protected final static long PLURALITY_MASK = 0b0000001110000000;
    
    protected final static long UNKNOWN        = 0b0000000000000000;
    
    protected final static long MASCULINE      = 0b0000000000010000;
    protected final static long FEMININE       = 0b0000000000100000;
    protected final static long NEUTER         = 0b0000000000110000;
    protected final static long ANIMATE        = 0b0000000001000000;
    protected final static long INANIMATE      = 0b0000000001010000;
    protected final static long COMMON         = 0b0000000001100000;
    
    protected final static long UNKNOWN_PLURALITY  = 0b0000000010000000;
    protected final static long SINGULAR_ONLY      = 0b0000000100000000;
    protected final static long PLURAL_ONLY        = 0b0000000110000000;
    protected final static long SINGULAR_OR_PLURAL = 0b0000001000000000;
    
    protected String name;
    protected long flags;
    
    public PartOfSpeech() {
        super();
        flags = 0;
    }
    
    public PartOfSpeech(int index, ObjectManager om) {
        super(index, om);
        flags = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFlags() {
        return flags;
    }

    public void setFlags(byte flags) {
        this.flags = flags;
    }
    
    public long getGender() {
        return (long) (flags & GENDER_MASK);
    }
    
    public void setGender(long gender) {
        flags = (long) ((flags & ~GENDER_MASK) | gender);
    }
    
    public long getPlurality() {
        return (long) (flags & PLURALITY_MASK);
    }
    
    public void setPlurality(long plurality) {
        flags = (long) ((flags & ~PLURALITY_MASK) | plurality);
    }
    
    public void setPlurality(net.meetspeaklearn.grammarmanager.Adjective.PLURALITY plurality) {
        switch (plurality) {
            case UNKNOWN:
                setPlurality(UNKNOWN_PLURALITY);
                break;
            case SINGULAR_ONLY:
                setPlurality(SINGULAR_ONLY);
                break;
            case PLURAL_ONLY:
                setPlurality(PLURAL_ONLY);
                break;
            case SINGULAR_OR_PLURAL:
                setPlurality(SINGULAR_OR_PLURAL);
                break;
            default:
                setPlurality(SINGULAR_OR_PLURAL);
        }
    }
    
    public void setGender(net.meetspeaklearn.grammarmanager.Gender gender) {
        switch (gender) {
            case UNKNOWN: setGender(UNKNOWN); break;
            case MASCULINE: setGender(MASCULINE); break;
            case FEMININE: setGender(FEMININE); break;
            case NEUTER: setGender(NEUTER); break;
            case ANIMATE: setGender(ANIMATE); break;
            case INANIMATE: setGender(INANIMATE); break;
            case COMMON: setGender(COMMON); break;
            default:                
        }
    }
    
    public boolean isUnknownGender() {
        return (flags & GENDER_MASK) == UNKNOWN;
    }
    
    public boolean isMasculine() {
        return (flags & GENDER_MASK) == MASCULINE;
    }
    
    public boolean isFeminine() {
        return (flags & GENDER_MASK) == FEMININE;
    }
    
    public boolean isNeuter() {
        return (flags & GENDER_MASK) == NEUTER;
    }
    
    public boolean isAnimate() {
        return (flags & GENDER_MASK) == ANIMATE;
    }
    
    public boolean isInanimate() {
        return (flags & GENDER_MASK) == INANIMATE;
    }
    
    public boolean isCommon() {
        return (flags & GENDER_MASK) == COMMON;
    }
}
