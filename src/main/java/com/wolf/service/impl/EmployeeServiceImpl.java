package com.wolf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolf.domain.Employee;
import com.wolf.service.EmployeeService;
import com.wolf.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author 007
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2022-09-11 10:36:05
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




