/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.oopl;

/**
 *
 * @author steve
 */
public class Variable extends Identifier implements Expression, LValue {
    private ValueType type;
    private Value value;

    public Variable(String name, ValueType type) {
        super(name);
        this.type = type;
        this.value = null;
    }

    public Variable(String name, ValueType type, Value value) {
        super(name);
        this.type = type;
        this.value = value;
    }
    
    public Value eval(Scope scope) {
        return scope.getValue(this);
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public LValue resolveReference(Scope scope) {
        return this;
    }
}
