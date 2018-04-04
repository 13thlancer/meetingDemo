package com.v4ward.core.service;

import com.v4ward.core.model.Role;
import com.v4ward.core.model.ZTreeNode;
import com.v4ward.core.tips.Tip;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RoleSrv {
    Tip insertRole(Role role);

    Role selectById(String roleId);

    String getPname(Integer pnum);

    List<ZTreeNode> roleTreeList();

    List<Map<String,Object>> selectRoles(@Param("condition") String roleName);

    String checkNum(Integer num);

    void updateById(Role role);

    Tip delRoleById(String roleId);

    void setAuthority(String roleId, String ids);

    List<ZTreeNode> roleTreeListByRoleId(String userId);

    Role selectByRoleNum(Integer rolenum);

    Role selectByRoleId(String roleId);

    List<ZTreeNode> pRoleTreeList();
}
