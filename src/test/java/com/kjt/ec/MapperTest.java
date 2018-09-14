package com.kjt.ec;

import com.kjt.ec.demo.AccountController;
import com.kjt.ec.demo.model.UserInfo;
import com.kjt.ec.ioc.bean.BeanFactory;
import com.kjt.ec.ioc.context.AnnotationApplicationContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MapperTest {

    @Test
    public void test(){
        BeanFactory factory=new AnnotationApplicationContext();
        AccountController controller= factory.getBean(AccountController.class);
        List<UserInfo> result= controller.run();
        Assert.assertTrue(result!=null&&result.size()>0);
    }
}
