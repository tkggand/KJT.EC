package com.kjt.ec.data.expressions;

public abstract class Expression {

    protected String expression;
    protected String value;

    public String getValue() {
        return value;
    }

    public String getExpression() {
        return expression;
    }

    public abstract String evaluation(Object context);

    protected boolean match(Object context){
        return false;
    }
}
