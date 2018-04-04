package com.v4ward.core.factory;


import com.v4ward.core.dao.ManagerMapper;
import com.v4ward.core.dao.RoleMapper;
import com.v4ward.core.utils.ToolUtil;
import com.v4ward.core.utils.cache.CacheKey;
import com.v4ward.core.dao.DictMapper;
import com.v4ward.core.dao.MenuMapper;
import com.v4ward.core.kit.StrKit;
import com.v4ward.core.model.Dict;
import com.v4ward.core.model.Manager;
import com.v4ward.core.model.Menu;
import com.v4ward.core.model.Role;
import com.v4ward.core.utils.SpringContextHolder;
import com.v4ward.core.utils.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 常量的生产工厂
 *
 * @author fengshuonan
 * @date 2017年2月13日 下午10:55:21
 */
@Component
@DependsOn("springContextHolder")
public class ConstantFactory implements IConstantFactory {

    private RoleMapper roleMapper = SpringContextHolder.getBean(RoleMapper.class);
    private ManagerMapper managerMapper = SpringContextHolder.getBean(ManagerMapper.class);
    private MenuMapper menuMapper = SpringContextHolder.getBean(MenuMapper.class);
    private DictMapper dictMapper = SpringContextHolder.getBean(DictMapper.class);


    public static IConstantFactory me() {
        return SpringContextHolder.getBean("constantFactory");
    }

    /**
     *
     * @Description: 根据用户id获取用户名称
     *
     * @MethodName: getUserNameById
     * @Parameters: [userid]
     * @ReturnType: java.lang.Object
     *
     * @author huangxiang
     * @date 2017/12/26 16:06
     */
    @Override
    public Object getUserNameById(String userid) {
        Manager manager = managerMapper.getByUserId(userid);
        if (manager != null) {
            return manager.getName();
        } else {
            return "--";
        }
    }

    /**
     * 通过角色id获取角色名称
     */
    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.SINGLE_ROLE_NAME + "'+#roleId")
    public String getSingleRoleName(String roleId) {
        if (("0").equals(roleId)) {
            return "--";
        }
        Role roleObj = roleMapper.getById(roleId);
        if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
            return roleObj.getName();
        }
        return "";
    }


    /**
     * 通过角色num获取角色名称
     */
    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.SINGLE_ROLE_NAME + "'+#roleId")
    public String getSingleRoleNameByRoleNum(Integer rolenum) {
        if (("0").equals(rolenum)) {
            return "--";
        }
        Role roleObj = roleMapper.getByRoleNum(rolenum);
        if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
            return roleObj.getName();
        }
        return "";
    }

    /**
     * 获取菜单的名称们(多个)
     */
    @Override
    public String getMenuNames(String menuIds) {

        StringBuilder sb = new StringBuilder();
        for (String menuid : menuIds.split(",")) {
            Menu menuObj = menuMapper.selectById(menuid);
            if (ToolUtil.isNotEmpty(menuObj) && ToolUtil.isNotEmpty(menuObj.getName())) {
                sb.append(menuObj.getName()).append(",");
            }
        }
        return StrKit.removeSuffix(sb.toString(), ",");
    }

    /**
     * 获取菜单的名称们(多个)
     */
    @Override
    public String getMenuNamesByNum(String nums) {

        StringBuilder sb = new StringBuilder();
        for (String num : nums.split(",")) {
            Menu menuObj = menuMapper.selectByCode(num);
            if (ToolUtil.isNotEmpty(menuObj) && ToolUtil.isNotEmpty(menuObj.getName())) {
                sb.append(menuObj.getName()).append(",");
            }
        }
        return StrKit.removeSuffix(sb.toString(), ",");
    }

    /**
     * 根据用户id获取用户账号
     *
     * @author stylefeng
     * @date 2017年5月16日21:55:371
     */
    @Override
    public String getUserAccountById(String userId) {
        Manager manager = managerMapper.getByUserId(userId);
        if (manager != null) {
            return manager.getAccount();
        } else {
            return "--";
        }
    }

    /**
     * 通过角色ids获取角色名称
     */
    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.ROLES_NAME + "'+#roleIds")
    public String getRoleName(String roleIds) {
        StringBuilder sb = new StringBuilder();
        for (String role : roleIds.split(",")) {
            Role roleObj = roleMapper.getById(role);
            if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
                sb.append(roleObj.getName()).append(",");
            }
        }
        return StrKit.removeSuffix(sb.toString(), ",");
    }

    /**
     * 获取菜单名称
     */
    @Override
    public String getMenuName(String menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            return "";
        } else {
            Menu menu = menuMapper.selectById(menuId);
            if (menu == null) {
                return "";
            } else {
                return menu.getName();
            }
        }
    }

    @Override
    public List<Dict> findInDict(String id) {
        if (ToolUtil.isEmpty(id)) {
            return null;
        } else {
            List<Dict> dicts = dictMapper.selectListByPid(id);
            if (dicts == null || dicts.size() == 0) {
                return null;
            } else {
                return dicts;
            }
        }
    }

    @Override
    public Object getDictName(String dictId) {
        if (ToolUtil.isEmpty(dictId)) {
            return "";
        } else {
            Dict dict = dictMapper.selectById(dictId);
            if (dict == null) {
                return "";
            } else {
                return dict.getName();
            }
        }
    }
}
