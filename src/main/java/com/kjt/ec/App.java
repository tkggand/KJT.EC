package com.kjt.ec;

import com.kjt.ec.aop.selector.PointcutSelector;
import com.kjt.ec.demo.AccountController;
import com.kjt.ec.demo.model.UserInfo;
import com.kjt.ec.ioc.BeanContainer;
import com.sun.corba.se.impl.orbutil.concurrent.Mutex;

import javax.management.monitor.Monitor;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class App {

    public static void main(String[]args) {
         
        AccountController controller = BeanContainer.getBean(AccountController.class);
        List<UserInfo> result= controller.run();
        System.out.println(result.size());
    }
}
