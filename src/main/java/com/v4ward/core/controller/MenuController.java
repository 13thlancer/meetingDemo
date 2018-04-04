package com.v4ward.core.controller;


import com.v4ward.core.common.IsMenu;
import com.v4ward.core.common.MenuStatus;
import com.v4ward.core.exception.BizExceptionEnum;
import com.v4ward.core.exception.BussinessException;
import com.v4ward.core.model.Menu;
import com.v4ward.core.model.ZTreeNode;
import com.v4ward.core.poiEntity.MenuPoiEntity;
import com.v4ward.core.service.MenuSrv;
import com.v4ward.core.tips.Tip;
import com.v4ward.core.utils.ToolUtil;
import com.v4ward.core.utils.poi.PoiUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 菜单控制器
 *
 * @Date 2017年2月12日21:59:14
 */
@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {

    private static String PREFIX = "/system/menu/";

    @Resource
    MenuSrv menuSrv;

    /**
     * 跳转到菜单列表列表页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "menu";
    }

    /**
     * 跳转到菜单列表列表页面
     */
    @RequestMapping(value = "/menu_add")
    public String menuAdd() {
        return PREFIX + "menu_add";
    }

    /**
     * 获取菜单列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Menu menu) {
        List<Map<String, Object>> menus = menuSrv.listMenu(menu);

        for(Map menuMap: menus){
            Integer status = (Integer) menuMap.get("ismenu");
            if (status == IsMenu.YES.getCode()){
                menuMap.put("isMenuName", IsMenu.YES.getMessage());
            }else if(status == IsMenu.NO.getCode()){
                menuMap.put("isMenuName", IsMenu.NO.getMessage());
            }
        }
        return menus;
    }

    @RequestMapping(value = "/selectMenuTreeList")
    @ResponseBody
    public List<ZTreeNode> selectMenuTreeList() {
        List<ZTreeNode> roleTreeList = menuSrv.menuTreeList();
        roleTreeList.add(ZTreeNode.createParent());
        return roleTreeList;
    }

    /**
     * 新增菜单
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Tip add(@Valid Menu menu, BindingResult result) {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }

        //判断是否存在该编号
        String existedMenuName = menuSrv.checkCode(menu.getCode());
        if (ToolUtil.isNotEmpty(existedMenuName)) {
            throw new BussinessException(BizExceptionEnum.EXISTED_THE_MENU);
        }
        //设置父级菜单编号
        menu.setStatus(MenuStatus.ENABLE.getCode());
        this.menuSrv.insert(menu);
        return SUCCESS_TIP;
    }



    /**
     * 删除菜单
     */
    @RequestMapping(value = "/remove")
    @ResponseBody
    public Tip remove(@RequestParam String menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }

        menuSrv.delMenuContainSubMenus(menuId);
        return SUCCESS_TIP;
    }



    /**
     * 跳转到菜单详情列表页面
     */
    @RequestMapping(value = "/menu_edit/{id}")
    public String menuEdit(@PathVariable String id, Model model) {
        if (ToolUtil.isEmpty(id)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        Map<String,Object> menu = this.menuSrv.selectMapById(id);

        //获取父级菜单的id
        Menu temp = new Menu();
        temp.setCode(menu.get("pcode").toString());
        Menu pMenu = this.menuSrv.selectPmenu(temp);

        //如果父级是顶级菜单
        if (pMenu == null) {
//            menu.setPcode("0");
            menu.put("pcode","0");
            menu.put("pcodeName","顶级");
        } else {
            //设置父级菜单的code为父级菜单的id
            menu.put("pcode",pMenu.getCode());
            menu.put("pcodeName", pMenu.getName());
        }
        model.addAttribute("menu", menu);
        return PREFIX + "menu_edit";
    }


    /**
     * 修改菜单
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    public Tip edit(@Valid Menu menu, BindingResult result) {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }

        return this.menuSrv.updateById(menu);
    }


    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/menuTreeListByRoleId/{id}")
    @ResponseBody
    public List<ZTreeNode> menuTreeListByRoleId(@PathVariable String id) {
        List<Long> menuIds = this.menuSrv.getMenuIdsByRoleNum(id);
        if (ToolUtil.isEmpty(menuIds)) {
            List<ZTreeNode> roleTreeList = this.menuSrv.menuTreeList();
            return roleTreeList;
        } else {
            List<ZTreeNode> roleTreeListByUserId = this.menuSrv.menuTreeListByMenuIds(menuIds);
            return roleTreeListByUserId;
        }
    }

    /*
   * 导入
   */
    @RequestMapping(method = RequestMethod.POST,value = "/importExcel")
    @ResponseBody
    public Tip importExcel(MultipartFile file){
        //解析excel，
        List<MenuPoiEntity> menuList = PoiUtils.importExcel(file,0,1,MenuPoiEntity.class);
        return this.menuSrv.importExcel(menuList);
    }


    /*
    * 导出
    */
    @RequestMapping("/export")
    @ResponseBody
    public void orgExport(HttpServletResponse response){
        //模拟从数据库获取需要导出的数据
        List<MenuPoiEntity> menuList = this.menuSrv.exportMenu();
        //导出
        PoiUtils.exportExcel(menuList,"菜单","菜单",MenuPoiEntity.class,"菜单.xls",response);
    }


    /*
     * 导出模版
     */
    @RequestMapping("/excelModel")
    @ResponseBody
    public void excelModel(HttpServletResponse response){
        List<MenuPoiEntity> list= new ArrayList<>();
        list.add(MenuPoiEntity.newInstance("1","","主菜单","#",1));
        list.add(MenuPoiEntity.newInstance("1100","1","一级子菜单","/xxx",1));
        PoiUtils.exportExcel(list,null,"菜单模版",MenuPoiEntity.class,"菜单模版.xls",response);
    }

    /**
     * 同级上移
     */
    @RequestMapping(value = "/upSeq")
    @ResponseBody
    public Tip upSeq(@RequestParam String menuId){
        return this.menuSrv.upSeq(menuId);
    }

    /**
     * 同级下移
     */
    @RequestMapping(value = "/downSeq")
    @ResponseBody
    public Tip downSeq(@RequestParam String menuId){
        return this.menuSrv.downSeq(menuId);
    }



}
