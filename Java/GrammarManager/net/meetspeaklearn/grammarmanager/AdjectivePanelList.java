/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author steve
 */
public class AdjectivePanelList extends javax.swing.JPanel {
    private GrammarManangerFrame gmf;
    private Adjective selectedAdjective;
    private ArrayList<AdjectivePanel> panels;
    private ArrayList<Adjective> adjs;
    boolean selectingAdjectiveFromOutside;
    boolean selectingAdjectiveFromInside;
    
    private final static Color SELECTED_COLOR = new Color(0, 0, 255);
    private final static Color UNSELECTED_COLOR = new Color(0, 0, 0);
    private final static Color BACKGROUND_COLOR = new Color(255, 255, 255);
    
    class AdjectivePanel extends JPanel {
        Adjective adj;
        boolean selected;
        JPanel adjectivePanel;
        JLabel ajdNameLabel, adjName, catNameLabel, catName;
        
        public AdjectivePanel() {
            ajdNameLabel = new JLabel();
            adjName = new JLabel();
            catNameLabel = new JLabel();
            catName = new JLabel();

            this.setPreferredSize(new java.awt.Dimension(381, 80));
            this.setLayout(new java.awt.FlowLayout());

            ajdNameLabel.setText("adj:");
            this.add(ajdNameLabel);

            adjName.setFont(new java.awt.Font("Tahoma", 1, 14));
            adjName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            adjName.setText("adjective");
            this.add(adjName);

            catNameLabel.setText("category:");
            this.add(catNameLabel);

            catName.setFont(new java.awt.Font("Tahoma", 1, 14));
            catName.setText("category");
            this.add(catName);
            
            this.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    panelMouseClicked(evt);
                }
            });

            // this.add(infoPanel, java.awt.BorderLayout.NORTH);
        }
        
        public AdjectivePanel(Adjective adj) {
            this();
            
            this.adj = adj;
            
            adjName.setText(adj.getLexeme().getLexeme());
            
            if (adj.getCategory() != null) {
                catName.setText(adj.getCategory().getName());
            } else {
                catName.setText("<undefined>");
            }

            selected = false;
        }

        public Adjective getAdj() {
            return adj;
        }

        public void setAdj(Adjective adj) {
            this.adj = adj;
        }

        public JPanel getAdjectivePanel() {
            return adjectivePanel;
        }

        public void setAdjectivePanel(JPanel adjectivePanel) {
            this.adjectivePanel = adjectivePanel;
        }

        public JLabel getAjdNameLabel() {
            return ajdNameLabel;
        }

        public void setAjdNameLabel(JLabel ajdNameLabel) {
            this.ajdNameLabel = ajdNameLabel;
        }

        public JLabel getAdjName() {
            return adjName;
        }

        public void setAdjName(JLabel adjName) {
            this.adjName = adjName;
        }

        public JLabel getCatNameLabel() {
            return catNameLabel;
        }

        public void setCatNameLabel(JLabel catNameLabel) {
            this.catNameLabel = catNameLabel;
        }

        public JLabel getCatName() {
            return catName;
        }

        public void setCatName(JLabel catName) {
            this.catName = catName;
        }
        
        public void setSelected(boolean selected) {
            setSelected(selected, true);
        }
        
        public void setSelected(boolean selected, boolean repaint) {
            this.selected = selected;
            
            if (selected) {
                for (AdjectivePanel p : panels)
                    if (p != this)
                        p.setSelected(false);
                
                selectedAdjective = adj;
                this.setBorder(new javax.swing.border.LineBorder(SELECTED_COLOR, 4, true));
                gmf.adjectivalCategoryPanelListAdjectiveSelected(selectedAdjective);
            
                if (repaint) {
                    this.revalidate();
                }
            } else {
                selectedAdjective = null;
                this.setBorder(new javax.swing.border.LineBorder(UNSELECTED_COLOR, 4, true));
            }
        }
        
        private void panelMouseClicked(java.awt.event.MouseEvent evt) {                                             
            this.setSelected(true);
        }
    }

    /**
     * Creates new form AdjectivePanelList
     */
    public AdjectivePanelList() { // Main
        initComponents();
        
        panels = new ArrayList<AdjectivePanel>();
        adjs = new ArrayList<Adjective>();
        selectedAdjective = null;
        selectingAdjectiveFromOutside = false;
        selectingAdjectiveFromInside = false;
    }
    
    public AdjectivePanelList(GrammarManangerFrame gmf) {
        this();
        this.gmf = gmf;
    }
    
    public void clear() {
        System.out.println("clearing adjectives list panel");
        
        for (AdjectivePanel p : panels) {
            adjectivesPanel.remove(p);
            p.invalidate();
        }
        
        panels.clear();
        adjs.clear();
        adjectivesPanel.invalidate();
        this.getParent().invalidate();
    }
    
    public void setAdjectives(ArrayList<Adjective> adjs) {
        System.out.println("setAdjectives(): adjs=" + adjs);
        if (panels.size() > 0) {
            for (AdjectivePanel p : panels) adjectivesPanel.remove(p);
            panels.clear();
            adjs.clear();
        }
        
        for (Adjective a : adjs) {
            addAdjective(a, false);
        }
        
        adjectivesPanel.revalidate();
    }
    
    public void addAdjective(Adjective adj) {
        addAdjective(adj, true);
    }
    
    private void addAdjective(Adjective adj, boolean repaint) {
        AdjectivePanel panel = new AdjectivePanel(adj);
        adjs.add(adj);
        panels.add(panel);
        adjectivesPanel.add(panel);
        panel.setSelected(true, repaint);
    }
    
    private void removeAdjective(Adjective adj) {
        int index = adjs.indexOf(adj);
        
        if (index == -1) return;
        
        int indexOfSelectedAdjective = (selectedAdjective != null) ? adjs.indexOf(selectedAdjective) : -1;
        AdjectivePanel panelToRemove = panels.get(index);
        
        if (selectedAdjective == adj) {
            panels.get(indexOfSelectedAdjective).setSelected(false, false);
            indexOfSelectedAdjective = -1;
        }
        
        adjs.remove(adj);
        this.remove(panelToRemove);
        panels.remove(panelToRemove);
        
        if (indexOfSelectedAdjective != -1) {
            if (indexOfSelectedAdjective < adjs.size()) {
                panels.get(indexOfSelectedAdjective).setSelected(true, false);
            }
        } else {
            gmf.adjectivalCategoryPanelListAdjectiveSelected(null);
        }
        
        adjectivesPanel.revalidate();
    }
    
    private void selectAdjectiveUnprotected(Adjective adj) {
        for (AdjectivePanel p : panels)
            if (p.getAdj() == adj) {
                p.setSelected(true);
                return;
            }
    }
    
    public void selectAdjective(Adjective adj) {
        if (selectingAdjectiveFromOutside) return; // Prevent a cycle of selection.
        
        selectingAdjectiveFromOutside = true;
        selectAdjectiveUnprotected(adj);
        selectingAdjectiveFromOutside = false;
    }
    
    private void selectedAdjectiveFromInside(Adjective adj) {
        if (selectingAdjectiveFromInside) return; // Prevent a cycle of selection.
        
        selectingAdjectiveFromInside = true;
        selectAdjectiveUnprotected(adj);
        selectingAdjectiveFromInside = false;
    }
    
    public void updateFromOutside(Adjective adj) {
        if ((adj == null) || (adjs.size() == 0)) return;
        
        System.out.println("adjs.size()=" + adjs.size());
        
        int indexOfAdj = -1;
        int i = 0;
        
        for (Adjective a : adjs) {
            if (a == adj) {
                indexOfAdj = i;
                break;
            }
            i += 1;
        }
        
        if (indexOfAdj == -1) return;
        
        System.out.println("indexOfAdj=" + indexOfAdj);
        
        AdjectivePanel p = panels.get(indexOfAdj);
        
        p.adjName.setText(adj.getLexeme().getLexeme());
        
        if (adj.getCategory() != null)
            p.catName.setText(adj.getCategory().getName());
        else
            p.catName.setText("<undefined>");
        
        p.revalidate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPanel = new javax.swing.JScrollPane();
        adjectivesPanel = new javax.swing.JPanel();

        setBackground(new java.awt.Color(0, 153, 153));
        setPreferredSize(new java.awt.Dimension(202, 400));
        setLayout(new java.awt.BorderLayout());

        scrollPanel.setHorizontalScrollBar(null);

        adjectivesPanel.setBackground(new java.awt.Color(0, 153, 153));
        adjectivesPanel.setPreferredSize(new java.awt.Dimension(200, 800));
        adjectivesPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adjectivesPanelMouseClicked(evt);
            }
        });
        adjectivesPanel.setLayout(new java.awt.GridLayout(20, 1, 10, 4));
        scrollPanel.setViewportView(adjectivesPanel);

        add(scrollPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void adjectivesPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adjectivesPanelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_adjectivesPanelMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel adjectivesPanel;
    private javax.swing.JScrollPane scrollPanel;
    // End of variables declaration//GEN-END:variables
}
