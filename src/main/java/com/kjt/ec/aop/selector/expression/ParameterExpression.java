package com.kjt.ec.aop.selector.expression;

import com.kjt.ec.aop.selector.expression.AbstractExpression;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

class ParameterExpression extends AbstractExpression{

    public ParameterExpression(String exp)
    {
        super(exp);
    }

    @Override
    protected boolean doMatch(Method method) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length == 0 &&(getValue()==null||getValue().equals("")))
            return true;
        String[] array = getValue().split(",");
        if (array.length != parameters.length)
            return false;
        boolean result = true;
        for(int i = 0; i < array.length; i++)
        {
            result &= super.expressionEquals(array[i], parameters[i].getParameterizedType().getTypeName());
        }
        return result;
    }
}
