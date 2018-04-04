package com.v4ward.core.service;

import com.v4ward.core.model.Manager;
import com.v4ward.core.model.Menu;

import java.util.List;
import java.util.Map;

public interface LoginSrv {

    Manager getManager(String username);

    List<Map<String,Object>> getMenuList(String managerId);

    List<Menu> getAuthList(String managerId);

    List<String> getRoleTypeList(String managerId);
}
