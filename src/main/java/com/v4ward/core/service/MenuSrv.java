package com.v4ward.core.service;



import com.v4ward.core.model.Menu;
import com.v4ward.core.model.ZTreeNode;
import com.v4ward.core.poiEntity.MenuPoiEntity;
import com.v4ward.core.tips.Tip;

import java.util.List;
import java.util.Map;

public interface MenuSrv {
    List<Map<String,Object>> listMenu(Menu menu);


    List<ZTreeNode> menuTreeList();

//    Menu selectById(int code);

    void insert(Menu menu);

    String checkCode(String code);

    void delMenuContainSubMenus(String menuId);

    Tip updateById(Menu menu);

    List<Long> getMenuIdsByRoleNum(String id);

    List<ZTreeNode> menuTreeListByMenuIds(List<Long> menuIds);

    Menu selectByCode(String code);

    Map<String,Object> selectMapById(String id);

    Menu selectPmenu(Menu temp);

    Tip importExcel(List<MenuPoiEntity> menuList);

    List<MenuPoiEntity> exportMenu();

    Tip upSeq(String menuId);

    Tip downSeq(String menuId);
}
