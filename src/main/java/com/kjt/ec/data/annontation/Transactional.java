package com.kjt.ec.data.annontation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transactional {

    int timeout() default -1;

    int Isolation() default 2;

    Class<? extends Throwable>[] rollbackFor() default {};
}
