/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author steve
 */
public class EditLanguageAdjectivalCategoriesPanel extends javax.swing.JPanel {
    private final static Color SELECTED_COLOR = new Color(255, 0, 0);
    private final static Color UNSELECTED_COLOR = new Color(0, 0, 0);
    private final static Color BACKGROUND_COLOR = new Color(255, 255, 255);
    
    private ArrayList<CategoryPanel> categories;
    private CategoryPanel selectedCategory;
    
    enum MOVE_DIRECTION {
        UP, DOWN
    }
    
    class CategoryPanel extends JPanel {
        private JLabel nameLabel;
        private JLabel precedenceLabel;
        protected boolean selected;
        
        public CategoryPanel() {
            selected = false;
            
            nameLabel = new javax.swing.JLabel();
            precedenceLabel = new javax.swing.JLabel();
        
            this.setBackground(BACKGROUND_COLOR);
            this.setBorder(new javax.swing.border.LineBorder(UNSELECTED_COLOR, 4, true));
            this.setPreferredSize(new java.awt.Dimension(200, 100));
            this.setLayout(new java.awt.BorderLayout());

            nameLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            nameLabel.setText("category");
            nameLabel.setPreferredSize(new java.awt.Dimension(200, 24));
            this.add(nameLabel, java.awt.BorderLayout.CENTER);

            precedenceLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            precedenceLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            precedenceLabel.setText("0");
            precedenceLabel.setPreferredSize(new java.awt.Dimension(50, 24));
            
            this.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    categoryPanelMouseClicked(evt);
                }
            });
        
            this.add(precedenceLabel, java.awt.BorderLayout.EAST);
            
        }
        
        public String getNameLabelText() {
            return nameLabel.getText();
        }
        
        public void setNameLabelText(String name) {
            nameLabel.setText(name);
        }
        
        public int getPrecedence() {
            return Integer.parseInt(precedenceLabel.getText());
        }
        
        public void setPrecedence(int precedence) {
            precedenceLabel.setText(Integer.toString(precedence));
        }
        
        public void setSelected(boolean value) {
            if (value) {
                for (CategoryPanel category : categories) {
                    if (category.selected) {
                        category.setSelected(false);
                    }
                }
                
                this.setBorder(new javax.swing.border.LineBorder(SELECTED_COLOR, 4, true));
                selected = true;
                selectedCategory = this;
                deleteButton.setEnabled(true);
                upButton.setEnabled(true);
                downButton.setEnabled(true);
            } else {
                this.setBorder(new javax.swing.border.LineBorder(UNSELECTED_COLOR, 4, true));
                if (selectedCategory == this) {
                    selectedCategory = null;
                    deleteButton.setEnabled(false);
                    upButton.setEnabled(false);
                    downButton.setEnabled(false);
                }
                selected = false;
            }
        }
        
        private void categoryPanelMouseClicked(java.awt.event.MouseEvent evt) {
            CategoryPanel categoryPanel = (CategoryPanel) evt.getComponent();
            categoryPanel.setSelected(true);
        }
    }

    /**
     * Creates new form EditLanguageAdjectivalCategoriesPanel
     */
    public EditLanguageAdjectivalCategoriesPanel() {
        initComponents();
        
        categories = new ArrayList<CategoryPanel>();
        selectedCategory = null;
        addButton.setEnabled(false);
        deleteButton.setEnabled(false);
        upButton.setEnabled(false);
        downButton.setEnabled(false);
            
            // Initialize categories
            
        Language language = WorkingDataManager.currentWorkingDataManager.GetLanguage(WorkingDataManager.workingLanguage);
        ArrayList<String> categoriesFromLanguageAsArrayList = language.getAdjectivalCatgeoriesByPrecedence();
        String[] categoriesFromLanguage = (categoriesFromLanguageAsArrayList != null)
                ? categoriesFromLanguageAsArrayList.toArray(new String[0])
                : new String[0];

        for (String categoryName : categoriesFromLanguage)
            this.addCategory(categoryName);
        
        selectedCategory.setSelected(false);
    }
    
    private boolean categoryAlreadyAdded(String name) {
        for (CategoryPanel p : categories) {
            if (name.compareToIgnoreCase(p.getNameLabelText()) == 0)
                return true;
        }
        
        return false;
    }
    
    private CategoryPanel addCategory(String name) {
        // first, make sure the name is not already used.
        if (categoryAlreadyAdded(name)) return null;
        
        int count = categories.size();
        CategoryPanel newPanel = new CategoryPanel();
        
        newPanel.setNameLabelText(name);
        newPanel.setPrecedence(count + 1);
        categories.add(newPanel);
        categoriesPanel.add(newPanel);
        categoriesPanel.revalidate();
        
        newPanel.setSelected(true);
        
        synchronizeLanguageCategories();
        
        return newPanel;
    }
    
    private CategoryPanel removeCategory() {
        CategoryPanel toBeRemoved = selectedCategory;
        
        selectedCategory.setSelected(false);
        upButton.setEnabled(false);
        downButton.setEnabled(false);
        
         // Remove all categories from panel.
        for (CategoryPanel category : categories)
            categoriesPanel.remove(category);
        
        categories.remove(toBeRemoved);
        
        // Re-add all categories to panel.
        int newPrecedence = 1;
        
        for (CategoryPanel category : categories) {
            category.setPrecedence(newPrecedence++);
            categoriesPanel.add(category);
        }
        
        categoriesPanel.paintAll(categoriesPanel.getGraphics());
        
        if (categories.isEmpty()) {
            deleteButton.setEnabled(false);
            upButton.setEnabled(false);
            downButton.setEnabled(false);
        }
        
        synchronizeLanguageCategories();
        
        return toBeRemoved;
    }
    
    private void moveCategory(MOVE_DIRECTION direction) {
        int count = categories.size();
        int beforePosition = selectedCategory.getPrecedence() - 1;
        int afterPosition = (direction == MOVE_DIRECTION.UP)
                ? beforePosition - 1
                : beforePosition + 1;
        
        if ((afterPosition < 0) || (afterPosition >= count)) return;
        
        // Remove all categories from panel.
        for (CategoryPanel category : categories)
            categoriesPanel.remove(category);
        
        categories.remove(selectedCategory);
        categories.add(afterPosition, selectedCategory);
        
        // Re-add all categories to panel.
        int newPrecedence = 1;
        
        for (CategoryPanel category : categories) {
            category.setPrecedence(newPrecedence++);
            categoriesPanel.add(category);
        }
        
        synchronizeLanguageCategories();
        
        categoriesPanel.revalidate();
    }
    
    private void synchronizeLanguageCategories() {
        Language language = WorkingDataManager.currentWorkingDataManager.GetLanguage(WorkingDataManager.workingLanguage);
        int categoryCount = categories.size();
        String[] categoryNames = new String[categoryCount];
        int i = 0;
        
        for (CategoryPanel current : categories)
            categoryNames[i++] = current.getNameLabelText();
        
        language.addAdjectivalCategories(categoryNames);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        categoriesPanel = new javax.swing.JPanel();
        topPanel = new javax.swing.JPanel();
        newCategoryNameTextField = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        bottomPanel = new javax.swing.JPanel();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(204, 204, 255));
        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setPreferredSize(new java.awt.Dimension(500, 300));
        setLayout(new java.awt.BorderLayout());

        mainPanel.setLayout(new java.awt.BorderLayout());

        categoriesPanel.setBackground(new java.awt.Color(204, 204, 255));
        categoriesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Adjectival Categories"));
        categoriesPanel.setLayout(new java.awt.GridLayout(20, 1, 2, 2));
        mainPanel.add(categoriesPanel, java.awt.BorderLayout.CENTER);

        topPanel.setBackground(new java.awt.Color(204, 204, 255));

        newCategoryNameTextField.setPreferredSize(new java.awt.Dimension(120, 24));
        newCategoryNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCategoryNameTextFieldActionPerformed(evt);
            }
        });
        newCategoryNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                newCategoryNameTextFieldKeyReleased(evt);
            }
        });
        topPanel.add(newCategoryNameTextField);

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        topPanel.add(addButton);

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        topPanel.add(deleteButton);

        mainPanel.add(topPanel, java.awt.BorderLayout.NORTH);

        bottomPanel.setBackground(new java.awt.Color(204, 204, 255));

        upButton.setText("Up");
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });
        bottomPanel.add(upButton);

        downButton.setText("Down");
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });
        bottomPanel.add(downButton);

        mainPanel.add(bottomPanel, java.awt.BorderLayout.SOUTH);

        add(mainPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        String newCategoryName = newCategoryNameTextField.getText().trim();
        
        if (newCategoryName.compareTo("") == 0) return;
        
        addCategory(newCategoryName);
        newCategoryNameTextField.setText("");
        addButton.setEnabled(false);
    }//GEN-LAST:event_addButtonActionPerformed

    private void newCategoryNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newCategoryNameTextFieldActionPerformed
        String newCategoryName = newCategoryNameTextField.getText().trim();
        
        if (newCategoryName.compareTo("") == 0) return;
        
        addCategory(newCategoryName);
        newCategoryNameTextField.setText("");
        addButton.setEnabled(false);
    }//GEN-LAST:event_newCategoryNameTextFieldActionPerformed

    private void newCategoryNameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newCategoryNameTextFieldKeyReleased
        String newCategoryName = newCategoryNameTextField.getText().trim();
        
        addButton.setEnabled(newCategoryName.compareTo("") != 0);
    }//GEN-LAST:event_newCategoryNameTextFieldKeyReleased

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        if (selectedCategory != null) {
            moveCategory(MOVE_DIRECTION.UP);
        }
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        if (selectedCategory != null) {
            moveCategory(MOVE_DIRECTION.DOWN);
        }
    }//GEN-LAST:event_downButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if (selectedCategory != null) {
            removeCategory();
        }
    }//GEN-LAST:event_deleteButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JPanel categoriesPanel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton downButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField newCategoryNameTextField;
    private javax.swing.JPanel topPanel;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables
}
