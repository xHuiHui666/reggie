package com.wolf.mapper;

import com.wolf.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 007
* @description 针对表【user(用户信息)】的数据库操作Mapper
* @createDate 2022-09-15 19:27:22
* @Entity com.wolf.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




