package com.kjt.ec.aop.selector.expression;

import java.lang.reflect.Method;

public class AnnotationPointcut extends AbstractExpression {

    final boolean hasAnnotation;

    public AnnotationPointcut(boolean annontation, String value)
    {
        super(value);
        this.hasAnnotation = annontation;
    }

    public boolean hasAnnotation(){
        return this.hasAnnotation;
    }

    public Class annotationType(){
        if(this.getValue()==null||this.getValue()==""){
            return null;
        }
        try{
            return getClass().getClassLoader().loadClass(this.getValue());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected boolean doMatch(Method method) {
        Class annontationType = this.annotationType();
        if(annontationType!=null&&annontationType.isAnnotation()){
            return method.isAnnotationPresent(annontationType);
        }
        return false;
    }
}
