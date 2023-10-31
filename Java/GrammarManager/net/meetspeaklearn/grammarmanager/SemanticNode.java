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
public interface SemanticNode {
    public void associate_with(PartOfSpeech pos);
    public PartOfSpeech get_part_of_speech();
    
    public void is_a(SemanticNode object);
    public void has(SemanticNode object);
    public void is_able_to(SemanticNode action);
    public void is_modified_by(SemanticNode adjectiveOrAdverb);
    
    public void remove_is_a(SemanticNode object);
    
    public boolean is_an_object();
    public boolean is_an_action();
    public boolean is_an_adjective();
    public boolean is_an_adverb();
    
    public SemanticNode[] get_things_I_am();
    public SemanticNode[] get_things_I_have();
    public SemanticNode[] get_my_abilities();
    public SemanticNode[] get_my_modifiers();
    
    public SemanticNode[] get_things_I_specialize();
    public SemanticNode[] get_thing_I_generalize();
}
