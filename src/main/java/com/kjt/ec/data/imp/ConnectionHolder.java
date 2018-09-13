package com.kjt.ec.data.imp;

import com.kjt.ec.data.annontation.Transactional;

import java.sql.Connection;

public class ConnectionHolder {
    static final ThreadLocal<Transactional> transactional=new ThreadLocal<Transactional>();
    static final ThreadLocal<Connection> current=new ThreadLocal<Connection>();

    public static Transactional getTransactional() {
        return transactional.get();
    }

    public static void setTransactional(Transactional trans) {
       transactional.set(trans);
    }

    public static void setConnection(Connection connection){
        current.set(connection);
    }

    public static Connection getConnection(){
        return current.get();
    }
}
