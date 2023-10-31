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
public interface IGraphicalTree {
    public ITreeNode getRoot();
    public IGraphicalTree setRoot(ITreeNode rootNode);
}
