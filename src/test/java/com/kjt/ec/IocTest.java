package com.kjt.ec;

import com.kjt.ec.demo.AccountController;
import com.kjt.ec.ioc.BeanContainer;
import org.junit.Assert;
import org.junit.Test;

public class IocTest {
    @Test
    public void test(){
        AccountController controller= BeanContainer.getBean(AccountController.class);
        Assert.assertNotNull(controller);
    }
}
