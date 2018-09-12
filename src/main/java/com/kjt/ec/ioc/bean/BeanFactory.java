package com.kjt.ec.ioc.bean;

public interface BeanFactory {

    Object getBean(String beanName);

    <T> T getBean(Class<T> classType);

    void registry(String beanName,Object instance);
}
