package com.kjt.ec.demo;

import com.kjt.ec.aop.JoinPoint;
import com.kjt.ec.aop.annontation.*;
import com.kjt.ec.ioc.annontation.Service;

@Aspect
@Service
@SuppressWarnings("unused")
public class RequestInterceptor {

    //@PointCut("@target(com.kjt.ec.demo.RequestMapping)")
    @PointCut("@annotation(com.kjt.ec.demo.RequestMapping)")
    public void pointcut(){

    }

    @Before("pointcut()")
    public void before(JoinPoint point){
        System.out.println("before aspect");
    }

    @Around("pointcut()")
    public void around(JoinPoint point){
        System.out.println("around aspect");
    }

    @After("pointcut()")
    public void after(JoinPoint point){
        System.out.println("after aspect");
    }

    @Throwing("pointcut()")
    public void throwing(JoinPoint point){
        System.out.println("throwing");
    }
}
