package com.kjt.ec.extension;

import com.kjt.ec.ioc.annontation.Autowired;
import com.kjt.ec.ioc.annontation.Service;

import java.lang.reflect.Field;

public class BeanExtension {

    public static String getBeanName(Class classType){
        if(classType.isAnnotationPresent(Service.class)){
            Service service=(Service)classType.getAnnotation(Service.class);
            if(StringExtension.isEmptyOrNull(service.name())){
                return classType.getName();
            }
            return service.name();
        }
        return classType.getName();
    }

    public static String getServiceName(Field classType){
        String beanName=null;
        if(classType.isAnnotationPresent(Autowired.class)){
            Autowired service=(Autowired)classType.getAnnotation(Autowired.class);
            beanName=service.name();
            if(StringExtension.isEmptyOrNull(beanName)){
                beanName=classType.getType().getName();
            }
        }else {
            beanName=classType.getType().getName();
        }
        return beanName;
    }
}
