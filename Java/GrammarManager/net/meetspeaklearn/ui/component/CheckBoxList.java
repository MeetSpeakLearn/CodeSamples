/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.ui.component;

import net.meetspeaklearn.util.listener.IListItemSelected;
import net.meetspeaklearn.util.listener.IListItemSelectedListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.*;
import javax.swing.JCheckBox;

/**
 *
 * @author steve
 */
public class CheckBoxList
        extends javax.swing.JPanel
        implements IListItemSelected {
    
    protected static class Triplet {
        public JCheckBox cb;
        public Object value;
        public String text;
        
        public Triplet() {
            cb = null;
            value = null;
            text = null;
        }
        
        public Triplet(JCheckBox cb) {
            this.cb = cb;
        }
        
        public Triplet(JCheckBox cb, Object value, String text) {
            this.cb = cb;
            this.value = value;
            this.text = text;
        }
    }
    
    protected static class TripletComparator implements Comparator<Triplet> {
        private TripletComparator() {}
        public int compare(Triplet o1, Triplet o2) {
            return o1.text.compareToIgnoreCase(o2.text);
        }
    }
    
    protected static TripletComparator tripletComparator = new TripletComparator();
    
    protected Color bgColor = new Color(240, 240, 240);
    protected Color fgColor = new Color(0, 0, 0);
    protected GridLayout layout = new GridLayout(1, 1, 2, 4);
    protected Font myFont = new java.awt.Font("Tahoma", 1, 12);
    protected boolean sortByText = true;
    protected Dimension itemDimensions = new Dimension(100, 24);
    
    protected ArrayList<Triplet> items = new ArrayList<Triplet>();
    protected ArrayList<IListItemSelectedListener> itemSelectedListeners
            = new ArrayList<IListItemSelectedListener>();

    /**
     * Creates new form CheckBoxList
     */
    public CheckBoxList() {
        super();
        initComponents();
        initMoreComponents();
    }
    
    public CheckBoxList(LayoutManager lm) {
        super(lm);
        initComponents();
        initMoreComponents();
    }
    
    public CheckBoxList(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        initComponents();
        initMoreComponents();
    }
    
    public CheckBoxList (LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        initComponents();
        initMoreComponents();
    }
    
    public void addListener(IListItemSelectedListener listener) {
        itemSelectedListeners.add(listener);
    }
    
    public void removeListener(IListItemSelectedListener listener) {
        itemSelectedListeners.remove(listener);
    }
    
    protected void initMoreComponents() {
        this.items = new ArrayList<Triplet>();
        this.itemSelectedListeners = new ArrayList<IListItemSelectedListener>();
        checkBoxPanel.setVisible(true);
        // checkBoxPanel.setLayout(layout);
        checkBoxPanel.setBackground(bgColor);
        checkBoxPanel.setForeground(bgColor);
    }
    
    public void setMyBackground(Color c) {
        checkBoxPanel.setBackground(c);
        bgColor = c;
        
        for (Triplet item : this.items) {
            item.cb.setBackground(bgColor);
        }
    }
    
    public void setMyForeground(Color c) {
        checkBoxPanel.setForeground(c);
        fgColor = c;
        
        for (Triplet item : this.items) {
            item.cb.setBackground(fgColor);
        }
    }
    
    public void setMyFont(Font myFont) {
        checkBoxPanel.setFont(myFont);
        this.myFont = myFont;
        
        for (Triplet item : this.items) {
            item.cb.setFont(this.myFont);
        }
    }
    
    public void setItemSize(Dimension d) {
        int itemCount = this.items.size();
        Dimension panelSize = new Dimension(d.width, (d.height + 4) * itemCount);
        
        itemDimensions = d;
        
        checkBoxPanel.setPreferredSize(panelSize);
        
        for (Triplet item : this.items) {
            item.cb.setPreferredSize(itemDimensions);
        }
    }
    
    public void setSortByText(boolean sortByText) {
        this.sortByText = sortByText;
    }
    
    protected void addListenersToItem(Triplet item) {
        JCheckBox cb = item.cb;
        
        cb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                for (IListItemSelectedListener listener : itemSelectedListeners) {
                    listener.itemSelected(item.value, cb.isSelected());
                }
            }
        });
    }
    
    public void addItem(String text, Object value) {
        addItem(text, value, false);
    }
    
    public void addItem(String text, Object value, boolean isChecked) {
        JCheckBox cb = new JCheckBox();
        checkBoxPanel.add(cb);
        
        int itemCount = this.items.size();
        Triplet item;
        
        cb.setText(text);
        cb.setSelected(false);
        cb.setPreferredSize(itemDimensions);
        cb.setFont(myFont);
        // cb.setBackground(bgColor);
        cb.setBackground(Color.GREEN);
        cb.setForeground(fgColor);
        // setPreferredSize(new Dimension(itemDimensions.width, (itemDimensions.height + 4) * (itemCount + 1)));
        // layout.setRows(itemCount + 1);
        // layout = new GridLayout(itemCount + 1, 1, 2, 4);
        // checkBoxPanel.setLayout(layout);
        
        item = new Triplet(cb, value, text);
        this.items.add(item);
        
        addListenersToItem(item);
        
        System.out.println("Added item");
        
        revalidate();
    }
    
    public void removeItem(Object value) {
        int indexOfItem = this.items.indexOf(value);
        
        if (indexOfItem == -1) return;
        
        Triplet item = this.items.get(indexOfItem);
        JCheckBox cb = item.cb;
        int itemCount = this.items.size();
        
        checkBoxPanel.setPreferredSize(new Dimension(itemDimensions.width, (itemDimensions.height + 4) * Math.min(itemCount - 1, 1)));
        layout.setRows(Math.min(itemCount - 1, 1));
        
        this.items.remove(item);
        
        addListenersToItem(item);
        
        checkBoxPanel.remove(cb);
    }
    
    public ArrayList<Object> getSelectedValues() {
        ArrayList<Object> selectedValues = new ArrayList<Object>();
        
        for (Triplet item : this.items) {
            if (item.cb.isSelected()) selectedValues.add(item.value);
        }
        
        return selectedValues;
    }
    
    public ArrayList<Object> getUnselectedValues() {
        ArrayList<Object> selectedValues = new ArrayList<Object>();
        
        for (Triplet item : this.items) {
            if (! item.cb.isSelected()) selectedValues.add(item.value);
        }
        
        return selectedValues;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        templateCheckBox = new javax.swing.JCheckBox();
        checkBoxPanel = new javax.swing.JPanel();

        templateCheckBox.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        templateCheckBox.setText("text");
        templateCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                templateCheckBoxItemStateChanged(evt);
            }
        });
        templateCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                templateCheckBoxStateChanged(evt);
            }
        });
        templateCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                templateCheckBoxActionPerformed(evt);
            }
        });

        setBackground(new java.awt.Color(204, 204, 255));
        setMinimumSize(new java.awt.Dimension(180, 600));
        setPreferredSize(new java.awt.Dimension(180, 600));
        setLayout(new java.awt.BorderLayout(10, 10));

        checkBoxPanel.setPreferredSize(new java.awt.Dimension(1000, 4000));
        checkBoxPanel.setLayout(new java.awt.GridLayout(100, 8, 10, 10));
        add(checkBoxPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void templateCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_templateCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_templateCheckBoxActionPerformed

    private void templateCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_templateCheckBoxItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_templateCheckBoxItemStateChanged

    private void templateCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_templateCheckBoxStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_templateCheckBoxStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel checkBoxPanel;
    private javax.swing.JCheckBox templateCheckBox;
    // End of variables declaration//GEN-END:variables
}
