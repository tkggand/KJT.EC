package com.kjt.ec.data.expressions;

import com.kjt.ec.data.evaluation.OgnlCache;

public class IfExpression extends Expression {

    public IfExpression(String exp,String value){
        super.expression=exp;
        super.value=value;
    }

    @Override
    public String evaluation(Object context) {
        return this.value;
    }

    @Override
    protected boolean match(Object context) {
         Object value=OgnlCache.getValue(this.expression,context);
         if(value instanceof Boolean){
             return (boolean)value;
         }
         return false;
    }
}
