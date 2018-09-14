package com.kjt.ec.ioc.context;

import com.kjt.ec.aop.GenerateProxy;
import com.kjt.ec.aop.annontation.Aspect;
import com.kjt.ec.extension.BeanExtension;
import com.kjt.ec.ioc.annontation.Autowired;
import com.kjt.ec.ioc.bean.BeanFactoryAware;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractApplicationContext implements ApplicationContext {

    final List<Class> aspectList=new LinkedList<Class>();

    protected void postBeanFactory(Object instance, Class classType) {
        if(instance instanceof BeanFactoryAware){
            BeanFactoryAware aware=(BeanFactoryAware)instance;
            aware.setBeanFactory(this);
        }
    }

    protected Object createProxy(Object instance, Class impType) {
        if(impType!=null&&impType.isAnnotationPresent(Aspect.class)){
            return instance;
        }
        return GenerateProxy.newProxyInstance(this,instance,impType,aspectList);
    }

    protected void autowiredBean(Object instance,Class classType){
        try{
            Field[] fields=classType.getDeclaredFields();
            for (Field field:fields){
                if(!field.isAnnotationPresent(Autowired.class)){
                    continue;
                }
                String beanName= BeanExtension.getServiceName(field);
                Object value=this.getBean(beanName);
                field.setAccessible(true);
                field.set(instance,value);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    protected String buildBeanName(Class classType){
        return classType.getName();
    }
}
