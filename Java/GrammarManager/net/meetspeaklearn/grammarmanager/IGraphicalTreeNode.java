/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import javax.swing.JPanel;

/**
 *
 * @author steve
 */
public interface IGraphicalTreeNode extends ITreeNode {
    public JPanel getPanel();
    
    public Shape getShape();
    public IGraphicalTreeNode setShape(Shape shape);
    
    public int getWidth();
    public IGraphicalTreeNode setWidth(int w);
    
    public int getHeight();
    public IGraphicalTreeNode setHeight(int h);
    
    public int getX();
    public IGraphicalTreeNode setX(int x);
    
    public int getY();
    public IGraphicalTreeNode setY(int y);
    
    public IGraphicalTreeNode setXY(int x, int y);
    
    public int getTreeWidth();
    public int getTreeHeight();
}
