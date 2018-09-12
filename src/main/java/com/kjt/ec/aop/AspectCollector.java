package com.kjt.ec.aop;

import com.kjt.ec.aop.annontation.After;
import com.kjt.ec.aop.annontation.Around;
import com.kjt.ec.aop.annontation.Before;
import com.kjt.ec.aop.interceptor.AspectInterceptor;
import com.kjt.ec.aop.selector.PointcutSelector;
import com.kjt.ec.ioc.bean.BeanFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AspectCollector {
    final static Map<Method,List<AspectInterceptor>> aspectInterceptors=new ConcurrentHashMap<Method,List<AspectInterceptor>>();

    public static List<AspectInterceptor> collector(BeanFactory factory, Method method, List<Class> aspectList){
        List<AspectInterceptor> result=aspectInterceptors.get(method);
        if(result==null){
            result=new ArrayList<AspectInterceptor>();
            for (Class aspect:aspectList){
                Method[] methods= aspect.getDeclaredMethods();
                for (Method mh:methods){
                    if(mh.isAnnotationPresent(Before.class)){
                        Before around=mh.getAnnotation(Before.class);
                        PointcutSelector selector=new PointcutSelector(around.value());
                        if(selector.isValidForAdvisor(method)){
                            result.add(AspectFactory.create(factory,aspect,mh,Before.class));
                        }
                    }else if(mh.isAnnotationPresent(After.class)){
                        After around=mh.getAnnotation(After.class);
                        PointcutSelector selector=new PointcutSelector(around.value());
                        if(selector.isValidForAdvisor(method)){
                            result.add(AspectFactory.create(factory,aspect,mh,After.class));
                        }
                    }else if(mh.isAnnotationPresent(Around.class)){
                        Around around=mh.getAnnotation(Around.class);
                        PointcutSelector selector=new PointcutSelector(around.value());
                        if(selector.isValidForAdvisor(method)){
                            result.add(AspectFactory.create(factory,aspect,mh,Around.class));
                        }
                    }
                }
            }
            aspectInterceptors.put(method,result);
        }
        return result;
    }
}
