package com.v4ward.core.service.impl;

import com.v4ward.core.common.CoreConstant;
import com.v4ward.core.dao.ManagerMapper;
import com.v4ward.core.dao.OrgMapper;
import com.v4ward.core.dao.RoleMapper;
import com.v4ward.core.model.*;
import com.v4ward.core.service.RoleSrv;
import com.v4ward.core.tips.ErrorTip;
import com.v4ward.core.tips.SuccessTip;
import com.v4ward.core.tips.Tip;
import com.v4ward.core.utils.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RoleSrvImpl implements RoleSrv {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private OrgMapper orgMapper;

    @Autowired
    private ManagerMapper managerMapper;

    @Override
    public Tip insertRole(Role role) {
        role.setNum(orgSetCode(role.getPnum().toString()));
        if(role.getOrgCode() != null && role.getOrgCode() != ""){
            //通过组织code获取组织id，存入数据库
            Org org = this.orgMapper.getOrgByOrgCode(role.getOrgCode());
            role.setOrgId(org.getId());
        }
        this.roleMapper.insertRole(role);
        return new SuccessTip();
    }

    @Override
    public Role selectById(String roleId) {
        return roleMapper.getById(roleId);
    }

    @Override
    public String getPname(Integer pnum) {
        return roleMapper.getPname(pnum);
    }

    @Override
    public List<ZTreeNode> roleTreeList() {
        return roleMapper.roleTreeList();
    }

    @Override
    public List<Map<String, Object>> selectRoles(String roleName) {
        List<Map<String, Object>> roles =  roleMapper.selectRoles(roleName);
        for(Map role : roles){
            if(role.containsKey("pnum")){
                Integer pnum = Integer.valueOf(role.get("pnum").toString());
                String pname = roleMapper.getPname(pnum);
                role.put("pName",pname);
            }
        }
        return roles;
    }

    @Override
    public String checkNum(Integer num) {
        return roleMapper.checkNum(num);
    }

    @Override
    public void updateById(Role role) {

        if(role.getPnum() ==1){
            //通过组织code获取组织id，存入数据库
            Org org = this.orgMapper.getOrgByOrgCode(role.getOrgCode());
            role.setOrgId(org.getId());
        }
        this.roleMapper.updateById(role);
    }

    @Override
    public Tip delRoleById(String roleId) {
        Role role = this.roleMapper.getById(roleId);
        //判断角色是否为根节点
        if(role.getPnum() == -1){
            return new ErrorTip(CoreConstant.ROLE_IS_ROOT_CODE,CoreConstant.ROLE_IS_ROOT_MSG);
        }
        //判断角色是否已分配给用户
        List<ManagerRole> mr = this.roleMapper.getMnagerByRoleNum(role.getNum());
        if(!mr.isEmpty()){
            return new ErrorTip(CoreConstant.ROLE_HAS_MANAGER_CODE,CoreConstant.ROLE_HAS_MANAGER_MSG);
        }
        //删除角色
        this.roleMapper.deleteById(roleId);
        // 删除该角色所有的权限
        this.roleMapper.deleteRolesByrolenum(role.getNum());
        //删除该角色与组织的对应关系
        this.roleMapper.deleteRoleOrgByRoleNum(role.getNum());
        return new SuccessTip();
    }

    @Override
    public void setAuthority(String roleId, String ids) {
        Role role = this.roleMapper.getById(roleId);
        Integer rolenum= role.getNum();
        // 删除该角色所有的权限
        this.roleMapper.deleteRoleOrgByRoleNum(rolenum);
        // 添加新的权限
        for (String id : ids.split(",")) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRolenum(rolenum);
            roleMenu.setMenuid(id);
            this.roleMapper.insertRoleMenu(roleMenu);
        }
    }

    @Override
    public List<ZTreeNode> roleTreeListByRoleId(String userId) {
        List<String> roleids = this.managerMapper.getRoleIdById(userId);
//        String roleid = theUser.getRoleid();
        if (roleids.isEmpty()) {
            List<ZTreeNode> roleTreeList = this.roleMapper.roleTreeList();
            return roleTreeList;
        } else {

            int size = roleids.size();
            String[] strArray =  (String[])roleids.toArray(new String[size]);
            List<ZTreeNode> roleTreeListByUserId = this.roleMapper.roleTreeListByRoleId(strArray);
            return roleTreeListByUserId;
        }
    }

    @Override
    public Role selectByRoleNum(Integer rolenum) {
        return  roleMapper.getByRoleNum(rolenum);
    }

    @Override
    public Role selectByRoleId(String roleId) {
        return  roleMapper.getByRoleId(roleId);
    }

    @Override
    public List<ZTreeNode> pRoleTreeList() {
        return this.roleMapper.pRoleTreeList();
    }

    /**
     * 根据请求的父级菜单编号设置组织code
     */
    private Integer orgSetCode(String pnum) {
        String MaxNum = this.roleMapper.getMaxNumByPnum(pnum);
        if (ToolUtil.isEmpty(MaxNum)){
            return Integer.valueOf(pnum+"1000");
        }
        Integer i =  Integer.valueOf(MaxNum);
        return i+1;
    }

}
