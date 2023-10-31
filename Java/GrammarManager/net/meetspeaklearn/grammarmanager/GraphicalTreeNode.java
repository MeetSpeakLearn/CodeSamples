/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import java.awt.geom.Ellipse2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author steve
 */
public class GraphicalTreeNode implements IGraphicalTreeNode {
    private final static int horizonalGap = 20;
    private final static int verticalGap = 100;
    private final static int defaultWidth = 100;
    private final static int defaultHeight = 50;
    
    private final static int leftMargin = 200;
    private final static int topMargin = 100;
    
    private int x, y, width, height;
    private int cachedTreeWidth;
    private Shape shape;
    private JPanel panel;
    private GraphicalTree gt;
    private ITreeNode externalNode;
    private ArrayList<ITreeNode> childNodes;
    private ArrayList<ITreeNode> parentNodes;
    private boolean isFrozen = false;
    
    public GraphicalTreeNode(GraphicalTree gt) {
        this.gt = gt;
        panel = gt;
        shape = Shape.OVAL;
        width = defaultWidth;
        height = defaultHeight;
    }
    
    public GraphicalTreeNode(GraphicalTree gt, ITreeNode externalNode) {
        this(gt);
        this.externalNode = externalNode;
    }
    
    public boolean getFrozen() {
        return isFrozen;
    }
    
    public void setFrozen(boolean frozen) {
        if (childNodes != null) {
            for (ITreeNode child : childNodes)
                ((GraphicalTreeNode) child).setFrozen(frozen);
        }
        
        isFrozen = frozen;
    }
    
    public void paintNode() {
        Graphics2D g2d = (Graphics2D) gt.getGraphics();
        int myX = leftMargin + getX();
        int myY = topMargin + getY();
        String label = ((Noun) externalNode.getContent()).getLexeme().getLexeme();
        
        if (label.length() > 16) {
            label = label.substring(0, 16);
        }
        
        int labelOffset = ((defaultWidth / 2) / 16) * (16 - label.length());
        
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("Serif", Font.BOLD, 12);
        // g2d.setStroke(new BasicStroke(1));
        // g2d.setColor(Color.WHITE);
        
        switch (shape) {
            case CIRCLE: {
                Ellipse2D.Double circle = new Ellipse2D.Double(myX, myY, defaultWidth, defaultWidth);
                // g2d.draw(circle);
                g2d.setColor(Color.BLUE);
                g2d.fill(circle);
                break;
            }
            case OVAL:
                Ellipse2D.Double circle = new Ellipse2D.Double(myX, myY, defaultWidth, defaultHeight);
                // g2d.draw(circle);
                g2d.setColor(Color.BLUE);
                g2d.fill(circle);
                break;
            case SQUARE:
            case TRIANGLE:
        }
        
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        g2d.drawString(label, myX + labelOffset + 3, myY + defaultHeight / 2 + 1);
        
        if (childNodes != null)
            for (ITreeNode child : childNodes) {
                ((GraphicalTreeNode) child).paintNode();
            }
    }
    
    public void connectNode() {
        Graphics2D g2d = (Graphics2D) gt.getGraphics();
        int myX = leftMargin + getX();
        int myY = topMargin + getY();
        
        if (parentNodes != null) {
            if (parentNodes.size() > 0) {
                GraphicalTreeNode parent = (GraphicalTreeNode) parentNodes.get(0);
                int parentCenterX = leftMargin + parent.getX() + defaultWidth / 2;
                int parentCenterY = topMargin + parent.getY() + defaultHeight / 2;
                int myCenterX = myX + defaultWidth / 2;
                int myCenterY = myY + defaultHeight / 2;
        
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(Color.RED);
                g2d.drawLine(myCenterX, myCenterY, parentCenterX, parentCenterY);
            }
        }
        
        if (childNodes != null)
            for (ITreeNode child : childNodes) {
                ((GraphicalTreeNode) child).connectNode();
            }
    }

    public ITreeNode getExternalNode() {
        return externalNode;
    }

    public void setExternalNode(ITreeNode externalNode) {
        this.externalNode = externalNode;
    }
    
    public Object getContent() {
        return this;
    }
    
    public ArrayList<ITreeNode> getChildNodes() {
        if (childNodes == null) {
            childNodes = new ArrayList<ITreeNode>();
        }
        
        return childNodes;
    }
    
    public ArrayList<ITreeNode> getParentNodes() {
        if (parentNodes == null) {
            parentNodes = new ArrayList<ITreeNode>();
        }
        
        return parentNodes;
    }
    
    public JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
        }
        
        return panel;
    }
    
    public Shape getShape() {
        if (shape == null) {
            shape = Shape.CIRCLE;
        }
        
        return shape;
    }
    
    public IGraphicalTreeNode setShape(Shape shape) {
        this.shape = shape;
        
        return this;
    }
    
    public int getWidth() {
        return width;
    }
    
    public IGraphicalTreeNode setWidth(int w) {
        width = w;
        return this;
    }
    
    public int getHeight() {
        return height;
    }
    
    public IGraphicalTreeNode setHeight(int h) {
        height = h;
        return this;
    }
    
    public int getX() {
        if (isFrozen) return x;
        
        if (parentNodes != null) {
            if (parentNodes.size() > 0) {
                GraphicalTreeNode parent = (GraphicalTreeNode) parentNodes.get(0);
                ArrayList<ITreeNode> siblings = parent.getChildNodes();
                int accumulatedWidthOfLeftSiblings = 0;
                
                for (ITreeNode sibling : siblings) {
                    if (sibling == this) break;
                    accumulatedWidthOfLeftSiblings += ((GraphicalTreeNode) sibling).getTreeWidth();
                }
                
                accumulatedWidthOfLeftSiblings += this.getTreeWidth() / 2;
                
                int parentX = parent.getX();
                int parentWidth = parent.getTreeWidth();
                // int parentCenterX = parentX + parentWidth / 2;
                // int siblingCount = siblings.size();
                // int myIndex = siblings.indexOf(this);
                // int siblingZoneWidth = parentWidth / siblingCount;
                
                return x = (parentX - parentWidth / 2) + accumulatedWidthOfLeftSiblings; //  + (siblingZoneWidth / 2);
            }
        }
        
        return x = getTreeWidth() / 2;
    }
    
    public IGraphicalTreeNode setX(int x) {
        this.x = x;
        return this;
    }
    
    public int getY() {
        if (isFrozen) return y;
        
        if (parentNodes != null) {
            if (parentNodes.size() > 0) {
                GraphicalTreeNode parent = (GraphicalTreeNode) parentNodes.get(0);
                ArrayList<ITreeNode> siblings = parent.getChildNodes();
                int parentY = parent.getY();
                
                return y = parentY + defaultHeight + verticalGap;
            }
        }
        
        return y = 0;
    }
    
    public IGraphicalTreeNode setY(int y) {
        this.y = y;
        return this;
    }
    
    public IGraphicalTreeNode setXY(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    public int getTreeWidth() {
        if (isFrozen) return cachedTreeWidth;
        
        ArrayList<ITreeNode> children = getChildNodes();
        int childCount = children.size();
        
        if (childCount == 0) return getWidth() + horizonalGap;
        
        int treeWidth = 0;
        
        for (ITreeNode n : getChildNodes()) {
            treeWidth += ((IGraphicalTreeNode) n).getTreeWidth();
        }
        
        /*
        if (childCount > 0) {
            treeWidth += ((childCount - 1) * horizonalGap);
        }
        */
        
        return cachedTreeWidth = treeWidth;
    }
    
    public int getTreeHeight() {
        ArrayList<ITreeNode> children = getChildNodes();
        int childCount = children.size();
        
        if (childCount == 0) return getHeight();
        
        int treeWidth = 0;
        
        for (ITreeNode n : getChildNodes()) {
            treeWidth += ((IGraphicalTreeNode) n).getTreeHeight();
        }
        
        if (childCount > 0) {
            treeWidth += ((childCount - 1) * verticalGap);
        }
        
        return treeWidth;
        
    }
}
