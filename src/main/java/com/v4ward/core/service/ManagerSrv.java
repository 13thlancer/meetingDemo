package com.v4ward.core.service;

import com.v4ward.core.model.Manager;
import com.v4ward.core.poiEntity.ManagerPoiEntity;
import com.v4ward.core.tips.Tip;

import java.util.List;
import java.util.Map;

public interface ManagerSrv {

    void insertManager(Manager manager);

    Manager getByAccount(String account);

    List<Map<String,Object>> selectUsers(String name, String beginTime, String endTime,String code);

    Manager getByUserId(String userId);

    String getRoleNameById(String id);

    void updateManager(Manager manager);

    Tip setRoles(String userId, String roleIds);

    void setStatus(String userId, int code);

    void updateManagerPwd(Manager manager);

    List<ManagerPoiEntity> listManager();

    Tip setOrg(String userId, String orgIds);

    void insertManagerPoi(ManagerPoiEntity managerPoi);

    List<Map<String,Object>> selectUserPhoneById(String id);

    void insertManagerErrorDataPoi(ManagerPoiEntity managerPoi);

    List<ManagerPoiEntity> listErrorManagerByManagerId(String id);

    void delErrorManagerByManagerId(String id);

    void updateHeadImg(String userId, String filePath);

    List<Manager> selectUsersNoIm();

    void updateImByUserId(Manager manager);
}
