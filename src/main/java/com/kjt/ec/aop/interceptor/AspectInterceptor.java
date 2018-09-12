package com.kjt.ec.aop.interceptor;

import com.kjt.ec.aop.JoinPoint;

public interface AspectInterceptor {
    void interceptor(JoinPoint joinPoint);
}
