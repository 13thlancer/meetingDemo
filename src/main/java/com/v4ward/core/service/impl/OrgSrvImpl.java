package com.v4ward.core.service.impl;

import com.v4ward.core.common.CoreConstant;
import com.v4ward.core.dao.OrgMapper;
import com.v4ward.core.exception.BizExceptionEnum;
import com.v4ward.core.exception.BussinessException;
import com.v4ward.core.model.Manager;
import com.v4ward.core.model.Org;
import com.v4ward.core.model.ZTreeNode;
import com.v4ward.core.poiEntity.OrgPoiEntity;
import com.v4ward.core.poiEntity.OrgPoiExportEntity;
import com.v4ward.core.service.OrgSrv;
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
import static com.v4ward.core.common.CoreConstant.ORG_FULLNAMES_SEPARATE;

@Service
@Transactional
public class OrgSrvImpl implements OrgSrv {

    @Autowired
    private OrgMapper orgMapper;

    @Override
    public List<Map<String, Object>> listOrg(String orgName) {
        return this.orgMapper.listOrg(orgName);
    }

    @Override
    public String checkOrgByNum(String num) {
        return this.orgMapper.checkOrgByNum(num);
    }

    @Override
    public String getMaxCodeByPcode(String pCode) {
        return this.orgMapper.getMaxCodeByPcode(pCode);
    }

    @Override
    public Org getOrgByPCode(String pCode) {
        return this.orgMapper.getOrgByPCode(pCode);
    }

    @Override
    public Map<String, Object> selectOrgById(String id) {
        return this.orgMapper.selectOrgById(id);
    }

    @Override
    public void insertOrg(Org org) {
        //设置组织编码、父级菜单编码、组织全称
        orgSetPcode(org);
        this.orgMapper.insertOrg(org);
    }

    @Override
    public List<ZTreeNode> orgTreeList() {
        return this.orgMapper.orgTreeList();
    }

    @Override
    public Tip updateOrg(Org org) {
        //通过Id获取修改前的组织信息
        Org tempOrg = this.orgMapper.getPorg(org);
        //通过pcode获取修改后父级组织
        Org porg = this.orgMapper.getOrgByPCode(org.getPcode());
        //判断是否修改父级组织
        if(org.getPcode().equals(tempOrg.getPcode())){
            //判断是否为根节点
            if(null == porg){
                String searchName = tempOrg.getName()+".";
                String replaceName = org.getName()+".";
                String searchCode = tempOrg.getCode()+",";
                //修改组织编号和组织名称
                this.orgMapper.updateOrgById(org);
                this.orgMapper.updateOrgBySearchCode(org.getName(),searchCode,searchName,replaceName);
                return new SuccessTip();
            }

            String searchCode = tempOrg.getPcode()+","+tempOrg.getCode()+",";
            String searchName = porg.getName()+"."+tempOrg.getName()+".";
            String replaceName = porg.getName()+"."+org.getName()+".";
            //修改组织编号和组织名称
            this.orgMapper.updateOrgById(org);
            this.orgMapper.updateOrgBySearchCode(org.getName(),searchCode,searchName,replaceName);
        }else{
            //判断是否为根节点
            if(tempOrg.getPcode().equals("1")){
                return new ErrorTip(CoreConstant.ORG_IS_ROOT_CODE,CoreConstant.ORG_IS_ROOT_MSG);
            }
            //设置组织编码、父级菜单编码、组织全称
            orgSetPcode(org);
            //修改组织及其子组织
            Map<String,Object> codeMap = this.orgMapper.selectOrgById(org.getId());
            String code = codeMap.get("code").toString();

            //修改组织编号、组织名称、父级组织、组织编码、编码全称和组织全称
            this.orgMapper.updateOrg(org);
            updateChildOrg(code,org.getCode());
        }
        return new SuccessTip();
    }

    //删除组织
    @Override
    public Tip delOrgById(String orgId) {
        Map<String,Object> orgMap = this.orgMapper.selectOrgById(orgId);
        List<Map<String,Object>> childOrg = this.orgMapper.getChildOrgByCode(orgMap.get("code").toString());
        //判断组织是否有子节点
        if(childOrg.isEmpty()){
            List<Map<String,Object>> map = this.orgMapper.getOrgManagerById(orgId);
            //判断组织是否已分配人员
            if(map.size() == 0){
                List<Map<String,Object>> roleList = this.orgMapper.getRolesByOrgId(orgId);
                //判断组织是否已分配角色
                if(roleList.isEmpty()){
                    this.orgMapper.delRoleByOrgId(orgId);
                    this.orgMapper.delOrgById(orgId);
                    return new SuccessTip();
                }
                return new ErrorTip(CoreConstant.ORG_HAS_ROLE_CODE, CoreConstant.ORG_HAS_ROLE_MSG);
            }
            return new ErrorTip(CoreConstant.ORG_HAS_PERSON_CODE, CoreConstant.ORG_HAS_PERSON_MSG);
        }
        return new ErrorTip(CoreConstant.ORG_HAS_CHILD_CODE,CoreConstant.ORG_HAS_CHILD_MSG);
    }

    @Override
    public Tip importExcel(List<OrgPoiEntity> orgList) {
        List<Map<String, Object>> orgs = this.orgMapper.listOrg("");
        if(orgs.size()>0){
            return new ErrorTip(CoreConstant.ORG_IS_NOT_EMPTY_CODE, CoreConstant.ORG_IS_NOT_EMPTY_MSG);
        }
        //转树形结构
        List<OrgPoiEntity> orgTree = buildByRecursive(orgList);
        //重新生成code和pcode并存入数据库
        rebuildOrgList(orgTree,"0");
        return new SuccessTip();
    }

    @Override
    public List<ZTreeNode> orgTreeListByOrgCode(String userId) {
        List<String> orgids = this.orgMapper.getOrgCodeById(userId);
        if (orgids.isEmpty()) {
            List<ZTreeNode> roleTreeList = this.orgMapper.orgTreeList();
            return roleTreeList;
        } else {
            int size = orgids.size();
            String[] strArray =  (String[])orgids.toArray(new String[size]);
            List<ZTreeNode> roleTreeListByUserId = this.orgMapper.orgTreeListByOrgCode(strArray);
            return roleTreeListByUserId;
        }
    }

    @Override
    public List<OrgPoiExportEntity> exportOrg() {
        return this.orgMapper.listExportOrg();
    }

    @Override
    public Tip upSeq(String orgId) {
        Org org = this.orgMapper.getOrgByOrgId(orgId);
        if(org.getSeq() == 0){
            return new ErrorTip(CoreConstant.ORG_IS_FIRST_CODE, CoreConstant.ORG_IS_FIRST_MSG);
        }
        this.orgMapper.updateOrgSeqUp(org.getPcode(),org.getSeq());
        return new SuccessTip();
    }

    @Override
    public Tip downSeq(String orgId) {
        Org org = this.orgMapper.getOrgByOrgId(orgId);
        Integer maxSeq = this.orgMapper.getMaxSeqByPcode(org.getPcode());
        if(maxSeq == org.getSeq()){
            return new ErrorTip(CoreConstant.ORG_IS_LAST_CODE, CoreConstant.ORG_IS_LAST_MSG);
        }
        this.orgMapper.updateOrgSeqDown(org.getPcode(),org.getSeq());
        return new SuccessTip();
    }

    @Override
    public List<Map<String, Object>> listUserByOrg(String code) {
        Org org = this.orgMapper.getOrgByOrgCode(code);
        return this.orgMapper.listUserByOrg(org.getFullCodes());
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
    private void rebuildOrgList(List<OrgPoiEntity> orgTree,String pcode){
        Org org = new Org();
        for (OrgPoiEntity child:orgTree){
            if(null != child.getChildren()){
                org.setName(child.getName());
                org.setPcode(pcode);
                org.setNum(child.getCode());
                org.setPnum(child.getPcode());
                orgSetPcode(org);
                this.orgMapper.insertOrg(org);
                rebuildOrgList(child.getChildren(),org.getCode());
            }else{
                org.setName(child.getName());
                org.setPcode(pcode);
                org.setNum(child.getCode());
                org.setPnum(child.getPcode());
                orgSetPcode(org);
                this.orgMapper.insertOrg(org);
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
        List<Org> orglist = this.orgMapper.listChildOrgByPcode(pcode);
        while(!orglist.isEmpty()){
            for (Org org:orglist){
                String orgCode = org.getCode();
                //修改父级编码、组织编码、编码全称以及组织全称
                org.setPcode(code);
                orgSetPcode(org);
                this.orgMapper.updateOrg(org);
                return updateChildOrg(orgCode,org.getCode());
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
    private void orgSetPcode(@Valid Org org) {
        if (ToolUtil.isEmpty(org.getPcode()) || org.getPcode().equals("0")) {
            org.setPcode("1");
            org.setPnum("1");
            org.setSeq(1);
            //设置组织编码和序号
//            org.setCode(orgSetCode("1"));
            orgSetCodeAndSeq(org,"1");
            //设置组织全部编码
            org.setFullCodes("1" + ORG_FULLCODES_SEPARATE + org.getCode() + ORG_FULLCODES_SEPARATE);
            //set组织全称
            org.setFullNames("" + org.getName() + ORG_FULLNAMES_SEPARATE);
        } else {
            String pCode = org.getPcode();
            //获取父级组织
            Org pOrg = this.orgMapper.getOrgByPCode(pCode);
            org.setPcode(pOrg.getCode());
            org.setPnum(pOrg.getNum());
            //设置组织编码和序号
//            org.setCode(orgSetCode(pCode));
            orgSetCodeAndSeq(org,pCode);
            //如果编号和父编号一致会导致无限递归
            if (org.getCode().equals(org.getPcode())) {
                throw new BussinessException(BizExceptionEnum.MENU_PCODE_COINCIDENCE);
            }
            org.setFullCodes(pOrg.getFullCodes() + org.getCode() + ORG_FULLCODES_SEPARATE);
            org.setFullNames(pOrg.getFullNames() + org.getName() + ORG_FULLNAMES_SEPARATE);
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
    private void orgSetCodeAndSeq(Org org, String pCode){
        String MaxCode = this.orgMapper.getMaxCodeByPcode(pCode);
        if (ToolUtil.isEmpty(MaxCode)){
            org.setCode(pCode+"1000");
            org.setSeq(0);
        }else{
            Integer seq = this.orgMapper.getMaxSeqByPcode(pCode);
            Integer i =  Integer.valueOf(MaxCode.substring(MaxCode.length()-4));
            org.setCode(MaxCode.substring(0,MaxCode.length()-4)+Integer.toString(i+1));
            org.setSeq(seq+1);
        }
    }


    /**
     * 根据请求的父级菜单编号设置组织code
     */
    private String orgSetCode(String pCode) {
        String MaxCode = this.orgMapper.getMaxCodeByPcode(pCode);
        if (ToolUtil.isEmpty(MaxCode)){
            return pCode+"1000";
        }
        Integer i =  Integer.valueOf(MaxCode.substring(MaxCode.length()-4));
        return MaxCode.substring(0,MaxCode.length()-4)+Integer.toString(i+1);
    }

    /**
     * 使用递归方法建树
     * @param orglist
     * @return
     */
    private static List<OrgPoiEntity> buildByRecursive(List<OrgPoiEntity> orglist) {
        List<OrgPoiEntity> trees = new ArrayList<OrgPoiEntity>();
        for (OrgPoiEntity orgPoiEntity : orglist) {
            if (orgPoiEntity.getPcode() == null) {
                trees.add(findChildren(orgPoiEntity,orglist));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     * @param orglist
     * @return
     */
    private static OrgPoiEntity findChildren(OrgPoiEntity orgPoiEntity,List<OrgPoiEntity> orglist) {
        for (OrgPoiEntity org : orglist) {
            if(orgPoiEntity.getCode().equals(org.getPcode())) {
                if (orgPoiEntity.getChildren() == null) {
                    orgPoiEntity.setChildren(new ArrayList<OrgPoiEntity>());
                }
                orgPoiEntity.getChildren().add(findChildren(org,orglist));
            }
        }
        return orgPoiEntity;
    }
}
