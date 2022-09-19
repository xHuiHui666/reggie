package com.wolf.mapper;

import com.wolf.domain.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 007
* @description 针对表【employee(员工信息)】的数据库操作Mapper
* @createDate 2022-09-11 10:36:05
* @Entity com.wolf.domain.Employee
*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}




