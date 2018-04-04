package com.v4ward.core.service.impl;

import com.v4ward.core.service.LoginSrv;
import com.v4ward.core.dao.LoginMapper;
import com.v4ward.core.model.Manager;
import com.v4ward.core.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LoginSrvImpl implements LoginSrv {

    @Autowired
    private LoginMapper loginMapper;

    @Override
    public Manager getManager(String username) {
        Manager manager = loginMapper.getManager(username);
        return manager;
    }

    @Override
    public List<Map<String,Object>> getMenuList(String managerId) {
        return loginMapper.getMenuList(managerId);
    }

    @Override
    public List<Menu> getAuthList(String managerId) {
        return loginMapper.getAuthList(managerId);
    }

    @Override
    public List<String> getRoleTypeList(String managerId) {
        return this.loginMapper.getRoleTypeList(managerId);
    }

}
