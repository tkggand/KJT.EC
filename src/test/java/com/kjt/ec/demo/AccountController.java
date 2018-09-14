package com.kjt.ec.demo;

import com.kjt.ec.data.annontation.Transactional;
import com.kjt.ec.demo.mapper.UserMapper;
import com.kjt.ec.demo.model.UserInfo;
import com.kjt.ec.demo.service.UserService;
import com.kjt.ec.ioc.annontation.Autowired;
import com.kjt.ec.ioc.annontation.Service;
import com.kjt.ec.ioc.bean.BeanFactory;
import com.kjt.ec.ioc.bean.BeanFactoryAware;

import java.util.List;

@Service
@RequestMapping
public class AccountController implements BeanFactoryAware{

    public AccountController(){

    }

    @Autowired
    private UserService userService;

    @RequestMapping
    public List<UserInfo> run(){
        return userService.queryUserInfo(40231);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {

    }
}
