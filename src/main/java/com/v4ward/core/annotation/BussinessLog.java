package com.v4ward.core.annotation;

import com.v4ward.core.dict.base.SystemDict;
import com.v4ward.core.dict.base.AbstractDictMap;

import java.lang.annotation.*;

/**
 * @ClassName: BussinessLog
 * @Package: com.whxx.core.annotation
 * @Description: TODO
 * @author Capricornus
 * @date 2017/12/25 14:56
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BussinessLog {


    /**
     * 业务名称
     */
    String value() default "";

    /**
     * 被修改的实体的唯一标识,例如:菜单实体的唯一标识为"id"
     */
    String key() default "id";


    /**
     * 字典（用于查找key的中文名称和字段的中文名称）
     */
    Class<? extends AbstractDictMap>  dict() default SystemDict.class;


}
