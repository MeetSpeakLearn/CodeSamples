/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;

/**
 *
 * @author steve
 */
public abstract class GrammarObject {
    public enum GO_CLASS {
        UNKNOWN("Unknown"),
        LANGUAGE("Language"),
        LEXICON("Lexicon"),
        LEX("LEX"),
        PART_OF_SPEECH("PartOfSpeech"),
        PROTO_NOUN("ProtoNoun"),
        NOUN("Noun"),
        ADJECTIVE("Adjective"),
        ADVERB("Adverb"),
        VERB("Verb");
        private String type;
        
        GO_CLASS(String type) {
            this.type = type;
        }
        
        public String toString() {
            return this.type;
        }
    }
    
    protected GO_CLASS goClass = GO_CLASS.UNKNOWN;
    
    public GrammarObject() {
    }
    
    public GO_CLASS getGoClass() {
        return this.goClass;
    }
    
    public static GrammarObject instantiate(GO_CLASS goClass) {
        switch (goClass) {
            // PART_OF_SPEECH - do not instantiate.
            // PROTO_NOUN - do not instantiate.
            case LANGUAGE: return new Language();
            case LEXICON: return new Lexicon();
            case LEX: return new Lex();
            case NOUN: return new Noun();
            case ADJECTIVE: return new Adjective();
            case ADVERB: return new Adverb();
            case VERB: return new Verb();
            default: return null;
        }
    }
    
    public static Object decode(XMLDecoder decoder) {
        try {
            Object decodedObject = decoder.readObject();
            
            return (Language) decodedObject;
        } catch(ArrayIndexOutOfBoundsException e1) {
            return null;
        } catch (Exception e2) {
            System.err.println(e2.toString());
            return null;
        }
    }
    
    public void encode(XMLEncoder encoder) {
        encoder.writeObject(this);
    }
    
    public boolean isA(GO_CLASS goClass) {
        return this.goClass == goClass;
    }
}
