package com.kjt.ec.aop.selector.expression;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ArgumentPointcut extends AnnotationPointcut {
    public ArgumentPointcut(boolean annontation, String value) {
        super(annontation, value);
    }

    @Override
    protected boolean doMatch(Method method) {
        Parameter[] parameters = method.getParameters();
        Class annontationType = this.annotationType();
        if (hasAnnotation())
        {
            if (annontationType != null &&annontationType.isAnnotation())
            {
                for (Parameter param:parameters){
                    if(param.isAnnotationPresent(annontationType)){
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        for (Parameter param:parameters){
            if(super.expressionEquals(this.getValue(),param.getType().getName()));
        }
        return false;
    }
}
