package com.v4ward.core.annotation;

import java.lang.annotation.*;

/**
 *
 *@Description: 权限注解 用于检查权限
 *
 *
 * @ClassName: Permission
 * @Package: com.v4ward.core.annotation
 *
 * @author huangxiang
 * @date 2017/12/25 15:59
 */

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Permission {
    String[] value() default{};
}
