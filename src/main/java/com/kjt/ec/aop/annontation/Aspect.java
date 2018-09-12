package com.kjt.ec.aop.annontation;

import com.kjt.ec.ioc.annontation.Service;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Aspect {

}
