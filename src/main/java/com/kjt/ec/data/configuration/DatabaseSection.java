package com.kjt.ec.data.configuration;

import java.util.HashMap;
import java.util.Map;

public class DatabaseSection {
    private String name;
    private Map<String,String> datasource;

    public DatabaseSection(){
        this.datasource=new HashMap<String, String>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getDatasource() {
        return datasource;
    }

    public void setDatasource(Map<String, String> datasource) {
        this.datasource = datasource;
    }
}
