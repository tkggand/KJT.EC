package com.kjt.ec.aop.selector.expression;

import java.lang.reflect.Method;

public abstract class PointCutExpression {
    private String expression;

    public PointCutExpression(String expression){
        this.expression=expression;
    }

    public final boolean match(Method method){
        if(expression==null||expression.equals("*")){
            return true;
        }
       return doMatch(method);
    }

    protected abstract boolean doMatch(Method method);
}
