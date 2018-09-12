package com.kjt.ec.ioc.bean;

import com.kjt.ec.aop.GenerateProxy;
import com.kjt.ec.aop.annontation.Aspect;
import com.kjt.ec.data.imp.MapperProxy;
import com.kjt.ec.data.annontation.Mapper;
import com.kjt.ec.extension.BeanExtension;
import com.kjt.ec.ioc.annontation.Autowired;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory implements BeanFactory {

    final List<Class> aspectList=new ArrayList<Class>();
    final Map<String,BeanDefinition> definitions;
    final Map<String,Object> instanceMap=new ConcurrentHashMap<String, Object>();

    public DefaultBeanFactory(){
        definitions= BeanScanner.scanBean(aspectList);
    }

    @Override
    public Object getBean(String beanName) {
        Object instance= instanceMap.get(beanName);
        if(instance==null){
            instance=doResolve(beanName);
            if(instance!=null){
                instanceMap.put(beanName,instance);
            }
        }
        return instance;
    }

    @Override
    public <T> T getBean(Class<T> classType) {
        return (T)this.getBean(buildBeanName(classType));
    }

    @Override
    public void registry(String beanName, Object instance) {
        instanceMap.put(beanName,instance);
    }

    protected Object doResolve(String beanName) {
        BeanDefinition definition=definitions.get(beanName);
        if(definition!=null){
            return newInstance(definition);
        }
        return null;
    }

    protected Object newInstance(BeanDefinition definition) {
        Object instance=null;
        try
        {
            Class classType=definition.getImpType();
            if(classType!=null){
                Constructor[] constructors= classType.getDeclaredConstructors();
                if(constructors==null||constructors.length==0){
                    instance=classType.newInstance();
                }else {
                    Constructor constructor=constructors[0];
                    Class[] paramTypeList= constructor.getParameterTypes();
                    Object[] paramList=new Object[paramTypeList.length];
                    for (int i=0;i<paramTypeList.length;i++){
                        paramList[i]=this.getBean(paramTypeList[i]);
                    }
                    instance=constructor.newInstance(paramList);
                }
                if(instance!=null){
                    this.autowiredBean(instance,classType);
                    return this.createProxy(instance,classType);
                }
            }else {
                Class serviceType=definition.getServiceType();
                if(serviceType.isAnnotationPresent(Mapper.class)){
                    return MapperProxy.proxyMapper(serviceType);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return instance;
    }

    private Object createProxy(Object instance, Class impType) {
        if(impType!=null&&impType.isAnnotationPresent(Aspect.class)){
            return instance;
        }
        return GenerateProxy.newProxyInstance(this,instance,impType,aspectList);
    }

    private void autowiredBean(Object instance,Class classType){
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
