package com.kjt.ec.data.evaluation;

import ognl.DefaultClassResolver;

public class OgnlClassResolver extends DefaultClassResolver {

    @Override
    protected Class toClassForName(String className) throws ClassNotFoundException {
        ClassLoader[] classLoader= new ClassLoader[]{
            Thread.currentThread().getContextClassLoader(),
            getClass().getClassLoader(),
            ClassLoader.getSystemClassLoader()
        };
        for (ClassLoader cl : classLoader) {
            if (null != cl) {
                try {
                    Class<?> c = Class.forName(className, true, cl);
                    if (null != c) {
                        return c;
                    }
                } catch (ClassNotFoundException e) {
                    // we'll ignore this until all classloaders fail to locate the class
                }
            }
        }
        throw new ClassNotFoundException("Cannot find class: " + className);
    }
}