package com.v4ward.core.dict;


import com.v4ward.core.dict.base.AbstractDictMap;

/**
 * 用于删除业务的字典
 *
 * @author fengshuonan
 * @date 2017-05-06 15:01
 */
public class DeleteDict extends AbstractDictMap {

    @Override
    public void init() {
        put("roleId","角色名称");
        put("menuId", "菜单名称");
        put("dictId", "字典名称");
    }

    @Override
    protected void initBeWrapped() {
        putFieldWrapperMethodName("roleId","getCacheObject");
        putFieldWrapperMethodName("menuId","getCacheObject");
        putFieldWrapperMethodName("dictId","getCacheObject");

    }
}
