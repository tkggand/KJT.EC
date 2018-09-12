package com.kjt.ec.ioc.bean;

import com.kjt.ec.aop.GenerateProxy;
import com.kjt.ec.aop.annontation.Aspect;
import com.kjt.ec.extension.BeanExtension;
import com.kjt.ec.ioc.annontation.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanScanner {

    public static Map<String,BeanDefinition> scanBean(List<Class> aspectList) {
        String basePackage = "com.kjt.ec";
        Map<String,BeanDefinition> result =new ConcurrentHashMap<String, BeanDefinition>();
        List<Class> allClass = PackageScanner.loadClassFromPackage(basePackage);
        aspectList.addAll(scanAspect(allClass));
        for (Class classType:allClass) {
            Class[] interfaces = classType.getInterfaces();
            if (classType.isInterface()) {
                String beanName = BeanExtension.getBeanName(classType);
                BeanDefinition definition = new BeanDefinition(classType,null);
                result.put(beanName, definition);
            } else if (classType.isAnnotationPresent(Service.class)) {
                if (interfaces == null || interfaces.length == 0) {
                    String beanName = BeanExtension.getBeanName(classType);
                    BeanDefinition definition = new BeanDefinition(null, classType);
                    result.put(beanName, definition);
                } else {
                    for (Class interfaceType : interfaces) {
                        String beanName = BeanExtension.getBeanName(interfaceType);
                        BeanDefinition definition = new BeanDefinition(interfaceType, classType);
                        result.put(beanName, definition);
                    }
                }
            } else if (interfaces != null && interfaces.length > 0) {
                for (Class interfaceType : interfaces) {
                    if (!interfaceType.isAnnotationPresent(Service.class)) {
                        continue;
                    }
                    String beanName = BeanExtension.getBeanName(interfaceType);
                    BeanDefinition definition = new BeanDefinition(interfaceType, classType);
                    result.put(beanName, definition);
                }
            }
        }
        return result;
    }

    private static List<Class> scanAspect(List<Class> allClass){
        List<Class> aspect=new ArrayList<Class>();
        for(Class classType:allClass){
            if(!classType.isInterface()&&classType.isAnnotationPresent(Aspect.class)){
                aspect.add(classType);
            }
        }
        return aspect;
    }
}
