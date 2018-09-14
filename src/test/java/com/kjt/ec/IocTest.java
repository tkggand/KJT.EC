package com.kjt.ec;

import com.kjt.ec.demo.AccountController;
import com.kjt.ec.ioc.bean.BeanFactory;
import com.kjt.ec.ioc.context.AnnotationApplicationContext;
import org.junit.Assert;
import org.junit.Test;

public class IocTest {
    @Test
    public void test(){
        BeanFactory factory=new AnnotationApplicationContext();
        AccountController controller= factory.getBean(AccountController.class);
        Assert.assertNotNull(controller);
    }
}
