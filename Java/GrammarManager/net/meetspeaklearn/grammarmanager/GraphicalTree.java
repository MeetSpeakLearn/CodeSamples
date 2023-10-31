/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author steve
 */
public class GraphicalTree extends JPanel implements IGraphicalTree {
    private ITreeNode externalRoot;
    private GraphicalTreeNode root;
    private Shape nodeShape = Shape.OVAL;
    private Color backgroundColor = Color.BLUE;
    private Color borderColor = Color.BLACK;
    private Color foregroundColor = Color.WHITE;
    private int borderThickness = 3;
    private boolean preventPaint = false;
    
    public GraphicalTree() {
        super();
    }
    
    public GraphicalTree(ITreeNode root) {
        this();
        setRoot(root);
    }
    
    public void paint(Graphics g) {
        if (preventPaint) return;
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        // My code
        
        if (root == null) return;
        
        root.connectNode();
        
        if (!root.getFrozen()) root.setFrozen(true);
        
        root.paintNode();
    }
    /*
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        // My code
        
        if (root == null) return;
        
        root.connectNode();
        
        if (!root.getFrozen()) root.setFrozen(true);
        
        root.paintNode();
    }
    */
    public ITreeNode getRoot() {
        return externalRoot;
    }
    
    private GraphicalTreeNode mimic(ITreeNode theirs) {
        GraphicalTreeNode mine = new GraphicalTreeNode(this, theirs);
        ArrayList<ITreeNode> theirChildren = theirs.getChildNodes();
        int theirChildrenSize = theirChildren.size();
        
        if (theirChildrenSize == 0) return mine;
        
        ArrayList<ITreeNode> myChildren = mine.getChildNodes();
        
        for (ITreeNode theirChild : theirChildren) {
            ITreeNode myChild = mimic(theirChild);
            ArrayList<ITreeNode> myChildsParents = myChild.getParentNodes();
            myChildsParents.add(mine);
            myChildren.add(myChild);
        }
        
        return mine;
    }
    
    public IGraphicalTree setRoot(ITreeNode rootNode) {
        System.out.println("setRoot(): node=" + rootNode);
        externalRoot = rootNode;
        this.root = mimic(externalRoot);
        // preventPaint = false;
        this.paint(getGraphics());
        // preventPaint = true;
        
        return this;
    }

    public Shape getNodeShape() {
        return nodeShape;
    }

    public void setNodeShape(Shape nodeShape) {
        this.nodeShape = nodeShape;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
    }
}
