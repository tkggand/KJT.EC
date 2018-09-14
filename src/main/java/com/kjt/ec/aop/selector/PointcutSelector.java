package com.kjt.ec.aop.selector;

import com.kjt.ec.aop.selector.expression.AbstractExpression;
import com.kjt.ec.aop.selector.expression.PointcutTokenlizer;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PointcutSelector {
    public static final String MatchAll="execution(* *.*(*))";
    static final Map<Integer,Boolean> matchCache=new ConcurrentHashMap<Integer,Boolean>();
    final String expression;

    @SuppressWarnings("unused")
    public PointcutSelector(){
        this(PointcutSelector.MatchAll);
    }

    public PointcutSelector(String expression)
    {
        this.expression=expression;
    }

    public boolean isValidForAdvisor(Method method)
    {
        if (method==null|| expression==null||expression.equals(""))
        {
            return false;
        }
        int hash=this.computeHash(method,expression);
        Boolean result=matchCache.get(hash);
        if(result==null){
            AbstractExpression exp = new PointcutTokenlizer(expression).parser();
            result=exp.match(method);
            matchCache.put(hash,result);
        }
        return result;
    }

    private int computeHash(Method method,String exp){
        return exp.hashCode()^method.hashCode();
    }
}
