package com.kjt.ec.aop.selector.expression;

import java.lang.reflect.Method;

public class WithinPointcut extends AnnotationPointcut {

    public WithinPointcut(boolean annontation, String value) {
        super(annontation, value);
    }

    @Override
    protected boolean doMatch(Method method) {
        Class declareType = method.getDeclaringClass();
        Class annontationType = this.annotationType();
        if (hasAnnotation())
        {
            if (annontationType != null &&annontationType.isAnnotation())
            {
                return declareType.isAnnotationPresent(annontationType);
            }
            return false;
        }
        else
        {
            return super.expressionEquals(this.getValue(), declareType.getName());
        }
    }
}
