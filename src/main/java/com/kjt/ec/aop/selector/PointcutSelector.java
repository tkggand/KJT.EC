package com.kjt.ec.aop.selector;

import com.kjt.ec.aop.selector.expression.AbstractExpression;
import com.kjt.ec.aop.selector.expression.PointcutTokenlizer;

import java.lang.reflect.Method;

public class PointcutSelector {
    public static final String MatchAll="execution(* *.*(*))";

    final AbstractExpression _expression;

    public PointcutSelector(){
        this(PointcutSelector.MatchAll);
    }

    public PointcutSelector(String expression)
    {
        if (expression==null||expression.equals(""))
            _expression = null;
        else
            _expression = new PointcutTokenlizer(expression).parser();
    }

    public boolean isValidForAdvisor(Method method)
    {
        if (_expression == null || method == null)
            return false;
        return _expression.match(method);
    }
}
