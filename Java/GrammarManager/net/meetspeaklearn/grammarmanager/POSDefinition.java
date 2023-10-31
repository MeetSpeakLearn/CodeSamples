/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

/**
 *
 * @author steve
 */
public class POSDefinition {
    private PartOfSpeech what;
    private String text;
    
    public POSDefinition() {
        
    }
    
    public POSDefinition(PartOfSpeech pos, String definition) {
        this.what = pos;
        this.text = definition;
    }

    public PartOfSpeech getWhat() {
        return what;
    }

    public void setWhat(PartOfSpeech what) {
        this.what = what;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public String toString() {
        GrammarObject.GO_CLASS type = what.getGoClass();
        String posType = type.toString();
        
        if (text != null) {
            return posType + ": " + text;
        } else {
            return posType;
        }
    }
}
