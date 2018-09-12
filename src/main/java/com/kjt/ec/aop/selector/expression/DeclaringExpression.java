package com.kjt.ec.aop.selector.expression;

import com.kjt.ec.aop.selector.expression.AbstractExpression;

import java.lang.reflect.Method;

class DeclaringExpression extends AbstractExpression {

    public DeclaringExpression(String exp)
    {
        super(exp);
    }

    @Override
    protected boolean doMatch(Method method) {
        return super.expressionEquals(
                this.getValue(),
                method.getDeclaringClass().getName());
    }
}
