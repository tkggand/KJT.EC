package com.kjt.ec.aop;

import java.lang.reflect.Method;

public class JoinPoint {

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object getProxy() {
        return proxy;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public void process(){
        if(!processed) {
            try {
                this.returnValue = method.invoke(target, args);
            } catch (Exception e) {
                this.exception = e;
            }
            this.processed=true;
        }
    }

    private volatile boolean processed;
    private Object target;
    private Object proxy;
    private Method method;
    private Object[] args;
    private Exception exception;
    private Object returnValue;
}
