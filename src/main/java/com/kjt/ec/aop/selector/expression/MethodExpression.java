package com.kjt.ec.aop.selector.expression;

import com.kjt.ec.aop.selector.expression.AbstractExpression;

import java.lang.reflect.Method;

class MethodExpression extends AbstractExpression {
    public MethodExpression(String exp)
    {
        super(exp);
    }

    @Override
    protected boolean doMatch(Method method) {
        return super.expressionEquals(
                this.getValue(),
                method.getName());
    }
}
