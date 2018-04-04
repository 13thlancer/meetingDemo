package com.v4ward.core.dict;


import com.v4ward.core.dict.base.AbstractDictMap;

/**
 * 角色的字典
 *
 * @author fengshuonan
 * @date 2017-05-06 15:01
 */
public class RoleDict extends AbstractDictMap {

    @Override
    public void init() {
        put("rolenum","角色名称");
        put("num","角色排序");
        put("pnum","角色的父级");
        put("name","角色名称");
        put("tips","备注");
        put("ids","资源名称");
    }

    @Override
    protected void initBeWrapped() {
        putFieldWrapperMethodName("pnum","getSingleRoleName");
        putFieldWrapperMethodName("num","getSingleRoleName");
        putFieldWrapperMethodName("rolenum","getSingleRoleNameByRoleNum");
        putFieldWrapperMethodName("ids","getMenuNamesByNum");
    }
}
