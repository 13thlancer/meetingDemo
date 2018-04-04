package com.v4ward.core.controller;

import com.v4ward.core.annotation.BussinessLog;
import com.v4ward.core.dict.RoleDict;
import com.v4ward.core.exception.BizExceptionEnum;
import com.v4ward.core.exception.BussinessException;
import com.v4ward.core.factory.ConstantFactory;
import com.v4ward.core.log.LogObjectHolder;
import com.v4ward.core.model.Role;
import com.v4ward.core.service.RoleSrv;
import com.v4ward.core.utils.ToolUtil;
import com.v4ward.core.model.ZTreeNode;
import com.v4ward.core.tips.Tip;
import com.v4ward.core.kit.CacheKit;
import com.v4ward.core.utils.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    private static String PREFIX = "/system/role/";

    @Autowired
    private RoleSrv roleSrv;
    /**
     * 跳转到添加角色
     */
    @RequestMapping(value = "/role_add")
    public String roleAdd() {
        return PREFIX + "role_add";
    }

    /**
     * 跳转到修改角色
     */
    @RequestMapping(value = "/role_edit/{roleId}")
    public String roleEdit(@PathVariable String roleId, Model model) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        Role role = roleSrv.selectById(roleId);
        String pName = roleSrv.getPname(role.getPnum());
        model.addAttribute(role);
        model.addAttribute("pName",pName);
        return PREFIX + "role_edit";
    }

    /**
     * 跳转到角色分配
     */
    @RequestMapping(value = "/role_assign/{id}")
    public String roleAssign(@PathVariable("id") String id, Model model) {
        if (ToolUtil.isEmpty(id)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        model.addAttribute("id", id);
        Role role = roleSrv.selectByRoleId(id);

        model.addAttribute("roleName", role.getName());
        return PREFIX + "role_assign";
    }



    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String roleName) {
        List<Map<String, Object>> roles = roleSrv.selectRoles(super.getPara("roleName"));
        return roles;
    }


    /**
     * 角色新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Tip add(@Valid Role role, BindingResult result) {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        return this.roleSrv.insertRole(role);
    }


    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/roleTreeList")
    @ResponseBody
    public List<ZTreeNode> roleTreeList() {
        List<ZTreeNode> roleTreeList = this.roleSrv.roleTreeList();
        roleTreeList.add(ZTreeNode.createParent());
        return roleTreeList;
    }

    /**
     * 获取角色类型列表
     */
    @RequestMapping(value = "/pRoleTreeList")
    @ResponseBody
    public List<ZTreeNode> pRoleTreeList(){
        List<ZTreeNode> roleTreeList = this.roleSrv.pRoleTreeList();
        return roleTreeList;
    }

    /**
     * 角色修改
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    public Tip edit(@Valid Role role, BindingResult result) {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        this.roleSrv.updateById(role);
        return SUCCESS_TIP;
    }

    /**
     * 删除角色
     */
    @RequestMapping(value = "/remove")
    @ResponseBody
    public Tip remove(@RequestParam String roleId) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        return  this.roleSrv.delRoleById(roleId);
    }



    /**
     * 配置权限
     */
    @RequestMapping("/setAuthority")
    @ResponseBody
    public Tip setAuthority(@RequestParam("id") String id, @RequestParam("ids") String ids) {
        if (ToolUtil.isOneEmpty(id)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        this.roleSrv.setAuthority(id, ids);
        return SUCCESS_TIP;
    }


    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/roleTreeListByUserId/{userId}")
    @ResponseBody
    public List<ZTreeNode> roleTreeListByUserId(@PathVariable String userId) {
        return roleSrv.roleTreeListByRoleId(userId);
    }

}
