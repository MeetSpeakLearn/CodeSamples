/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.grammarmanager;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author steve
 */
public class AdjectivalCategoryTableModel extends AbstractTableModel {
    private ArrayList<String> columnNames;
    private ArrayList<ArrayList<Adjective>> data;
    private int rowCount = 0;
    private int colCount = 0;

    public AdjectivalCategoryTableModel(int columns, ArrayList<Adjective> adjectives) {
        super();
        int adjCount = (adjectives == null) ? 0 : adjectives.size();
        
        System.err.println("AdjectivalCategoryTableModel(): columns=" + columns);
        
        if (adjCount == 0) {
            colCount = 0;
            rowCount = 0;
            return;
        }

        colCount = columns;
        rowCount = adjCount / columns;

        if ((adjCount % columns) > 0)
            rowCount++;
        
        System.err.println("AdjectivalCategoryTableModel(): rowCount=" + rowCount + ", colCount=" + colCount);
        
        data = new ArrayList<ArrayList<Adjective>>();
        
        columnNames = new ArrayList<String>();
        
        for (int i = 0; i < columns; i++) {
            columnNames.add("");
        }
        
        for (int row = 0; row < rowCount; row++) {
            ArrayList<Adjective> rowData = new ArrayList<Adjective>();
            
            for (int i = 0; i < columns; i++) {
                rowData.add(new Adjective.DummyAdjective());
            }
            
            data.add(rowData);
        }

        int adjIndex = 0;
        int rowIndex = 0;
        int colIndex = 0;
        String firstNameInColumn = "";
        String lastNameInColumn = "";
        Adjective previousAdj = null;
        
        for (Adjective adj : adjectives) {
            if (rowIndex == 0) {
                firstNameInColumn = adj.getText();
            }
            
            if (rowIndex == rowCount) {
                lastNameInColumn = previousAdj.getText();
                columnNames.set(colIndex, formColumnName(firstNameInColumn, lastNameInColumn));
                firstNameInColumn = adj.getText();
                
                rowIndex = 0;
                colIndex += 1;
            }
            
            data.get(rowIndex).set(colIndex, adj);
            
            previousAdj = adj;
            rowIndex += 1;
            adjIndex += 1;
        }
        
        lastNameInColumn = previousAdj.getText();
        columnNames.set(colIndex, formColumnName(firstNameInColumn, lastNameInColumn));
    }
    
    private String formColumnName(String first, String last) {
        if (first.compareTo(last) == 0)
            return first;
        else
            return first + " - " + last;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return colCount;
    }
    
    public String getColumnName(int columnIndex) {
        return columnNames.get(columnIndex);
    }

    public Object getValueAt(int row, int column) {
        return data.get(row).get(column);
    }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data.get(rowIndex).set(columnIndex, (Adjective) aValue);
    }

    public Class getColumnClass(int columnIndex) {
        return Adjective.class;
    }
}