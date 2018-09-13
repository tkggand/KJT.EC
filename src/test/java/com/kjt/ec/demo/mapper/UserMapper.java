package com.kjt.ec.demo.mapper;

import com.kjt.ec.data.annontation.Mapper;
import com.kjt.ec.data.annontation.Param;
import com.kjt.ec.demo.model.UserInfo;
import java.util.List;

@Mapper
public interface UserMapper {
    List<UserInfo> selectAll(@Param("userId") int userId);

    UserInfo selectOne(@Param("userId") int userId);
}
