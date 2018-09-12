package com.kjt.ec.aop.selector.expression;

import java.lang.reflect.Method;

public class AnyExpression extends AbstractExpression {
    public AnyExpression(){
        this.setValue("*");
    }

    @Override
    protected boolean doMatch(Method method) {
        return true;
    }
}
