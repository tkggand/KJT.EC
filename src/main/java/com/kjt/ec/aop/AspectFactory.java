package com.kjt.ec.aop;

import com.kjt.ec.aop.advice.AfterAdvice;
import com.kjt.ec.aop.advice.AroundAdivice;
import com.kjt.ec.aop.advice.BeforeAdvice;
import com.kjt.ec.aop.annontation.After;
import com.kjt.ec.aop.annontation.Around;
import com.kjt.ec.aop.annontation.Before;
import com.kjt.ec.aop.interceptor.AspectInterceptor;
import com.kjt.ec.ioc.bean.BeanFactory;

import java.lang.reflect.Method;

public class AspectFactory {

    public static AspectInterceptor create(BeanFactory factory, Class classType, Method method, Class annontationType) {
        if (annontationType == Around.class) {
            return new AroundAdivice(factory,classType, method);
        } else if (annontationType == Before.class) {
            return new BeforeAdvice(factory,classType, method);
        } else if (annontationType == After.class) {
            return new AfterAdvice(factory,classType, method);
        }
        return null;
    }
}
