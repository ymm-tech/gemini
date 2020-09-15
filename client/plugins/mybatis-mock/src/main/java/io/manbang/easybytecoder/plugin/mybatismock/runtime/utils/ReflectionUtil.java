package io.manbang.easybytecoder.plugin.mybatismock.runtime.utils;

import java.lang.reflect.Field;

/**
 * 反射相关的通用方法
 *
 * @Author: sundaoming
 * @CreateDate: 2019/3/18 12:26
 */
public class ReflectionUtil {

    /**
     * 设置对象的属性值
     * @param object
     * @param propertyName
     * @param value
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setProperty(Object object, String propertyName, Object value)
            throws NoSuchFieldException, IllegalAccessException {

        Class c = object.getClass();
        Field field = c.getDeclaredField(propertyName);
        // 取消类型检查
        field.setAccessible(true);
        field.set(object, value);
    }

}
