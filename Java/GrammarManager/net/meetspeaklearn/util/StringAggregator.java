/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.util;

import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author steve
 */
public class StringAggregator {
    public static String aggregate(int[] values, String delimiter) {
        String result = "";
        int count = values.length;
        
        for (int i = 0; i < count; i++) {
            if (i > 0) result += delimiter;
            result += Integer.toString(values[i]);
        }
        
        return result;
    }
    
    public static String aggregate(int[] values) {
        return aggregate(values, ", ");
    }
    
    public static String aggregate(Integer[] values, String delimiter) {
        String result = "";
        int count = values.length;
        
        for (int i = 0; i < count; i++) {
            if (i > 0) result += delimiter;
            result += values[i].toString();
        }
        
        return result;
    }
    
    public static String aggregate(Integer[] values) {
        return aggregate(values, ", ");
    }
    
    private class StackFrame {
        private String open;
        private ArrayList<String> strings;
        private String close;
        private String delimiter;
        private String indentation;
        private String elementIndentation;
        
        public StackFrame (String open, String close) {
            this.open = open;
            this.strings = new ArrayList<String>();
            this.close = close;
            this.delimiter = "";
            this.indentation = "";
        }
        
        public StackFrame (String open, String delmiter, String close) {
            this.open = open;
            this.strings = new ArrayList<String>();
            this.close = close;
            this.delimiter = delmiter;
            this.indentation = "";
        }
    }
    
    private ArrayList<String> strings;
    private String indentation;
    private String delimiter;
    private Stack<StackFrame> stack;
    private int depth;
    private boolean ommitIndentation = false;
    
    public StringAggregator() {
        strings = new ArrayList<String>();
        stack = new Stack<StackFrame>();
        indentation = "";
        delimiter = "";
        depth = 0;
    }
    
    public StringAggregator(String indentation) {
        this();
        this.indentation = indentation;
    }
    
    public StringAggregator(String delimiter, String indentation) {
        this(indentation);
        this.delimiter = delimiter;
    }
    
    public StringAggregator(String delimiter, boolean ommitIndentation) {
        this(delimiter, "");
        this.ommitIndentation = ommitIndentation;
    }
    
    public void add(String string) {
        if (depth == 0)
            strings.add(string);
        else
            stack.peek().strings.add(string);
    }
    
    public void push(String open, String close) {
        String stackFrameIndentation = "";
        
        depth += 1;
        
        for (int i = 0; i < depth; i++)
            stackFrameIndentation += indentation;
        
        if (! ommitIndentation)
            strings.add("\n" + stackFrameIndentation);
        
        strings.add(open);
        
        StackFrame stackFrame = new StackFrame(open, close);
        
        stackFrame.indentation = stackFrameIndentation;
        stackFrame.elementIndentation = indentation + stackFrameIndentation;
        
        stack.push(stackFrame);
    }
    
    public void push(String open, String delmiter, String close) {
        String stackFrameIndentation = "";
        
        depth += 1;
        
        for (int i = 0; i < depth; i++)
            stackFrameIndentation += indentation;
        
        if (! ommitIndentation)
            strings.add("\n" + stackFrameIndentation);
        
        strings.add(open);
        
        StackFrame stackFrame = new StackFrame(open, delimiter, close);
        
        stackFrame.indentation = stackFrameIndentation;
        stackFrame.elementIndentation = indentation + stackFrameIndentation;
        
        stack.push(stackFrame);
    }
    
    public void pop() {
        if (stack.isEmpty()) return;
        
        depth -= 1;
        
        StackFrame stackFrame = stack.pop();
        String stackFrameIndentation = stackFrame.indentation;
        
        int count = stackFrame.strings.size();
        
        for (int i = 0; i < count; i++) {
            if (! ommitIndentation)
                strings.add("\n" + stackFrame.elementIndentation);
            strings.add(stackFrame.strings.get(i));
            if ((i > 0) && (i < count - 1)) strings.add(stackFrame.delimiter);
        }
        
        if (! ommitIndentation)
            strings.add("\n" + stackFrameIndentation);
        
        strings.add(stackFrame.close);
    }
    
    public String toString() {
        String result = null;
        
        for (String string : strings) {
            if (result == null) {
                result = "";
            } else {
                result += delimiter;
            }
            
            result += string;
        }
        
        return result;
    }
}
