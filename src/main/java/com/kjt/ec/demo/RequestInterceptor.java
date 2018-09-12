package com.kjt.ec.demo;

import com.kjt.ec.aop.JoinPoint;
import com.kjt.ec.aop.annontation.*;
import com.kjt.ec.ioc.annontation.Service;

@Aspect
@Service
public class RequestInterceptor {

//    @PointCut("@annotation(com.kjt.ec.demo.RequestMapping)")
//    public void pointcut(){
//
//    }

    @Before("@annotation(com.kjt.ec.demo.RequestMapping)")
    public void before(JoinPoint point){
        System.out.println("before aspect");
    }

    @Around("@annotation(com.kjt.ec.demo.RequestMapping)")
    public void around(JoinPoint point){
        System.out.println("around aspect");
    }

    @After("@annotation(com.kjt.ec.demo.RequestMapping)")
    public void after(JoinPoint point){
        System.out.println("after aspect");
    }
}
