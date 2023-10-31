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
public class ProtoNoun extends PartOfSpeech {
    protected Lex lexeme;
    
    public ProtoNoun() {
        goClass = GrammarObject.GO_CLASS.PROTO_NOUN;
    }
    
    public ProtoNoun(Lex lexeme) {
        this.lexeme = lexeme;
    }
    
    public Lex getLexeme() {
        return this.lexeme;
    }
    
    public void setLexeme(Lex lexeme) {
        this.lexeme = lexeme;
    }
}
