package com.kjt.ec.aop.annontation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Throwing {
    String value() default "";
}
