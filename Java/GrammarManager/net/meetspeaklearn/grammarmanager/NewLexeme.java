package net.meetspeaklearn.grammarmanager;

import java.awt.Color;
import static javax.swing.JOptionPane.showMessageDialog;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author steve
 */
public class NewLexeme extends javax.swing.JFrame {
    private GrammarManangerFrame gm;
    private Lexicon lexicon;
    private Lex lexeme;
    private boolean canceled;
    private String text;
    private boolean abstraction;
    private boolean dynamic;

    /**
     * Creates new form NewLexeme
     */
    public NewLexeme(GrammarManangerFrame gm, Lexicon lexicon) {
        initComponents();
        
        this.gm = gm;
        this.lexicon = lexicon;
        this.lexeme = null;
        
        lexemeTextField.selectAll();
        createButton.setEnabled(false);
    }
    
    public Lex getLexeme() {
        return lexeme;
    }
    
    public boolean isOK() {
        return ! canceled;
    }
    
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        centerPanel = new javax.swing.JPanel();
        lexemeLabel = new javax.swing.JLabel();
        lexemeTextField = new javax.swing.JTextField();
        isDynamicCheckBox = new javax.swing.JCheckBox();
        isAbstractCheckBox = new javax.swing.JCheckBox();
        bottomPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        createButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Lexeme");
        setMinimumSize(new java.awt.Dimension(400, 160));

        centerPanel.setLayout(new java.awt.GridLayout(2, 2, 6, 6));

        lexemeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lexemeLabel.setText("Lexeme:");
        centerPanel.add(lexemeLabel);

        lexemeTextField.setText("Enter lexeme here");
        lexemeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lexemeTextFieldActionPerformed(evt);
            }
        });
        lexemeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lexemeTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lexemeTextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                lexemeTextFieldKeyTyped(evt);
            }
        });
        centerPanel.add(lexemeTextField);

        isDynamicCheckBox.setSelected(true);
        isDynamicCheckBox.setText("Is dynamic?");
        isDynamicCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        isDynamicCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isDynamicCheckBoxActionPerformed(evt);
            }
        });
        centerPanel.add(isDynamicCheckBox);

        isAbstractCheckBox.setText("Is abstract?");
        isAbstractCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isAbstractCheckBoxActionPerformed(evt);
            }
        });
        centerPanel.add(isAbstractCheckBox);

        getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        bottomPanel.add(cancelButton);

        createButton.setText("Add");
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });
        bottomPanel.add(createButton);

        getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lexemeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lexemeTextFieldActionPerformed
        
    }//GEN-LAST:event_lexemeTextFieldActionPerformed

    private void isDynamicCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isDynamicCheckBoxActionPerformed
        dynamic = isDynamicCheckBox.isSelected();
    }//GEN-LAST:event_isDynamicCheckBoxActionPerformed

    private void isAbstractCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isAbstractCheckBoxActionPerformed
        abstraction = isAbstractCheckBox.isSelected();
    }//GEN-LAST:event_isAbstractCheckBoxActionPerformed

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        text = lexemeTextField.getText();
        
        if (lexicon.isInterned(text)) {
            showMessageDialog(this, "Lexicon " + lexicon.getName() + " already contains a lexeme with text \"" + text + "\"!");
            lexemeTextField.selectAll();
            createButton.setEnabled(false);
            return;
        } else {
            createButton.setEnabled(true);
        }
        
        lexeme = lexicon.instantiateLex(text);
        lexeme.setDynamic(dynamic);
        lexeme.setAbstraction(abstraction);
        lexicon.intern(text, lexeme);
        canceled = false;
        
        if (gm != null) {
            gm.updateView();
            gm.selectLexeme(lexeme);
        }
        
        setVisible(false);
        dispose();
    }//GEN-LAST:event_createButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void lexemeTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lexemeTextFieldKeyTyped
        
    }//GEN-LAST:event_lexemeTextFieldKeyTyped

    private void lexemeTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lexemeTextFieldKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lexemeTextFieldKeyPressed

    private void lexemeTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lexemeTextFieldKeyReleased
        text = lexemeTextField.getText();
        
        if (lexicon.isInterned(text)) {
            lexemeTextField.setForeground(Color.red);
            createButton.setEnabled(false);
        } else {
            lexemeTextField.setForeground(Color.black);
            createButton.setEnabled(true);
        }
    }//GEN-LAST:event_lexemeTextFieldKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewLexeme.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewLexeme.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewLexeme.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewLexeme.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewLexeme(null, WorkingDataManager.workingLexicon).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JButton createButton;
    private javax.swing.JCheckBox isAbstractCheckBox;
    private javax.swing.JCheckBox isDynamicCheckBox;
    private javax.swing.JLabel lexemeLabel;
    private javax.swing.JTextField lexemeTextField;
    // End of variables declaration//GEN-END:variables
}
