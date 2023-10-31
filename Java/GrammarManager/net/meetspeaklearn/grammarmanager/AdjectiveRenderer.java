/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author steve
 */
public class AdjectiveRenderer extends DefaultTableCellRenderer {
        public AdjectiveRenderer() {
            super();
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            Adjective adj = (Adjective) value;

            if (adj != null) {
                this.setText(adj.getText());
            } else
                this.setText("");
            
            addListeners(this);

            return this;
         }
        
        private void addListeners(Component c) {
            MouseAdapter[] listeners = c.getListeners(MouseAdapter.class);
            
            if ((listeners != null) ? (listeners.length > 0) : false) {
                System.out.println("addListeners(): listeners=" + listeners);                
            } else {
                c.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        tableCellMouseEntered(evt);
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        tableCellMouseExited(evt);
                    }
                });
            }
        }
        
        private void switchColors(JLabel c) {
            Color fore = c.getForeground();
            Color back = c.getBackground();
            
            c.setForeground(back);
            c.setBackground(fore);
        }
        
        private void tableCellMouseEntered(java.awt.event.MouseEvent evt) {
            System.out.println("tableCellMouseEntered()");
            JLabel label = (JLabel) evt.getComponent();
            switchColors(label);
        }
        
        private void tableCellMouseExited(java.awt.event.MouseEvent evt) {
            JLabel label = (JLabel) evt.getComponent();
            switchColors(label);
        }
    }