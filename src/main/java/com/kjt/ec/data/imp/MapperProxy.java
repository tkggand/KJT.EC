package com.kjt.ec.data.imp;

import com.kjt.ec.data.DataAccess;
import com.kjt.ec.data.DataCommand;
import com.kjt.ec.data.annontation.Param;

import java.lang.reflect.*;
import java.util.List;

public class MapperProxy implements InvocationHandler{

    public static  <T> T proxyMapper(Class<T> classType) {
        MapperProxy proxy=new MapperProxy();
        return (T) Proxy.newProxyInstance(proxy.getClass().getClassLoader(), new Class[]{classType}, proxy);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String commandName=method.getName();
        if(commandName=="toString"){
            return "mapperProxy";
        }
        Parameter []parameters= method.getParameters();
        DataAccess dataAccess=new DataAccessImp();
        DataCommand command= dataAccess.createCommand(commandName);
        if(command==null){
            throw new Exception(String.format("cant not find %s command",commandName));
        }
        for(int i=0;i<parameters.length;i++){
            Object value=args[i];
            Parameter parm=parameters[i];
            String paramName=null;
            if(parm.isAnnotationPresent(Param.class)){
                Param p=(Param)parm.getAnnotation(Param.class);
                paramName=p.value();
            }
            command.setParameter(paramName,value);
        }
        Class returnType=method.getReturnType();
        Type genericReturnType=method.getGenericReturnType();
        if (genericReturnType!=null&& ParameterizedType.class.isAssignableFrom(genericReturnType.getClass())) {
            ParameterizedType pt = (ParameterizedType) genericReturnType;
            returnType =(Class) pt.getActualTypeArguments()[0];
            return command.executeList(returnType);
        }
        return command.executeEntity(returnType);
    }
}
