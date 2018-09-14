package com.kjt.ec.ioc.context;

import com.kjt.ec.data.annontation.Mapper;
import com.kjt.ec.data.imp.MapperProxy;
import com.kjt.ec.ioc.bean.*;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class AnnotationApplicationContext extends AbstractApplicationContext {
    final Map<String,BeanDefinition> definitions;
    final Map<String,Object> instanceMap=new ConcurrentHashMap<String, Object>();

    public AnnotationApplicationContext(){
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
                    this.postBeanFactory(instance,classType);
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
}
