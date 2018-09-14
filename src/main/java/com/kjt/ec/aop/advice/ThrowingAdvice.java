package com.kjt.ec.aop.advice;

import com.kjt.ec.ioc.bean.BeanFactory;

import java.lang.reflect.Method;

public class ThrowingAdvice extends AspectExecution {
    public ThrowingAdvice(BeanFactory factory, Class classType, Method invoker) {
        super(factory,classType, invoker);
    }
}
