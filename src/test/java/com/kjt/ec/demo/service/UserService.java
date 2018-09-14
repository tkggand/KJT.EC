package com.kjt.ec.demo.service;

import com.kjt.ec.data.annontation.Transactional;
import com.kjt.ec.demo.RequestMapping;
import com.kjt.ec.demo.mapper.UserMapper;
import com.kjt.ec.demo.model.UserInfo;
import com.kjt.ec.ioc.annontation.Autowired;
import com.kjt.ec.ioc.annontation.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper mapper;

    @Transactional
    public List<UserInfo> queryUserInfo(int userId){
        List<UserInfo> list= mapper.selectAll(userId);
        UserInfo userInfo=mapper.selectOne(userId);
        return list;
    }
}
