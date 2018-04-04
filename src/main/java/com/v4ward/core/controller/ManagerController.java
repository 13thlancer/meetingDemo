package com.v4ward.core.controller;

import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerColorImpl;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import com.v4ward.core.annotation.BussinessLog;
import com.v4ward.core.annotation.Permission;
import com.v4ward.core.common.CoreConstant;
import com.v4ward.core.common.ManagerStatus;
import com.v4ward.core.exception.BizExceptionEnum;
import com.v4ward.core.exception.BussinessException;
import com.v4ward.core.model.Manager;
import com.v4ward.core.model.UserAccount;
import com.v4ward.core.poiEntity.ManagerPoiEntity;
import com.v4ward.core.poiEntity.OrgPoiEntity;
import com.v4ward.core.service.ManagerSrv;
import com.v4ward.core.tips.ErrorTip;
import com.v4ward.core.tips.SuccessTip;
import com.v4ward.core.utils.Const;
import com.v4ward.core.utils.Md5Utils;
import com.v4ward.core.utils.ToolUtil;
import com.v4ward.core.dict.UserDict;
import com.v4ward.core.tips.Tip;
import com.v4ward.core.utils.poi.PoiUtils;
import com.v4ward.meeting.controller.PartmeetingController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

import static com.v4ward.core.common.ImComm.createUserAccount;

@Controller
@RequestMapping("/mgr")
public class ManagerController extends BaseController{

    private static String PREFIX = "/system/user/";

    public static Logger log = LoggerFactory.getLogger(ManagerController.class);


    @Autowired
    private ManagerSrv managerSrv;

    /**
     * 跳转到查看管理员列表的页面
     */
    @RequestMapping("/user_add")
    public String addView() {
        return PREFIX + "user_add";
    }

    /**
     * 跳转到修改密码界面
     */
    @RequestMapping("/user_chpwd")
    public String chPwd() {
        return PREFIX + "user_chpwd";
    }

    /**
     * 跳转到角色分配页面
     */
    @RequestMapping("/role_assign/{userId}")
    public String roleAssign(@PathVariable String userId, Model model) {
        if (ToolUtil.isEmpty(userId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        Manager user = managerSrv.getByUserId(userId);
        model.addAttribute("userId", userId);
        model.addAttribute("userAccount", user.getAccount());
        return PREFIX + "user_roleassign";
    }

    @RequestMapping("/org_assign/{userId}")
    public String org_assign(@PathVariable String userId, Model model){
        if (ToolUtil.isEmpty(userId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        Manager user = managerSrv.getByUserId(userId);
        model.addAttribute("userId", userId);
        model.addAttribute("userAccount", user.getAccount());
        return PREFIX + "user_orgassign";
    }

    /**
     * 跳转到编辑管理员页面
     */
    @RequestMapping("/user_edit/{userId}")
    public String userEdit(@PathVariable String userId, Model model) {
        Manager manager = managerSrv.getByUserId(userId);
        model.addAttribute(manager);
        model.addAttribute("roleName", managerSrv.getRoleNameById(manager.getId()));
        return PREFIX + "user_edit";
    }

    /**
     * 跳转到查看用户手机号历史页面
     */
    @RequestMapping("/user_phone/{userId}")
    public String userPhone(@PathVariable String userId, Model model){
        model.addAttribute("userId", userId);
        return PREFIX + "user_phone";
    }

    /**
     * 添加管理员
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Tip add(@Valid Manager manager, BindingResult result) {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        // 判断账号是否重复
        Manager theManager = managerSrv.getByAccount(manager.getPhone());
        if (theManager != null) {
            throw new BussinessException(BizExceptionEnum.USER_ALREADY_REG);
        }
        Manager curManager = (Manager) getSession().getAttribute("manager");
        // 完善账号信息
        manager.setSalt(Md5Utils.getSalt());
        //设置默认密码
        manager.setPassword(Md5Utils.encrypt(CoreConstant.DEFAULT_PASSWORD, manager.getSalt()));
        manager.setStatus(ManagerStatus.OK.getCode());
        manager.setAccount(manager.getPhone());
        manager.setCreatetime(new Date());
        manager.setCreateemp(curManager.getName());

        this.managerSrv.insertManager(manager);
        return SUCCESS_TIP;
    }


    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String name, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) String code) {
        List<Map<String, Object>> users = managerSrv.selectUsers(name, beginTime, endTime, code);
        for(Map manager: users){
            Integer status = (Integer) manager.get("status");
            if (status == ManagerStatus.OK.getCode()){
                manager.put("statusName", ManagerStatus.OK.getMessage());
            }else if(status == ManagerStatus.FREEZED.getCode()){
                manager.put("statusName", ManagerStatus.FREEZED.getMessage());
            }
        }
        return users;
    }


    @ResponseBody
    @RequestMapping("/listManagerPhone")
    public Object listManagerPhone(@RequestParam(required = false) String id, HttpServletRequest request){
        List<Map<String, Object>> phones = managerSrv.selectUserPhoneById(id);
        return phones;
    }


    /**
     * 修改管理员
     *
     * @throws NoPermissionException
     */
    @RequestMapping(value ="/edit", method = RequestMethod.POST)
    @ResponseBody
    public Tip edit(@Valid Manager manager, BindingResult result) {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        //判断是否修改手机号
        Manager tempManager = this.managerSrv.getByUserId(manager.getId());
        if(!tempManager.getPhone().equals(manager.getPhone())) {
            // 判断手机号是否重复
            Manager theManager = managerSrv.getByAccount(manager.getPhone());
            if (theManager != null) {
                throw new BussinessException(BizExceptionEnum.USER_ALREADY_REG);
            }
        }
        manager.setAccount(manager.getPhone());
        managerSrv.updateManager(manager);
        return SUCCESS_TIP;
    }

    /**
     * 删除管理员（逻辑删除）
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Tip delete(@RequestParam String userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        this.managerSrv.setStatus(userId, ManagerStatus.DELETED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 导入excel（逻辑删除）
     */
    @RequestMapping(method = RequestMethod.POST,value = "/importExcel")
    @ResponseBody
    public Tip importManagerExcel(MultipartFile file){
        Boolean flag = true;
        //解析excel
        if(!file.isEmpty()) {
            List<ManagerPoiEntity> personList = PoiUtils.importExcel(file, 0, 1, ManagerPoiEntity.class);
            for (ManagerPoiEntity managerPoi : personList) {
                Manager curManager = (Manager) getSession().getAttribute("manager");
                Manager theManager = managerSrv.getByAccount(managerPoi.getPhone());
                managerPoi.setCreateemp(curManager.getId());
                if (theManager != null || null == managerPoi.getPhone() || null == managerPoi.getName()) {
                    this.managerSrv.insertManagerErrorDataPoi(managerPoi);
                    flag = false;
                }else{
                    managerPoi.setId(UUID.randomUUID().toString());
                    // 完善账号信息
                    managerPoi.setSalt(Md5Utils.getSalt());
                    //设置默认密码
                    managerPoi.setPassword(Md5Utils.encrypt(CoreConstant.DEFAULT_PASSWORD, managerPoi.getSalt()));
                    managerPoi.setStatus(ManagerStatus.OK.getCode());
                    managerPoi.setAccount(managerPoi.getPhone());
                    managerPoi.setCreatetime(new Date());

                    this.managerSrv.insertManagerPoi(managerPoi);
                }
            }
            if(flag){
                return new SuccessTip();
            }
            return new ErrorTip(CoreConstant.MANAGER_IMPORT_HAS_ERROR_DATA_CODE, CoreConstant.MANAGER_IMPORT_HAS_ERROR_DATA_MSG);
        }else{
            return new ErrorTip(CoreConstant.MANAGER_IMPORT_FILE_NOT_EXIST_CODE, CoreConstant.MANAGER_IMPORT_FILE_NOT_EXIST_MSG);
        }
    }

    /**
     * 导出所有数据excel
     */
    @RequestMapping("/export")
    @ResponseBody
    public void export(HttpServletResponse response){
        //模拟从数据库获取需要导出的数据
        List<ManagerPoiEntity> personList = managerSrv.listManager();
        //导出
        PoiUtils.exportExcel(personList,"用户","用户",ManagerPoiEntity.class,"用户.xls",response);
    }


    /**
     * 导出异常数据excel
     */
    @RequestMapping("/exportErrorData")
    @ResponseBody
    public void exportErrorData(HttpServletResponse response){
        Manager curManager = (Manager) getSession().getAttribute("manager");
        //通过当前登录用户ID获取有该用户导入的异常信息
        List<ManagerPoiEntity> errorList = this.managerSrv.listErrorManagerByManagerId(curManager.getId());
        this.managerSrv.delErrorManagerByManagerId(curManager.getId());
        PoiUtils.exportExcel(errorList,"异常数据","异常数据",ManagerPoiEntity.class,"异常数据.xls",response);
    }

    /*
     * 导出模版
     */
    @RequestMapping("/excelModel")
    @ResponseBody
    public void excelModel(HttpServletResponse response){
        List<ManagerPoiEntity> list= new ArrayList<>();
        list.add(ManagerPoiEntity.newInstance("18888888888","张三","部长","1000","18888888888"));
        list.add(ManagerPoiEntity.newInstance("18666666666","李四","副部长","1001","18666666666"));
        PoiUtils.exportExcel(list,null,"用户模版",ManagerPoiEntity.class,"用户模版.xls",response);
    }

    /**
     * 冻结用户
     */
    @RequestMapping(value = "/freeze", method = RequestMethod.POST)
    @ResponseBody
    public Tip freeze(@RequestParam String userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        this.managerSrv.setStatus(userId, ManagerStatus.FREEZED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 解除冻结用户
     */
    @RequestMapping(value = "/unfreeze",  method = RequestMethod.POST)
    @ResponseBody
    public Tip unfreeze(@RequestParam String userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        this.managerSrv.setStatus(userId, ManagerStatus.OK.getCode());
        return SUCCESS_TIP;
    }



    /**
     * 分配角色
     */
    @RequestMapping(value = "/setRole", method = RequestMethod.POST)
    @ResponseBody
    public Tip setRole(@RequestParam("userId") String userId, @RequestParam("roleIds") String roleIds) {
//        if (ToolUtil.isOneEmpty(userId, roleIds)) {
//            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
//        }
        //不能修改超级管理员
//        if (userId.equals(Const.ADMIN_ID)) {
//            throw new BussinessException(BizExceptionEnum.CANT_CHANGE_ADMIN);
//        }
//        assertAuth(userId);

        return this.managerSrv.setRoles(userId, roleIds);
    }


    /**
     * 分配组织
     */
    @RequestMapping(value = "/setOrg", method = RequestMethod.POST)
    @ResponseBody
    public Tip setOrg(@RequestParam("userId") String userId, @RequestParam("orgIds") String orgIds) {
        if (ToolUtil.isOneEmpty(userId, orgIds)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        return  this.managerSrv.setOrg(userId, orgIds);
    }

    /**
     * 修改当前用户的密码
     */
    @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
    @ResponseBody
    public Object changePwd(@RequestParam String oldPwd, @RequestParam String newPwd, @RequestParam String rePwd) {
        if (!newPwd.equals(rePwd)) {
            throw new BussinessException(BizExceptionEnum.TWO_PWD_NOT_MATCH);
        }
        HttpSession session = getSession();
        Manager manager  = (Manager) session.getAttribute("manager");


        String oldMd5 = Md5Utils.encrypt(oldPwd,manager.getSalt());
        if (manager.getPassword().equals(oldMd5)) {

            String newSalt = Md5Utils.getSalt();
            String newMd5 = Md5Utils.encrypt(newPwd, newSalt);

            manager.setSalt(newSalt);
            manager.setPassword(newMd5);

            managerSrv.updateManagerPwd(manager);
            return SUCCESS_TIP;
        } else {
            throw new BussinessException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
        }
    }


    /**
     * 重置管理员的密码
     */
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    public Tip reset(@RequestParam String userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }

//        HttpSession session = getSession();
//        Manager manager  = (Manager) session.getAttribute("manager");
        Manager manager = new Manager();
        String newSalt = Md5Utils.getSalt();
        String newMd5 = Md5Utils.encrypt(Const.DEFAULT_PWD, newSalt);

        manager.setId(userId);
        manager.setSalt(newSalt);
        manager.setPassword(newMd5);

        managerSrv.updateManagerPwd(manager);
        return SUCCESS_TIP;
    }


    /**
     *
     * @Description: 将未注册云信的用户注册
     *
     *
     * @MethodName: createIm
     * @Parameters: []
     * @ReturnType: com.v4ward.core.tips.Tip
     *
     * @author huangxiang
     * @date 2018/3/16 15:53
     */
    @RequestMapping(value = "/createIm", method = RequestMethod.POST)
    @ResponseBody
    public Tip createIm(){
        List<Manager> users = managerSrv.selectUsersNoIm();
        UserAccount po = new UserAccount();
        for(Manager manager:users){
            po.setName(manager.getName());
            po.setToken(manager.getPassword());
            po.setAccid(manager.getId());
            String imName = "";
            try {
                imName = createUserAccount(po);
                manager.setImToken(manager.getPassword());
                manager.setImName(manager.getId());
                manager.setIsOpenIm(1);
                this.managerSrv.updateImByUserId(manager);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("文件存储失败",e);
                return new ErrorTip(CoreConstant.MANAGER_IM_ERROR_CODE,CoreConstant.MANAGER_IM_ERROR_MSG);
            }
        }
        return SUCCESS_TIP;
    }
}
