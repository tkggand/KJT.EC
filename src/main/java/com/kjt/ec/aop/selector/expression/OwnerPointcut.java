package com.kjt.ec.aop.selector.expression;

import java.lang.reflect.Method;

public class OwnerPointcut extends AbstractExpression {

    public OwnerPointcut(String value){
        super(value);
    }

    @Override
    protected boolean doMatch(Method method) {
        Class declareType=method.getDeclaringClass();
        return super.expressionEquals(this.getValue(),declareType.getName());
    }
}
