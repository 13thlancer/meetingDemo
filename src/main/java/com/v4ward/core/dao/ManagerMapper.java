package com.v4ward.core.dao;

import com.v4ward.core.model.Manager;
import com.v4ward.core.model.ManagerOrg;
import com.v4ward.core.model.ManagerRole;
import com.v4ward.core.model.ManagerUsedPhone;
import com.v4ward.core.poiEntity.ManagerPoiEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ManagerMapper {

    void insertManager(Manager manager);

    void insertManagerPoi(ManagerPoiEntity managerPoi);

    Manager getByAccount(String account);

    List<Map<String,Object>> selectUsers(@Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("fullCodes") String fullCodes);

    Manager getByUserId(@Param("id") String userId);

    List<String> getRoleNameById(@Param("id") String userId);

    void updateManager(Manager manager);

    List<String> getRoleIdById(@Param("id") String userId);

    void deleteRolesById(@Param("id") String userId);

    void insertManagerRole(ManagerRole managerRole);

    void updateStatus(@Param("id")String userId, @Param("code") int code);

    void updateManagerPwd(Manager manager);

    List<ManagerPoiEntity> listManager();

    void insertManagerOrg(ManagerOrg managerOrg);

    void deleteOrgById(@Param("id")String userId);

    int getCountByRoleType(@Param("pnum")int i, @Param("ids")String[] ids);

    void insertManagerUsedPhone(ManagerUsedPhone manager);

    List<Map<String,Object>> selectUserPhoneById(@Param("id")String id);

    void insertManagerErrorDataPoi(ManagerPoiEntity managerPoi);

    List<ManagerPoiEntity> listErrorManagerByManagerId(@Param("id")String id);

    void delErrorManagerByManagerId(@Param("id")String id);

    void updateHeadImg(@Param("id")String userId, @Param("url")String filePath);

    List<Manager> selectUsersNoIm();

    void updateImByUserId(Manager manager);
}