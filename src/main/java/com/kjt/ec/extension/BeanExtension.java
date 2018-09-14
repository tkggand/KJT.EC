package com.kjt.ec.extension;

import com.kjt.ec.ioc.annontation.Autowired;
import com.kjt.ec.ioc.annontation.Service;
import com.kjt.ec.ioc.bean.BeanFactoryAware;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

    public static Class [] getInterfaces(Class targetType){
        List<Class> list=new ArrayList<Class>();
        Class[] interfaces = targetType.getInterfaces();
        for (Class interfaceType:interfaces){
            if(interfaceType==BeanFactoryAware.class){
                continue;
            }
            list.add(interfaceType);
        }
        return list.toArray(new Class[list.size()]);
    }
}
