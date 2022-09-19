package com.wolf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wolf.common.R;
import com.wolf.domain.Employee;
import com.wolf.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param employee 员工信息
     * @param request  设置session对象
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request) {

        // 1. 对用户密码进行加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2. 根据用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee employeeFromMysql = employeeService.getOne(queryWrapper);
        if (employeeFromMysql == null) {
            // 3. 用户不存在
            return R.error("该用户不存在!");
        } else if (!employeeFromMysql.getPassword().equals(password)) {
            // 4. 密码比对
            return R.error("该用户不存在!");
        }

        if (employeeFromMysql.getStatus() == 0) {
            // 5. 账号禁用
            return R.error("账号已禁用!");
        }

        // 6. 登陆成功
        request.getSession().setAttribute("employee", employeeFromMysql.getId());
        return R.success(employeeFromMysql);

    }


    /**
     * 用户退出
     *
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // 1.清理session
        request.getSession().removeAttribute("employee");

        // 2.返回成功信息
        return R.error("NOTLOGIN");
    }


    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Employee employee, HttpServletRequest request) {
        log.info("新增员工,员工信息: {}", employee.toString());

        // 设置初始密码为123456 , 并进行MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employeeService.save(employee);


        return R.success("新增员工成功^_^!");

    }


    /**
     * 员工信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<Employee>> page(int page, int pageSize, String name) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);

        // 创建分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);

        // 添加查询条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Strings.isNotBlank(name), Employee::getName, name);
        // 添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        // 执行查询
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(@RequestBody Employee employee, HttpServletRequest request) {
        log.info("employee: {}", employee);

        boolean b = employeeService.updateById(employee);

        return b == true ? R.success("修改成功^_^!") : R.error("修改失败-_-!");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    public R<Employee> queryById(@PathVariable Long id){
       log.info("根据id查询员工信息");
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }



}
