package com.wolf.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 元数据处理器
 * 作用: 统一更新冗余操作的字段, 并存入相应对象中
 */

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入操作, 自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段方法(insert): {}",metaObject.toString());
        metaObject.setValue("createTime",new Date());
        metaObject.setValue("updateTime",new Date());
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    /**
     * 更新操作, 自动填充
     * @param metaObject
     */

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段方法(update): {}",metaObject.toString());
        metaObject.setValue("updateTime",new Date());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());

    }
}
