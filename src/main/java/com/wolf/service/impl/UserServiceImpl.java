package com.wolf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolf.domain.User;
import com.wolf.service.UserService;
import com.wolf.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 007
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2022-09-15 19:27:22
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




