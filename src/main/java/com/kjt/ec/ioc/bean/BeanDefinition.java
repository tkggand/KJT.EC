package com.kjt.ec.ioc.bean;

public class BeanDefinition {
    public Class getServiceType() {
        return serviceType;
    }

    public void setServiceType(Class serviceType) {
        this.serviceType = serviceType;
    }

    public Class getImpType() {
        return impType;
    }

    public void setImpType(Class impType) {
        this.impType = impType;
    }

    public BeanDefinition(Class serviceType, Class impType) {
        this.serviceType = serviceType;
        this.impType = impType;
    }

    private Class serviceType;
    private Class impType;
}
