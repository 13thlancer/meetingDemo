package com.v4ward.core.factory;


import com.v4ward.core.model.Dict;

import java.util.List;

/**
 * 常量生产工厂的接口
 *
 * @author fengshuonan
 * @date 2017-06-14 21:12
 */
public interface IConstantFactory {

    Object getUserNameById(String userid);

    Object getSingleRoleName(String roleId);


    Object getSingleRoleNameByRoleNum(Integer rolenum);

    /**
     * 获取菜单的名称们(多个)
     */
    String getMenuNames(String menuIds);

    /**
     * 获取菜单的名称们(多个)
     */
    String getMenuNamesByNum(String nums);

    /**
     * 根据用户id获取用户账号
     *
     * @author stylefeng
     * @date 2017年5月16日21:55:371
     */
    String getUserAccountById(String userId);

    /**
     * 通过角色ids获取角色名称
     */
    String getRoleName(String roleIds);

    Object getMenuName(String menuId);

    List<Dict> findInDict(String id);

    Object getDictName(String dictId);
}
