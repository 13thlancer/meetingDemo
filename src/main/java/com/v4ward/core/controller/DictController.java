package com.v4ward.core.controller;


import com.v4ward.core.annotation.BussinessLog;
import com.v4ward.core.dict.DictMap;
import com.v4ward.core.exception.BizExceptionEnum;
import com.v4ward.core.exception.BussinessException;
import com.v4ward.core.factory.ConstantFactory;
import com.v4ward.core.log.LogObjectHolder;
import com.v4ward.core.model.Dict;
import com.v4ward.core.utils.ToolUtil;
import com.v4ward.core.warpper.DictWarpper;
import com.v4ward.core.service.DictSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 字典控制器
 *
 * @author fengshuonan
 * @Date 2017年4月26日 12:55:31
 */
@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {

    private String PREFIX = "/system/dict/";

    @Autowired
    private DictSrv dictSrv;

    /**
     * 跳转到字典管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "dict";
    }

    /**
     * 跳转到添加字典
     */
    @RequestMapping("/dict_add")
    public String deptAdd() {
        return PREFIX + "dict_add";
    }

    /**
     * 跳转到修改字典
     */
    @RequestMapping("/dict_edit/{dictId}")
    public String deptUpdate(@PathVariable String dictId, Model model) {
        Dict dict = dictSrv.selectById(dictId);
        model.addAttribute("dict", dict);
        List<Dict> subDicts = dictSrv.selectListByPid(dictId);
        model.addAttribute("subDicts", subDicts);
        LogObjectHolder.me().set(dict);
        return PREFIX + "dict_edit";
    }

    /**
     * 新增字典
     *
     * @param dictValues 格式例如   "1:启用;2:禁用;3:冻结"
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(String dictName, String dictValues) {
        if (ToolUtil.isOneEmpty(dictName, dictValues)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        this.dictSrv.addDict(dictName, dictValues);
        return SUCCESS_TIP;
    }

    /**
     * 获取所有字典列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> list = this.dictSrv.list(condition);
        return super.warpObject(new DictWarpper(list));
    }

    /**
     * 字典详情
     */
    @RequestMapping(value = "/detail/{dictId}")
    @ResponseBody
    public Object detail(@PathVariable("dictId") String dictId) {
        return dictSrv.selectById(dictId);
    }

    /**
     * 修改字典
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(String dictId, String dictName, String dictValues) {
        if (ToolUtil.isOneEmpty(dictId, dictName, dictValues)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        dictSrv.editDict(dictId, dictName, dictValues);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除字典记录
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String dictId) {

        //缓存被删除的名称
        LogObjectHolder.me().set(ConstantFactory.me().getDictName(dictId));

        this.dictSrv.delteDict(dictId);
        return SUCCESS_TIP;
    }

}
