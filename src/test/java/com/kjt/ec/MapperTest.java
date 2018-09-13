package com.kjt.ec;

import com.kjt.ec.demo.AccountController;
import com.kjt.ec.demo.model.UserInfo;
import com.kjt.ec.ioc.BeanContainer;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MapperTest {

    @Test
    public void test(){
        AccountController controller = BeanContainer.getBean(AccountController.class);
        List<UserInfo> result= controller.run();
        Assert.assertTrue(result.size()>0);
    }
}
