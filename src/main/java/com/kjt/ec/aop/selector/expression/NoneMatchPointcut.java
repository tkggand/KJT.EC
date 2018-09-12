package com.kjt.ec.aop.selector.expression;

import java.lang.reflect.Method;

public class NoneMatchPointcut extends AbstractExpression {
    @Override
    protected boolean doMatch(Method method) {
        return false;
    }
}
