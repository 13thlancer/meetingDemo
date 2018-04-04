package com.v4ward.core.service;

import com.v4ward.core.model.Org;
import com.v4ward.core.model.ZTreeNode;
import com.v4ward.core.poiEntity.OrgPoiEntity;
import com.v4ward.core.poiEntity.OrgPoiExportEntity;
import com.v4ward.core.tips.Tip;

import java.util.List;
import java.util.Map;

public interface OrgSrv {

    List<Map<String,Object>> listOrg(String orgName);

    String checkOrgByNum(String num);

    String getMaxCodeByPcode(String pCode);

    Org getOrgByPCode(String pCode);

    Map<String,Object> selectOrgById(String id);

    void insertOrg(Org org);

    List<ZTreeNode> orgTreeList();

    Tip updateOrg(Org org);

    Tip delOrgById(String orgId);

    Tip importExcel(List<OrgPoiEntity> orgList);

    List<ZTreeNode> orgTreeListByOrgCode(String userId);

    List<OrgPoiExportEntity> exportOrg();

    Tip upSeq(String orgId);

    Tip downSeq(String orgId);

    List<Map<String,Object>> listUserByOrg(String code);
}
