/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.oopl;

import javafx.util.Pair;

/**
 *
 * @author steve
 */
public abstract class Value implements Expression {
    protected ValueType type;
    
    public ValueType getType() {
        return type;
    }
    
    public abstract Object valueOf();
    
    public Value eval(Scope scope) {
        return this;
    }
    
    public static ValueType staticGetType() {
        return ValueType.NOT_A_TYPE;
    }
    
    public static class ValueException extends Exception {
        public ValueException(String message) {
            super(message);
        }
    }
    
    public static class ValueTypeException extends ValueException {
        public ValueTypeException(String message) {
            super(message);
        }
    }
    
    public static class ValueVectorException extends ValueTypeException {
        public ValueVectorException(String message) {
            super(message);
        }
    }
    
    public static class BooleanValue extends Value {
        private boolean value;
        
        public BooleanValue(boolean value) {
            type = ValueType.BOOLEAN;
            this.value = value;
        }
        
        public BooleanValue(BooleanValue value) {
            this((boolean) value.valueOf());
        }
        
        public Object valueOf() {
            return value;
        }
    
        public static ValueType staticGetType() {
            return ValueType.BOOLEAN;
        }
    }
    
    public static class CharacterValue extends Value {
        private char value;
        
        public CharacterValue(char value) {
            type = ValueType.CHARACTER;
            this.value = value;
        }
        
        public CharacterValue(CharacterValue value) {
            this((char) value.valueOf());
        }
        
        public Object valueOf() {
            return value;
        }
    
        public static ValueType staticGetType() {
            return ValueType.CHARACTER;
        }
    }
    
    public static class IntegerValue extends Value {
        private long value;
        
        public IntegerValue(long value) {
            type = ValueType.INTEGER;
            this.value = value;
        }
        
        public IntegerValue(IntegerValue value) {
            this((long) value.valueOf());
        }
        
        public Object valueOf() {
            return value;
        }
    
        public static ValueType staticGetType() {
            return ValueType.INTEGER;
        }
    }
    
    public static class RealValue extends Value {
        private double value;
        
        public RealValue(double value) {
            type = ValueType.REAL;
            this.value = value;
        }
        
        public RealValue(RealValue value) {
            this((double) value.valueOf());
        }
        
        public Object valueOf() {
            return value;
        }
    
        public static ValueType staticGetType() {
            return ValueType.REAL;
        }
    }
    
    public static abstract class HomogenousIndexedValueStructure extends Value {
        protected ValueType elementType;        
        protected ValueType indexType;
        
        protected Value internalGet(Value index) throws ValueTypeException {
            if (index.getType() != indexType) {
                throw new ValueTypeException("Index exception: expected an index of type " + indexType + " but parameter is of type " + index.getType());
            }
            
            // Override me...
            return null;
        }
        
        protected void internalSet(Value index, Value value) throws ValueTypeException {
            if (index.getType() != indexType) {
                throw new ValueTypeException("Index exception: expected an index of type " + indexType + " but parameter is of type " + index.getType());
            }
            if (value.getType() != elementType) {
                throw new ValueTypeException("Value exception: expected an value of type " + elementType + " but parameter is of type " + value.getType());
            }
        }
        
        public abstract Value get(Value index) throws ValueTypeException;
        public abstract void set(Value index, Value value) throws ValueTypeException;
    }
    
    public static class VectorValue extends HomogenousIndexedValueStructure {
        private Value[] value;
        
        public VectorValue(Value[] value) {
            Class componentType = value.getClass().getComponentType();
            
            if (componentType == Boolean.class) {
                elementType = ValueType.BOOLEAN;
            } else if (componentType == Character.class) {
                elementType = ValueType.CHARACTER;
            } else if ((componentType == Integer.class) || (componentType == Long.class)) {
                elementType = ValueType.INTEGER;
            } else if ((componentType == Float.class) || (componentType == Double.class)) {
                elementType = ValueType.REAL;
            } else if (componentType == String.class) {
                elementType = ValueType.STRING;
            } else if (Object.class.isAssignableFrom(componentType)) {
                elementType = ValueType.OBJECT;
            }
            
            indexType = ValueType.INTEGER;
            
            type = ValueType.VECTOR;
            this.value = value;
        }
        
        public VectorValue(VectorValue value) {
            this((Value[]) value.valueOf());
        }
        
        public Object valueOf() {
            return value;
        }
    
        public static ValueType staticGetType() {
            return ValueType.VECTOR;
        }
        
        public Value get(Value index) throws ValueTypeException {
            internalGet(index);
            return ((Value[]) value)[(int) index.valueOf()];
        }
        
        public void set(Value index, Value v) throws ValueTypeException {
            internalSet(index, v);
            ((Value[]) value)[(int) index.valueOf()] = v;
        }
        
        public VectorLValueReference getLValue(IntegerValue index) {
            return new VectorLValueReference(this, (int) index.valueOf());
        }
    }
    
    public static class StringValue extends Value {
        private String value;
        
        public StringValue(String value) {
            type = ValueType.STRING;
            this.value = value;
        }
        
        public StringValue(StringValue value) {
            this((String) value.valueOf());
        }
        
        public Object valueOf() {
            return value;
        }
    
        public static ValueType staticGetType() {
            return ValueType.STRING;
        }
    }
    
    public static class ObjectValue extends Value {
        private Object value;
        private Scope scope;
        
        public ObjectValue(Object value) {
            type = ValueType.OBJECT;
            scope = new Scope();
            
            if (value instanceof ObjectValue) {
                this.value = ((ObjectValue) value).valueOf();
                this.scope = ((ObjectValue) value).getScope();
            } else {
                this.value = value;
            }
        }
        
        protected Scope getScope () {
            return scope;
        }
        
        public Object valueOf() {
            return value;
        }
        
        public Value get(Variable property) {
            return scope.getValue(property);
        }
        
        public Value set(Variable property, Value val) {
            scope.setValue(property, val);
            return val;
        }
    
        public static ValueType staticGetType() {
            return ValueType.OBJECT;
        }
    }
    
    public static class VectorLValueReference implements LValue {
        private VectorValue vector;
        private int vectorIndex;

        public VectorLValueReference(VectorValue vector, int vectorIndex) {
            this.vector = vector;
            this.vectorIndex = vectorIndex;
        }

        public VectorValue getVector() {
            return vector;
        }

        public void setVector(VectorValue vector) {
            this.vector = vector;
        }

        public int getVectorIndex() {
            return vectorIndex;
        }

        public void setVectorIndex(int vectorIndex) {
            this.vectorIndex = vectorIndex;
        }

        @Override
        public LValue resolveReference(Scope scope) {
            return this;
        }
    }
    
    public static class ObjectPropertyLValueReference implements LValue {
        private ObjectValue obj;
        private Variable property;

        public ObjectPropertyLValueReference(ObjectValue obj, Variable property) {
            this.obj = obj;
            this.property = property;
        }

        public ObjectValue getObj() {
            return obj;
        }

        public void setObj(ObjectValue obj) {
            this.obj = obj;
        }

        public Variable getProperty() {
            return property;
        }

        public void setProperty(Variable property) {
            this.property = property;
        }

        @Override
        public LValue resolveReference(Scope scope) {
            return this;
        }
    }
}

