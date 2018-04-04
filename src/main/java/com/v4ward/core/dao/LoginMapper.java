package com.v4ward.core.dao;

import com.v4ward.core.model.Manager;
import com.v4ward.core.model.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LoginMapper {
    Manager getManager(@Param("account") String username);

    List<Map<String,Object>> getMenuList(@Param("id") String managerId);

    List<Menu> getAuthList(@Param("id") String managerId);

    List<String> getRoleTypeList(@Param("id") String managerId);
}
