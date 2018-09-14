package com.kjt.ec.aop;

import com.kjt.ec.aop.annontation.*;
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
                Method pointCutMethod=getPointCutMethod(methods);
                PointcutSelector pointcut=null;
                if(pointCutMethod!=null){
                    PointCut annon=pointCutMethod.getAnnotation(PointCut.class);
                    pointcut=new PointcutSelector(annon.value());
                }
                for (Method mh:methods){
                    if(mh.isAnnotationPresent(PointCut.class)){
                        continue;
                    }
                    if(mh.isAnnotationPresent(Before.class)){
                        Before around=mh.getAnnotation(Before.class);
                        if(pointcut!=null&&
                            around.value().startsWith(pointCutMethod.getName())&&
                            pointcut.isValidForAdvisor(method)){
                            result.add(AspectFactory.create(factory,aspect,mh,Before.class));
                        }else {
                            PointcutSelector selector=new PointcutSelector(around.value());
                            if(selector.isValidForAdvisor(method)){
                                result.add(AspectFactory.create(factory,aspect,mh,Before.class));
                            }
                        }
                    }else if(mh.isAnnotationPresent(After.class)){
                        After around=mh.getAnnotation(After.class);
                        if(pointcut!=null&&
                                around.value().startsWith(pointCutMethod.getName())&&
                                pointcut.isValidForAdvisor(method)){
                            result.add(AspectFactory.create(factory,aspect,mh,After.class));
                        }else {
                            PointcutSelector selector=new PointcutSelector(around.value());
                            if(selector.isValidForAdvisor(method)){
                                result.add(AspectFactory.create(factory,aspect,mh,After.class));
                            }
                        }
                    }else if(mh.isAnnotationPresent(Around.class)){
                        Around around=mh.getAnnotation(Around.class);
                        if(pointcut!=null&&
                                around.value().startsWith(pointCutMethod.getName())&&
                                pointcut.isValidForAdvisor(method)){
                            result.add(AspectFactory.create(factory,aspect,mh,Around.class));
                        }else {
                            PointcutSelector selector=new PointcutSelector(around.value());
                            if(selector.isValidForAdvisor(method)){
                                result.add(AspectFactory.create(factory,aspect,mh,Around.class));
                            }
                        }
                    }else if(mh.isAnnotationPresent(Throwing.class)){
                        Throwing around=mh.getAnnotation(Throwing.class);
                        if(pointcut!=null&&
                                around.value().startsWith(pointCutMethod.getName())&&
                                pointcut.isValidForAdvisor(method)){
                            result.add(AspectFactory.create(factory,aspect,mh,Throwing.class));
                        }else {
                            PointcutSelector selector=new PointcutSelector(around.value());
                            if(selector.isValidForAdvisor(method)){
                                result.add(AspectFactory.create(factory,aspect,mh,Throwing.class));
                            }
                        }
                    }
                }
            }
            aspectInterceptors.put(method,result);
        }
        return result;
    }

    private static Method getPointCutMethod(Method[] methods){
        for (Method mh:methods){
            if(mh.isAnnotationPresent(PointCut.class)){
                return mh;
            }
        }
        return null;
    }
}
