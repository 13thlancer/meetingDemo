package com.v4ward.core.service.impl;

import com.v4ward.core.common.CoreConstant;
import com.v4ward.core.dao.ManagerMapper;
import com.v4ward.core.dao.OrgMapper;
import com.v4ward.core.dao.RoleMapper;
import com.v4ward.core.model.*;
import com.v4ward.core.poiEntity.ManagerPoiEntity;
import com.v4ward.core.service.ManagerSrv;
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
public class ManagerSrvImpl implements ManagerSrv {

    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private OrgMapper orgMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void insertManager(Manager manager) {
        this.managerMapper.insertManager(manager);
    }

    @Override
    public void insertManagerPoi(ManagerPoiEntity managerPoi) {
        this.managerMapper.insertManagerPoi(managerPoi);
        if(null != managerPoi.getOrgNum()){
            Org org = this.orgMapper.getOrgByNum(managerPoi.getOrgNum());
            ManagerOrg mo = new ManagerOrg();
            mo.setManagerId(managerPoi.getId());
            mo.setOrgId(org.getId());
            this.managerMapper.insertManagerOrg(mo);
        }
    }

    @Override
    public List<Map<String, Object>> selectUserPhoneById(String id) {
        return this.managerMapper.selectUserPhoneById(id);
    }

    @Override
    public void insertManagerErrorDataPoi(ManagerPoiEntity managerPoi) {
        this.managerMapper.insertManagerErrorDataPoi(managerPoi);
    }

    @Override
    public List<ManagerPoiEntity> listErrorManagerByManagerId(String id) {
        return this.managerMapper.listErrorManagerByManagerId(id);

    }

    @Override
    public void delErrorManagerByManagerId(String id) {
        this.managerMapper.delErrorManagerByManagerId(id);
    }

    @Override
    public void updateHeadImg(String userId, String filePath) {
        this.managerMapper.updateHeadImg(userId, filePath);
    }

    @Override
    public List<Manager> selectUsersNoIm() {
        return this.managerMapper.selectUsersNoIm();
    }

    @Override
    public void updateImByUserId(Manager manager) {
        this.managerMapper.updateImByUserId(manager);
    }


    @Override
    public Manager getByAccount(String account) {
        return managerMapper.getByAccount(account);
    }

    @Override
    public List<Map<String, Object>> selectUsers(String name, String beginTime, String endTime,String code) {
        String fullCodes = "";
        if(null != code && code != ""){
            Org org = this.orgMapper.getOrgByOrgCode(code);
            fullCodes = org.getFullCodes();
        }
        List<Map<String, Object>> userList =  managerMapper.selectUsers(name,beginTime,endTime,fullCodes);
        for(Map manager:userList){
            if(manager.containsKey("id")) {
                String managerId = manager.get("id").toString();
                List<String> roles =  managerMapper.getRoleNameById(managerId);
                StringBuffer roleNames = new StringBuffer();
                if(!roles.isEmpty()) {
                    for (String roleName : roles) {
                        roleNames.append(",");
                        roleNames.append(roleName);
                    }
                    manager.put("roleName", roleNames.toString().substring(1));
                }else{
                    manager.put("roleName", roleNames.toString().substring(0));
                }
            }
        }
        return userList;
    }

    @Override
    public Manager getByUserId(String userId) {
        return managerMapper.getByUserId(userId);
    }

    @Override
    public String getRoleNameById(String userId) {
        List<String> roles =  managerMapper.getRoleNameById(userId);
        StringBuffer roleNames = new StringBuffer();
        if(!roles.isEmpty()){
            for(String roleName:roles){
                roleNames.append(",");
                roleNames.append(roleName);

            }
            return  roleNames.toString().substring(1);
        }
        return  roleNames.toString().substring(0);
    }

    @Override
    public void updateManager(Manager manager) {
        String id = manager.getId();
        Manager mgr = this.managerMapper.getByUserId(id);
        if(!manager.getPhone().equals(mgr.getPhone())){
            ManagerUsedPhone mup = new ManagerUsedPhone();
            mup.setManagerId(mgr.getId());
            mup.setUsedPhone(mgr.getPhone());
            mup.setStartTime(mgr.getCreatetime());
            this.managerMapper.insertManagerUsedPhone(mup);
        }
        managerMapper.updateManager(manager);
    }


    @Override
    public Tip setRoles(String userId, String roleIds) {
        String[] ids = roleIds.split(",");
        //判断是否选择根目录菜单
        if(isRootMenu(-1,ids)){
            return new ErrorTip(CoreConstant.CHOOSE_ROLE_IS_ROOT_CODE,CoreConstant.CHOOSE_ROLE_IS_ROOT_MSG);
        }
        //判断是否有重复的超级用户角色
        if(hasMultiSuperRole(1,ids)){
            return new ErrorTip(CoreConstant.CHOOSE_MULTI_SUPER_ROLE_CODE,CoreConstant.CHOOSE_MULTI_SUPER_ROLE_MSG);
        }

        // 删除该用户所有的角色
        this.managerMapper.deleteRolesById(userId);

        if (ToolUtil.isOneEmpty(userId, roleIds)) {
            return new SuccessTip();
        }
        // 添加新的角色
        for (String id : ids) {
            Role role = this.roleMapper.getByRoleNum(Integer.valueOf(id));
            ManagerRole managerRole = new ManagerRole();
            managerRole.setManagerId(userId);
            managerRole.setRoleId(id);
            managerRole.setRoleType(role.getPnum());
            managerRole.setOrgId(role.getOrgId());
            this.managerMapper.insertManagerRole(managerRole);
        }
        return new SuccessTip();
    }

    //判断是否有重复的超级用户角色
    private boolean hasMultiSuperRole(int i, String[] ids) {
        int count = this.managerMapper.getCountByRoleType(i,ids);
        if(count>1){
            return true;
        }
        return false;
    }

    //判断是否选择根目录菜单
    private boolean isRootMenu(int i, String[] ids) {
        int count = this.managerMapper.getCountByRoleType(i,ids);
        if(count>0){
            return true;
        }
        return false;
    }

    @Override
    public void setStatus(String userId, int code) {
        managerMapper.updateStatus(userId,code);
    }

    @Override
    public void updateManagerPwd(Manager manager) {
        managerMapper.updateManagerPwd(manager);
    }

    @Override
    public List<ManagerPoiEntity> listManager() {
        return managerMapper.listManager();
    }

    @Override
    public Tip setOrg(String userId, String codes) {
        String[] cds = codes.split(",");
        if(cds.length>1){
            return new ErrorTip(CoreConstant.MANAGER_MULTI_ORG_CODE, CoreConstant.MANAGER_MULTI_ORG_MSG);
        }
        // 删除该用户所有的组织
        this.managerMapper.deleteOrgById(userId);
        // 添加新的角色
        for (String code : codes.split(",")) {
            Org org = this.orgMapper.getOrgByOrgCode(code);
            ManagerOrg managerOrg = new ManagerOrg();
            managerOrg.setManagerId(userId);
            managerOrg.setOrgId(org.getId());
            this.managerMapper.insertManagerOrg(managerOrg);
        }
        return new SuccessTip();
    }


}
