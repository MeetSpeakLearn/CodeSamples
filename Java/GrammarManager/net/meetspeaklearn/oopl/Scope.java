/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.oopl;

import java.util.*;
import javafx.util.Pair;

/**
 *
 * @author steve
 */
public class Scope {
    private Scope outerScope;
    private HashMap<String, Pair<Identifier, Expression>> bindings;

    public Scope() {
        outerScope = null;
        bindings = new HashMap<String, Pair<Identifier, Expression>>();
    }

    public Scope(Scope outerScope) {
        this.outerScope = outerScope;
        bindings = new HashMap<String, Pair<Identifier, Expression>>();
    }

    public Scope getOuterScope() {
        return outerScope;
    }

    public void setOuterScope(Scope outerScope) {
        this.outerScope = outerScope;
    }

    public HashMap<String, Pair<Identifier, Expression>> getLexemes() {
        return bindings;
    }

    public void setLexemes(HashMap<String, Pair<Identifier, Expression>> bindings) {
        this.bindings = bindings;
    }
    
    public void bind(String id, Value v) {
        bindings.put(id, new Pair<Identifier, Expression>(new Variable(id, v.getType()), v));
    }
    
    public void bind(String id, Expression e) {
        bindings.put(id, new Pair<Identifier, Expression>(new Variable(id, e.getType()), e));
    }
    
    public void add(String id) {
        bindings.put(id, new Pair<Identifier, Expression>(new Variable(id, ValueType.UNKNOWN), null));
    }
    
    public Identifier getIdentifier(String lexeme) {
        Pair<Identifier, Expression> entry = bindings.get(lexeme);
        
        if (entry == null) {
            if (outerScope != null) 
                return outerScope.getIdentifier(lexeme);
        }
        
        return entry.getKey();
    }
    
    public Pair<Identifier, Expression> getBinding(String lexeme) {
        Pair<Identifier, Expression> entry = bindings.get(lexeme);
        
        if (entry == null) {
            if (outerScope != null) 
                return outerScope.getBinding(lexeme);
        }
        
        return entry;
    }
    
    public Value setBinding(String lexeme, Value val) {
        Pair<Identifier, Expression> entry = bindings.get(lexeme);
        Value returnValue = null;
        
        if (entry == null) {
            if (outerScope != null) {
                returnValue = outerScope.setBinding(lexeme, val);
            }
        } else {
            bindings.put(lexeme, new Pair<Identifier, Expression>(entry.getKey(), val));
            returnValue = val;
        }
        
        if (returnValue != null)
            return val;
        else
            return null;
    }
    
    public Value getValue(Variable var) {
        Pair<Identifier, Expression> binding = getBinding(var.getName());
        
        if (binding != null)
            return binding.getValue().eval(this);
        else
            return var.getValue();
    }
    
    public Value setValue(Variable var, Value val) {
        Value result = setBinding(var.getName(), val);
        
        if (result != null)
            return val;
        
        var.setValue(val);
        
        return val;
    }
}
