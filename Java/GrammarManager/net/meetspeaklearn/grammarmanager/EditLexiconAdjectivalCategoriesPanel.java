/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author steve
 */
public class EditLexiconAdjectivalCategoriesPanel extends javax.swing.JPanel {
    private final static Color SELECTED_COLOR = new Color(255, 0, 0);
    private final static Color UNSELECTED_COLOR = new Color(0, 0, 0);
    private final static Color BACKGROUND_COLOR = new Color(255, 255, 255);
    
    private ArrayList<CategoryPanel> categories;
    private CategoryPanel selectedCategory;
    private AdjectivalCategory currentCategory;
    private boolean userInteraction = false;
    
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
                AdjectivalCategoryManager adjCatManager = WorkingDataManager.workingLexicon.getAdjCatManager();
                
                for (CategoryPanel category : categories) {
                    if (category.selected) {
                        category.setSelected(false);
                    }
                }
                
                this.setBorder(new javax.swing.border.LineBorder(SELECTED_COLOR, 4, true));
                selected = true;
                selectedCategory = this;
                currentCategory = adjCatManager.getCategoryByName(getNameLabelText());
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
                currentCategory = null;
            }
            
            selectionChanged();
        }
        
        private void categoryPanelMouseClicked(java.awt.event.MouseEvent evt) {
            CategoryPanel categoryPanel = (CategoryPanel) evt.getComponent();
            categoryPanel.setSelected(true);
        }
    }

    /**
     * Creates new form EditLexiconAdjectivalCategoriesPanel
     */
    public EditLexiconAdjectivalCategoriesPanel() {
        initComponents();
        
        categories = new ArrayList<CategoryPanel>();
        selectedCategory = null;
        currentCategory = null;
        addButton.setEnabled(false);
        deleteButton.setEnabled(false);
        upButton.setEnabled(false);
        downButton.setEnabled(false);
            
        // Initialize categories
        
        Lexicon lexicon = WorkingDataManager.workingLexicon;
        AdjectivalCategoryManager adjCatManager = lexicon.getAdjCatManager();
        ArrayList<String> categoriesFromLexiconAsArrayList = adjCatManager.getCategoryNames();
        String[] categoriesFromLexicon = (categoriesFromLexiconAsArrayList != null)
                ? categoriesFromLexiconAsArrayList.toArray(new String[0])
                : new String[0];

        for (String categoryName : categoriesFromLexicon)
            this.addCategory(categoryName);
        
        selectedCategory.setSelected(false);
        
        // Initialize Dropdowns
        
        typeComboBox.setModel(new DefaultComboBoxModel<String>(AdjectivalCategory.TYPE.valuesAsString()));
        exclusivityComboBox.setModel(new DefaultComboBoxModel<String>(AdjectivalCategory.EXCLUSIVITY.valuesAsString()));
    }
    
    protected void selectionChanged() {
        if (currentCategory != null) {
            typeComboBox.setSelectedItem(currentCategory.getType().toString());
            exclusivityComboBox.setSelectedItem(currentCategory.getExclusivity().toString());            
        } else {
            typeComboBox.setSelectedItem(null);
            exclusivityComboBox.setSelectedItem(null);
        }
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
        
        if (userInteraction) {
            synchronizeAdjectivalCategories();
        }
        
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
        
        if (userInteraction) {
            synchronizeAdjectivalCategories();
        }
        
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
        
        if (userInteraction) {
            synchronizeAdjectivalCategories();
        }
        
        categoriesPanel.revalidate();
    }
    
    private void synchronizeAdjectivalCategories() {
        Lexicon lexicon = WorkingDataManager.workingLexicon;
        AdjectivalCategoryManager adjCatManager = lexicon.getAdjCatManager();
        ArrayList<String> categoryNames = new ArrayList<String>();
        
        for (CategoryPanel current : categories)
            categoryNames.add(current.getNameLabelText());
        
        adjCatManager.synchronizeAdjectivalCategories(categoryNames);
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
        categoryPropertiesPanel = new javax.swing.JPanel();
        categoryPropertiesCenterPanel = new javax.swing.JPanel();
        categoryPropertiesTabbedPane = new javax.swing.JTabbedPane();
        nonRelationalPropertiesPane = new javax.swing.JPanel();
        adjectiveTypePanel = new javax.swing.JPanel();
        typeLabel = new javax.swing.JLabel();
        typeComboBox = new javax.swing.JComboBox<>();
        exclusivityPanel = new javax.swing.JPanel();
        exclusivityLabel = new javax.swing.JLabel();
        exclusivityComboBox = new javax.swing.JComboBox<>();
        relationalPropertiesPane = new javax.swing.JPanel();

        setLayout(new java.awt.GridLayout(1, 2, 6, 6));

        mainPanel.setLayout(new java.awt.BorderLayout());

        categoriesPanel.setBackground(new java.awt.Color(204, 204, 255));
        categoriesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Adjectival Categories"));
        categoriesPanel.setLayout(new java.awt.GridLayout(20, 1, 2, 2));
        mainPanel.add(categoriesPanel, java.awt.BorderLayout.CENTER);

        topPanel.setBackground(new java.awt.Color(153, 153, 255));

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

        bottomPanel.setBackground(new java.awt.Color(153, 153, 255));

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

        add(mainPanel);

        categoryPropertiesPanel.setLayout(new java.awt.BorderLayout());

        categoryPropertiesCenterPanel.setBackground(new java.awt.Color(204, 204, 255));
        categoryPropertiesCenterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Category Properties"));
        categoryPropertiesCenterPanel.setLayout(new java.awt.BorderLayout());

        nonRelationalPropertiesPane.setBackground(new java.awt.Color(204, 204, 255));
        nonRelationalPropertiesPane.setLayout(new java.awt.GridLayout(16, 1, 0, 10));

        adjectiveTypePanel.setBackground(new java.awt.Color(204, 204, 255));

        typeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        typeLabel.setText("Type:");
        typeLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        typeLabel.setPreferredSize(new java.awt.Dimension(40, 20));
        adjectiveTypePanel.add(typeLabel);

        typeComboBox.setPreferredSize(new java.awt.Dimension(120, 20));
        typeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeComboBoxActionPerformed(evt);
            }
        });
        adjectiveTypePanel.add(typeComboBox);

        nonRelationalPropertiesPane.add(adjectiveTypePanel);

        exclusivityPanel.setBackground(new java.awt.Color(204, 204, 255));

        exclusivityLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        exclusivityLabel.setText("Exclusivity:");
        exclusivityLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        exclusivityLabel.setPreferredSize(new java.awt.Dimension(60, 20));
        exclusivityPanel.add(exclusivityLabel);

        exclusivityComboBox.setPreferredSize(new java.awt.Dimension(120, 20));
        exclusivityComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exclusivityComboBoxActionPerformed(evt);
            }
        });
        exclusivityPanel.add(exclusivityComboBox);

        nonRelationalPropertiesPane.add(exclusivityPanel);

        categoryPropertiesTabbedPane.addTab("Non Relational", nonRelationalPropertiesPane);
        categoryPropertiesTabbedPane.addTab("Relational", relationalPropertiesPane);

        categoryPropertiesCenterPanel.add(categoryPropertiesTabbedPane, java.awt.BorderLayout.CENTER);

        categoryPropertiesPanel.add(categoryPropertiesCenterPanel, java.awt.BorderLayout.CENTER);

        add(categoryPropertiesPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void newCategoryNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newCategoryNameTextFieldActionPerformed
        String newCategoryName = newCategoryNameTextField.getText().trim();

        if (newCategoryName.compareTo("") == 0) return;

        userInteraction = true;
        addCategory(newCategoryName);
        userInteraction = false;
        
        newCategoryNameTextField.setText("");
        addButton.setEnabled(false);
    }//GEN-LAST:event_newCategoryNameTextFieldActionPerformed

    private void newCategoryNameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newCategoryNameTextFieldKeyReleased
        String newCategoryName = newCategoryNameTextField.getText().trim();

        addButton.setEnabled(newCategoryName.compareTo("") != 0);
    }//GEN-LAST:event_newCategoryNameTextFieldKeyReleased

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        String newCategoryName = newCategoryNameTextField.getText().trim();

        if (newCategoryName.compareTo("") == 0) return;

        userInteraction = true;
        addCategory(newCategoryName);
        userInteraction = false;
        
        newCategoryNameTextField.setText("");
        addButton.setEnabled(false);
    }//GEN-LAST:event_addButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if (selectedCategory != null) {
            userInteraction = true;
            removeCategory();
            userInteraction = false;
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        if (selectedCategory != null) {
            userInteraction = true;
            moveCategory(MOVE_DIRECTION.UP);
            userInteraction = false;
        }
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        if (selectedCategory != null) {
            userInteraction = true;
            moveCategory(MOVE_DIRECTION.DOWN);
            userInteraction = false;
        }
    }//GEN-LAST:event_downButtonActionPerformed

    private void typeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeComboBoxActionPerformed
        if (currentCategory == null) return;
        
        AdjectivalCategory.TYPE selectedType = AdjectivalCategory.TYPE.fromString((String) typeComboBox.getSelectedItem());
        
        currentCategory.setType(selectedType);
    }//GEN-LAST:event_typeComboBoxActionPerformed

    private void exclusivityComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exclusivityComboBoxActionPerformed
        if (currentCategory == null) return;
        
        currentCategory.setExclusivity(AdjectivalCategory.EXCLUSIVITY.fromString((String) exclusivityComboBox.getSelectedItem()));
    }//GEN-LAST:event_exclusivityComboBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel adjectiveTypePanel;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JPanel categoriesPanel;
    private javax.swing.JPanel categoryPropertiesCenterPanel;
    private javax.swing.JPanel categoryPropertiesPanel;
    private javax.swing.JTabbedPane categoryPropertiesTabbedPane;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton downButton;
    private javax.swing.JComboBox<String> exclusivityComboBox;
    private javax.swing.JLabel exclusivityLabel;
    private javax.swing.JPanel exclusivityPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField newCategoryNameTextField;
    private javax.swing.JPanel nonRelationalPropertiesPane;
    private javax.swing.JPanel relationalPropertiesPane;
    private javax.swing.JPanel topPanel;
    private javax.swing.JComboBox<String> typeComboBox;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables
}
