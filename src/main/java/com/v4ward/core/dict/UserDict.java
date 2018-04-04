package com.v4ward.core.dict;


import com.v4ward.core.dict.base.AbstractDictMap;

/**
 * 用户的字典
 *
 * @author fengshuonan
 * @date 2017-05-06 15:01
 */
public class UserDict extends AbstractDictMap {

    @Override
    public void init() {
        put("userId","账号");
        put("avatar","头像");
        put("account","账号");
        put("name","名字");
        put("birthday","生日");
        put("sex","性别");
        put("email","电子邮件");
        put("phone","电话");
        put("roleId","角色名称");
        put("deptid","部门名称");
        put("roleIds","角色名称集合");
    }

    @Override
    protected void initBeWrapped() {
        putFieldWrapperMethodName("roleId","getSingleRoleName");
        putFieldWrapperMethodName("userId","getUserAccountById");
        putFieldWrapperMethodName("roleIds","getRoleName");
    }
}
