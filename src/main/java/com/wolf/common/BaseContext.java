package com.wolf.common;


/**
 * 基于ThreadLocal封装的工具类, 用于保存和取出当前登录用户id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 保存id
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 取出id
     * @return
     */
    public static Long getCurrentId(){
        if (threadLocal.get() != null){
            return threadLocal.get();
        }
        return 0L;
    }

}
