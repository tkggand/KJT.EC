package com.kjt.ec.data;

import java.util.List;

public interface DataCommand {

    void setParameter(String name,Object value);

    <T> void setParameter(T value);

    int executeNonQuery();

    <T> T executeScalar(Class<T> classType);

    <T> T executeEntity(Class<T> classType);

    <T> List<T> executeList(Class<T> classType);
}
