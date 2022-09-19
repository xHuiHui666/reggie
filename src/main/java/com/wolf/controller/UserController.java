package com.wolf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wolf.common.R;
import com.wolf.domain.User;
import com.wolf.service.UserService;
import com.wolf.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    /**
     * 发送验证码
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMes(@RequestBody User user, HttpSession session) {
        // 获取手机号
        String phone = user.getPhone();

        if (Strings.isNotBlank(phone)) {
            // 生成随机的验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            // 在控制台打印验证码
            log.info("验证码: {}", code);
            // 将验证码保存到Session中
            //session.setAttribute(phone,code);

            // 将生成的验证码缓存到redis中, 设置有效期为5分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            System.out.println("验证码:" + session.getAttribute(phone));
            System.out.println("session的输出:" + session.toString());
            return R.success("手机验证码发送成功!");

        }

        return R.error("短信发送失败!");
    }


    /**
     * 移动端登录
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        // 获取手机号
        String phone = map.get("phone").toString();

        // 获取验证码
        String code = map.get("code").toString();

        // 从session中得到验证码code
        //Object codeInSession = session.getAttribute(phone);

        //从redis缓存中取出验证码
        Object codeInRedis = redisTemplate.opsForValue().get(phone);

        // 进行验证码比对
        if (codeInRedis != null && code.equals(codeInRedis)){
            // 验证码相同, 登录成功

            LambdaQueryWrapper<User> queryWrapper
                                      = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            // 查询该用户是否为新用户
            User user = userService.getOne(queryWrapper);
            if (user == null){
                user = new User();
                // 证明为新用户, 此时完成注册
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            // 设置session
            session.setAttribute("user",user.getId());
            // 登录成功, 返回该用户信息
            // 删除redis中缓存的验证码
            redisTemplate.delete(phone);
        return R.success(user);

        }
        return R.error("登陆失败!");

    }

    }
