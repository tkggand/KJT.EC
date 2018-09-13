package com.kjt.ec.demo;

import com.kjt.ec.data.annontation.Transactional;
import com.kjt.ec.demo.mapper.UserMapper;
import com.kjt.ec.demo.model.UserInfo;
import com.kjt.ec.ioc.annontation.Autowired;
import com.kjt.ec.ioc.annontation.Service;

import java.util.List;

@Service
public class AccountController {

    public AccountController(){

    }

    @Autowired
    private UserMapper mapper;

    @Transactional
    @RequestMapping
    public List<UserInfo> run(){
        List<UserInfo> list= mapper.selectAll(40231);
        UserInfo userInfo=mapper.selectOne(41381);
        return list;
    }
}
