package com.v4ward.core.service.impl;



import com.v4ward.core.common.CoreConstant;
import com.v4ward.core.dao.MenuMapper;
import com.v4ward.core.dao.RoleMapper;
import com.v4ward.core.exception.BizExceptionEnum;
import com.v4ward.core.exception.BussinessException;
import com.v4ward.core.model.Menu;
import com.v4ward.core.model.Role;
import com.v4ward.core.model.ZTreeNode;
import com.v4ward.core.poiEntity.MenuPoiEntity;
import com.v4ward.core.service.MenuSrv;
import com.v4ward.core.tips.ErrorTip;
import com.v4ward.core.tips.SuccessTip;
import com.v4ward.core.tips.Tip;
import com.v4ward.core.utils.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.v4ward.core.common.CoreConstant.ORG_FULLCODES_SEPARATE;


//import org.springframework.cache.annotation.Cacheable;

@Service
@Transactional
public class MenuSrvImpl implements MenuSrv {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
//    @Cacheable(value = "content")
    public List<Map<String, Object>> listMenu(Menu menu) {
        return menuMapper.listMenus(menu);
    }

    @Override
    public List<ZTreeNode> menuTreeList() {
        return menuMapper.menuTreeList();
    }

    @Override
    public Map<String,Object> selectMapById(String id) {
        return menuMapper.selectMapById(id);
    }

    @Override
    public Menu selectPmenu(Menu temp) {
        return menuMapper.selectPmenu(temp);
    }

    @Override
    public Tip importExcel(List<MenuPoiEntity> menuList) {
        List<Map<String, Object>> menus = this.menuMapper.listMenus(new Menu());
        if(menus.size()>0){
            return new ErrorTip(CoreConstant.ORG_IS_NOT_EMPTY_CODE, CoreConstant.ORG_IS_NOT_EMPTY_MSG);
        }
        //转树形结构
        List<MenuPoiEntity> menuTree = buildByRecursive(menuList);
        //重新生成code和pcode并存入数据库
        rebuildMenuList(menuTree,"0");
        return new SuccessTip();
    }

    @Override
    public List<MenuPoiEntity> exportMenu() {
        return this.menuMapper.listExportMenu();
    }

    @Override
    public Tip upSeq(String menuId) {
        Menu menu = this.menuMapper.selectMenuByMenuId(menuId);
        if(menu.getSeq() == 0){
            return new ErrorTip(CoreConstant.ORG_IS_FIRST_CODE, CoreConstant.ORG_IS_FIRST_MSG);
        }
        this.menuMapper.updateMenuSeqUp(menu.getPcode(),menu.getSeq());
        return new SuccessTip();
    }

    @Override
    public Tip downSeq(String menuId) {
        Menu menu = this.menuMapper.selectMenuByMenuId(menuId);
        Integer maxSeq = this.menuMapper.getMaxSeqByPcode(menu.getPcode());
        if(maxSeq == menu.getSeq()){
            return new ErrorTip(CoreConstant.ORG_IS_LAST_CODE, CoreConstant.ORG_IS_LAST_MSG);
        }
        this.menuMapper.updateMenuSeqDown(menu.getPcode(),menu.getSeq());
        return new SuccessTip();
    }

    @Override
    public void insert(Menu menu) {
        menuSetPcode(menu);
        this.menuMapper.insert(menu);
    }

    @Override
    public String checkCode(String code) {
        return menuMapper.checkCode(code);
    }

    @Override
    public void delMenuContainSubMenus(String menuId) {
        Menu menu = menuMapper.selectById(menuId);

        //删除当前菜单
        menuMapper.delMenu(menuId);

        //删除所有子菜单
//        Wrapper<Menu> wrapper = new EntityWrapper<>();
//        wrapper = wrapper.like("pcodes", "%[" + menu.getCode() + "]%");
        List<Menu> menus = menuMapper.selectListByPcode( menu.getCode() + ",");
        if(menus.size()>0){
            for (Menu temp : menus) {
                menuMapper.delMenu(temp.getId());
            }
        }

    }

    @Override
    public Tip updateById(Menu menu) {
        //通过Id获取修改前的组织信息
        Menu tempMenu = this.menuMapper.getPmenu(menu);
        //通过pcode获取修改后父级组织
        Menu pMenu = this.menuMapper.getMenuByPCode(menu.getPcode());
        //判断是否修改父级组织
        if(menu.getPcode().equals(tempMenu.getPcode())){
            //判断是否为根节点
            if(null == pMenu){
                //修改组织编号和组织名称
                this.menuMapper.updateById(menu);
                return new SuccessTip();
            }
            //修改组织编号和组织名称
            this.menuMapper.updateById(menu);
            return new SuccessTip();
        }else{
            //判断是否为根节点
            if(tempMenu.getPcode().equals("1")){
                return new ErrorTip(CoreConstant.ORG_IS_ROOT_CODE, CoreConstant.ORG_IS_ROOT_MSG);
            }
            //设置组织编码、父级菜单编码、组织全称
            menuSetPcode(menu);
            //修改组织及其子组织
            Map<String,Object> codeMap = this.menuMapper.selectMenuById(menu.getId());
            String code = codeMap.get("code").toString();

            //修改组织编号、组织名称、父级组织、组织编码、编码全称和组织全称
            this.menuMapper.updateById(menu);
            updateChildOrg(code,menu.getCode());
        }
        return new SuccessTip();
    }

    @Override
    public List<Long> getMenuIdsByRoleNum(String id) {
        Role role = this.roleMapper.getById(id);
        Integer rolenum= role.getNum();
        return menuMapper.getMenuIdsByRoleNum(rolenum);
    }

    @Override
    public List<ZTreeNode> menuTreeListByMenuIds(List<Long> menuIds) {
        return menuMapper.menuTreeListByMenuIds(menuIds);
    }

    @Override
    public Menu selectByCode(String code) {
        return menuMapper.selectByCode(code);
    }


    /**
     *
     * @Description: 递归组织树逐条插入数据库
     *
     *
     * @MethodName: rebuildOrgList
     * @Parameters: [orgTree, pcode]
     * @ReturnType: void
     *
     * @author huangxiang
     * @date 2018/3/12 11:30
     */
    private void rebuildMenuList(List<MenuPoiEntity> menuTree, String pcode){
        Menu menu = new Menu();
        for (MenuPoiEntity child:menuTree){
            if(null != child.getChildren()){
                menu.setName(child.getName());
                menu.setPcode(pcode);
                menu.setNum(child.getCode());
                menu.setPnum(child.getPcode());
                menu.setUrl(child.getUrl());
                menu.setIsmenu(child.getIsMenu());
                menu.setStatus(1);
                menuSetPcode(menu);
                this.menuMapper.insert(menu);
                rebuildMenuList(child.getChildren(),menu.getCode());
            }else{
                menu.setName(child.getName());
                menu.setPcode(pcode);
                menu.setNum(child.getCode());
                menu.setPnum(child.getPcode());
                menu.setUrl(child.getUrl());
                menu.setIsmenu(child.getIsMenu());
                menu.setStatus(1);
                menuSetPcode(menu);
                this.menuMapper.insert(menu);
            }
        }
    }


    /**
     *
     * @Description: 更新子组织的组织编码、编码全称以及组织全称
     *
     *
     * @MethodName: updateChildOrg
     * @Parameters: [pcode, code]
     * @ReturnType: java.lang.String
     *
     * @author huangxiang
     * @date 2018/3/12 11:29
     */
    private String updateChildOrg(String pcode,String code) {
        //通过父级编码查询所有子树
        List<Menu> orglist = this.menuMapper.listChildMenuByPcode(pcode);
        while(!orglist.isEmpty()){
            for (Menu menu:orglist){
                String menuCode = menu.getCode();
                //修改父级编码、组织编码、编码全称以及组织全称
                menu.setPcode(code);
                menuSetPcode(menu);
                this.menuMapper.updateById(menu);
                return updateChildOrg(menuCode,menu.getCode());
            }
        }
        return "success";
    }


    /**
     *
     * @Description: 通过父级组织信息设置组织的编码、序号、编码全称和全称
     *
     *
     * @MethodName: orgSetPcode
     * @Parameters: [org]
     * @ReturnType: void
     *
     * @author huangxiang
     * @date 2018/3/12 11:27
     */
    private void menuSetPcode(@Valid Menu menu) {
        if (ToolUtil.isEmpty(menu.getPcode()) || menu.getPcode().equals("0")) {
            menu.setPcode("1");
            menu.setPnum("1");
            menu.setSeq(1);
            menu.setLevels(1);

            //设置组织编码和序号
//            org.setCode(orgSetCode("1"));
            menuSetCodeAndSeq(menu,"1");
            //设置组织全部编码
            menu.setFullCodes("1" + ORG_FULLCODES_SEPARATE + menu.getCode() + ORG_FULLCODES_SEPARATE);

        } else {
            String pCode = menu.getPcode();
            //获取父级组织
            Menu pMenu = this.menuMapper.getMenuByPCode(pCode);
            Integer pLevels = pMenu.getLevels();
            menu.setPcode(pMenu.getCode());
            menu.setPnum(pMenu.getNum());
            //设置组织编码和序号
//            org.setCode(orgSetCode(pCode));
            menuSetCodeAndSeq(menu,pCode);
            //如果编号和父编号一致会导致无限递归
            if (menu.getCode().equals(menu.getPcode())) {
                throw new BussinessException(BizExceptionEnum.MENU_PCODE_COINCIDENCE);
            }
            menu.setLevels(pLevels + 1);
            menu.setFullCodes(pMenu.getFullCodes() + menu.getCode() + ORG_FULLCODES_SEPARATE);
        }
    }


    /**
     *
     * @Description: 设置组织编号和序号
     *
     *
     * @MethodName: orgSetCodeAndSeq
     * @Parameters: [org, pCode]
     * @ReturnType: void
     *
     * @author huangxiang
     * @date 2018/3/12 11:26
     */
    private void menuSetCodeAndSeq(Menu menu, String pCode){
        String MaxCode = this.menuMapper.getMaxCodeByPcode(pCode);
        if (ToolUtil.isEmpty(MaxCode)){
            menu.setCode(pCode+"10");
            menu.setSeq(0);
        }else{
            Integer seq = this.menuMapper.getMaxSeqByPcode(pCode);
            Integer i =  Integer.valueOf(MaxCode.substring(MaxCode.length()-2));
            menu.setCode(MaxCode.substring(0,MaxCode.length()-2)+Integer.toString(i+1));
            menu.setSeq(seq+1);
        }
    }


    /**
     * 根据请求的父级菜单编号设置组织code
     */
    private String orgSetCode(String pCode) {
        String MaxCode = this.menuMapper.getMaxCodeByPcode(pCode);
        if (ToolUtil.isEmpty(MaxCode)){
            return pCode+"10";
        }
        Integer i =  Integer.valueOf(MaxCode.substring(MaxCode.length()-2));
        return MaxCode.substring(0,MaxCode.length()-2)+Integer.toString(i+1);
    }

    /**
     * 使用递归方法建树
     * @param menulist
     * @return
     */
    private static List<MenuPoiEntity> buildByRecursive(List<MenuPoiEntity> menulist) {
        List<MenuPoiEntity> trees = new ArrayList<MenuPoiEntity>();
        for (MenuPoiEntity menuPoiEntity : menulist) {
            if (menuPoiEntity.getPcode() == null) {
                trees.add(findChildren(menuPoiEntity,menulist));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     * @param menulist
     * @return
     */
    private static MenuPoiEntity findChildren(MenuPoiEntity menuPoiEntity, List<MenuPoiEntity> menulist) {
        for (MenuPoiEntity menu : menulist) {
            if(menuPoiEntity.getCode().equals(menu.getPcode())) {
                if (menuPoiEntity.getChildren() == null) {
                    menuPoiEntity.setChildren(new ArrayList<MenuPoiEntity>());
                }
                menuPoiEntity.getChildren().add(findChildren(menu,menulist));
            }
        }
        return menuPoiEntity;
    }

}
