package com.kjt.ec.aop.advice;

import com.kjt.ec.aop.JoinPoint;
import com.kjt.ec.ioc.bean.BeanFactory;

import java.lang.reflect.Method;

public class BeforeAdvice extends AspectExecution {
    public BeforeAdvice(BeanFactory factory, Class classType, Method invoker) {
        super(factory,classType, invoker);
    }
}
