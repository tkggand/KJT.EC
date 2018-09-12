package com.kjt.ec.aop.selector.expression;

import java.lang.reflect.Method;

public abstract class AbstractExpression {
    private String value;

    public AbstractExpression(){

    }

    protected AbstractExpression(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public final boolean match(Method method){
        if(value!=null&&value.equals("*")){
            return true;
        }
        return doMatch(method);
    }

    protected boolean expressionEquals(String source, String dest)
    {
        if (source == null || source.equals("*"))
            return true;
        String matchName ="";
        if (source.startsWith("*"))
        {
            matchName = source.substring(1, source.length() - 1);
            return dest.endsWith(matchName);
        }
        else if (source.endsWith("*"))
        {
            matchName = source.substring(0, source.length() - 1);
            return dest.startsWith(matchName);
        }
        return source.equalsIgnoreCase(dest);
    }

    protected abstract boolean doMatch(Method method);
}
