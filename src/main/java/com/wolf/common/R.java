package com.wolf.common;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果,服务端响应的数据最终会封装成此对象
 */

@Data
public class R<T> {

    private Integer code; // 编码 200成功,0和其他数字失败

    private String msg; // 错误信息

    private T data; // 数据

    private Map map = new HashMap(); // 动态数据

    public static<T> R<T>  success(T object){ // 结果成功
        R<T> r = new R<>();
        r.data = object;
        r.code = 200;
        return r;
    }

    public static <T> R<T> error(String msg){ // 结果失败
        R<T> r = new R<>();
        r.code = 0;
        r.msg = msg;
        return r;
    }

    public R<T> add(String key,Object value){// 操作动态数据
         this.map.put(key,value);
         return this;
    }





}
