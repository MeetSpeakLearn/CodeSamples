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
public class IndexedExpressionReference implements Expression, LValue {
    private Expression indexedExpression;
    private Expression indexExpression;

    public IndexedExpressionReference() {
        indexedExpression = null;
        indexExpression = null;
    }

    public IndexedExpressionReference(Expression indexedExpression, Expression indexExpression) {
        this.indexedExpression = indexedExpression;
        this.indexExpression = indexExpression;
    }

    public Expression getIndexedExpression() {
        return indexedExpression;
    }

    public void setIndexedExpression(Expression indexedExpression) {
        this.indexedExpression = indexedExpression;
    }

    public Expression getIndexExpression() {
        return indexExpression;
    }

    public void setIndexExpression(Expression indexExpression) {
        this.indexExpression = indexExpression;
    }

    @Override
    public Value eval(Scope scope) {
        Value indexedExpressionEvaluated = indexedExpression.eval(scope);
        Value indexExpressionEvaluated = indexExpression.eval(scope);
        
        try {
            if (indexedExpressionEvaluated instanceof Value.VectorValue) {
                Value.VectorValue vector = (Value.VectorValue) indexedExpressionEvaluated;
                return vector.get(indexExpressionEvaluated);
            }
        } catch (Value.ValueTypeException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
        }
        return null;
    }

    @Override
    public ValueType getType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LValue resolveReference(Scope scope) {
        
        Value indexedExpressionEvaluated = indexedExpression.eval(scope);
        Value indexExpressionEvaluated = indexExpression.eval(scope);
        
        try {
            if (indexedExpressionEvaluated instanceof Value.VectorValue) {
                Value.VectorValue vector = (Value.VectorValue) indexedExpressionEvaluated;
                
                if (indexExpressionEvaluated.getType() != ValueType.INTEGER) {
                    throw new Value.ValueTypeException("Index exception: expected an index of type "
                            + ValueType.INTEGER + " but parameter is of type "
                            + indexExpressionEvaluated.getType());
                }
                
                return vector.getLValue((Value.IntegerValue) indexExpressionEvaluated);
            }
        } catch (Value.ValueTypeException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
        }
        
        return null;
    }
}
