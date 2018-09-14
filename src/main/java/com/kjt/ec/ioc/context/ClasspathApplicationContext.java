package com.kjt.ec.ioc.context;

@SuppressWarnings("unused")
public class ClasspathApplicationContext extends AbstractApplicationContext {

    @Override
    public Object getBean(String beanName) {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> classType) {
        return null;
    }

    @Override
    public void registry(String beanName, Object instance) {

    }
}
