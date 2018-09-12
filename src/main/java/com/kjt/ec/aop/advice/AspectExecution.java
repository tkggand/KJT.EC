package com.kjt.ec.aop.advice;

import com.kjt.ec.aop.JoinPoint;
import com.kjt.ec.aop.interceptor.AspectInterceptor;
import com.kjt.ec.ioc.bean.BeanFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AspectExecution implements AspectInterceptor {

    protected Class classType;
    protected Method invoker;
    private BeanFactory factory;
    static final Map<Class,Object> aspectMap=new ConcurrentHashMap<Class,Object>();

    public AspectExecution(BeanFactory factory,Class classType, Method invoker) {
        this.classType = classType;
        this.factory=factory;
        this.invoker = invoker;
        this.invoker.setAccessible(true);
    }

    @Override
    public final void interceptor(JoinPoint joinPoint) {
        Object instance=aspectMap.get(classType);
        if(instance==null){
            try{
                instance=factory.getBean(classType);
                aspectMap.put(classType,instance);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        doInterceptor(instance,joinPoint);
    }

    protected void doInterceptor(Object aspect, JoinPoint joinPoint){
        try{
            invoker.invoke(aspect,joinPoint);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
