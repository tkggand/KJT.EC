package com.kjt.ec.aop;

import com.kjt.ec.aop.advice.AfterAdvice;
import com.kjt.ec.aop.advice.AroundAdivice;
import com.kjt.ec.aop.advice.BeforeAdvice;
import com.kjt.ec.aop.advice.ThrowingAdvice;
import com.kjt.ec.aop.annontation.Around;
import com.kjt.ec.aop.annontation.Throwing;
import com.kjt.ec.aop.interceptor.AspectInterceptor;
import com.kjt.ec.aop.selector.PointcutSelector;
import com.kjt.ec.extension.BeanExtension;
import com.kjt.ec.ioc.bean.BeanFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class GenerateProxy implements InvocationHandler,MethodInterceptor {
    final Object target;
    final Class classType;
    final List<Class> aspectList;
    final BeanFactory factory;

    protected GenerateProxy(BeanFactory factory,Object instance,Class classType,List<Class> aspectList){
        this.factory=factory;
        this.target=instance;
        this.classType=classType;
        this.aspectList=aspectList;
    }

    public static Object newProxyInstance(BeanFactory factory,Object instance, Class classType,List<Class> aspectList){
        GenerateProxy proxy=new GenerateProxy(factory,instance,classType,aspectList);
        ClassLoader classloader=classType.getClassLoader();
        Class[] interfaceList= BeanExtension.getInterfaces(classType);
        if(classType.isInterface()||(interfaceList!=null&&interfaceList.length>0)) {
            if (interfaceList == null || interfaceList.length == 0) {
                return Proxy.newProxyInstance(classloader, new Class[]{classType}, proxy);
            }
            return Proxy.newProxyInstance(classloader, interfaceList, proxy);
        }
        else {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(classType);
            enhancer.setCallback(proxy);
            return enhancer.create();
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        List<AspectInterceptor> interceptors = AspectCollector.collector(factory,method, aspectList);
        JoinPoint point = new JoinPoint();
        point.setArgs(args);
        point.setMethod(method);
        point.setProxy(proxy);
        point.setTarget(target);
        if (interceptors == null || interceptors.size() == 0) {
            point.process();
        } else {
            for (AspectInterceptor interceptor : interceptors) {
                if (interceptor instanceof BeforeAdvice) {
                    interceptor.interceptor(point);
                }
            }
            for (AspectInterceptor interceptor : interceptors) {
                if (interceptor instanceof AroundAdivice) {
                    interceptor.interceptor(point);
                }
            }
            point.process();
            if(point.getException()!=null){
                for (AspectInterceptor interceptor : interceptors) {
                    if (interceptor instanceof ThrowingAdvice) {
                        interceptor.interceptor(point);
                    }
                }
            }else {
                for (AspectInterceptor interceptor : interceptors) {
                    if (interceptor instanceof AfterAdvice) {
                        interceptor.interceptor(point);
                    }
                }
            }
        }
        return point.getReturnValue();
    }

    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return this.invoke(o,method,objects);
    }
}
