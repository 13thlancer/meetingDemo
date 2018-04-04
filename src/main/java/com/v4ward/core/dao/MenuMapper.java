package com.v4ward.core.dao;

import com.v4ward.core.model.Menu;
import com.v4ward.core.model.ZTreeNode;
import com.v4ward.core.poiEntity.MenuPoiEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MenuMapper {
    List<Map<String,Object>> listMenus(Menu menu);


    List<ZTreeNode> menuTreeList();

    Menu selectByCode(@Param("code") String code);

    Menu selectById(String id);

    void insert(Menu menu);

    String checkCode(String code);

    void delMenu(String id);

    List<Menu> selectListByPcode(String code);

    List<ZTreeNode> menuTreeListByMenuIds(List<Long> menuIds);

    List<Long> getMenuIdsByRoleId(@Param("roleId") String roleId);

    List<Long> getMenuIdsByRoleNum(@Param("rolenum") Integer rolenum);

//    Map<String,Object> selectMapByCode(@Param("code")String code);

    Map<String,Object> selectMapById(@Param("id") String num);

    Menu selectPmenu(Menu temp);

    void updateById(Menu menu);

    List<Menu> listChildMenuByPcode(@Param("pcode") String pcode);

    Menu getMenuByPCode(@Param("pcode") String pCode);

    String getMaxCodeByPcode(@Param("pcode") String pCode);

    Integer getMaxSeqByPcode(@Param("pcode") String pCode);

    List<MenuPoiEntity> listExportMenu();

    Menu getPmenu(Menu menu);

    Map<String,Object> selectMenuById(@Param("id") String id);

    Menu selectMenuByMenuId(@Param("id") String menuId);

    void updateMenuSeqDown(@Param("pCode") String pcode, @Param("seq") Integer seq);

    void updateMenuSeqUp(@Param("pCode") String pcode, @Param("seq") Integer seq);
}