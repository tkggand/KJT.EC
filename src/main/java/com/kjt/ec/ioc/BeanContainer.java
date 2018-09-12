package com.kjt.ec.ioc;

import com.kjt.ec.ioc.bean.BeanFactory;
import com.kjt.ec.ioc.bean.DefaultBeanFactory;

public class BeanContainer {
    final static BeanFactory resolver=new DefaultBeanFactory();

    public static Object getBean(String beanName){
        return resolver.getBean(beanName);
    }

    public static <T> T getBean(Class<T> beanType){
        return resolver.getBean(beanType);
    }
}
