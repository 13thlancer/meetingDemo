/**
 * 角色详情对话框（可用于添加和修改对话框）
 */
var RolInfoDlg = {
    roleInfoData: {},
    pOrgZtree: null,
    pNameZtree: null,
    validateFields: {
        name: {
            validators: {
                notEmpty: {
                    message: '角色名不能为空'
                }
            }
        },
        pOrgName: {
            validators: {
                notEmpty: {
                    message: '组织不能为空'
                }
            }
        },
        pName: {
            validators: {
                notEmpty: {
                    message: '角色类型不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
RolInfoDlg.clearData = function () {
    this.roleInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
RolInfoDlg.set = function (key, val) {
    this.roleInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
RolInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
RolInfoDlg.close = function () {
    parent.layer.close(window.parent.Role.layerIndex);
};


/**
 * 点击父级菜单input框时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
RolInfoDlg.onClickPName = function (e, treeId, treeNode) {
    $("#pName").attr("value", RolInfoDlg.pNameZtree.getSelectedVal());
    $("#pnum").attr("value", treeNode.id);

    if( $("#pnum").val()==1){
        $("#orgId").show();
    }else{
        $("#orgId").hide();
    }
};

RolInfoDlg.onClickpOrgName = function (e, treeId, treeNode) {
    $("#pOrgName").attr("value", RolInfoDlg.pOrgZtree.getSelectedVal());
    $("#orgCode").attr("value", treeNode.id);
};


/**
 * 显示角色菜单的树
 *
 * @returns
 */
RolInfoDlg.showPNameSelectTree = function () {
    Feng.showInputTree("pName", "pNameContent");
};


/**
 * 显示组织菜单的树
 *
 * @returns
 */
RolInfoDlg.showPOrgNameSelectTree = function () {
    Feng.showInputTree("pOrgName", "pOrgNameContent");
};

/**
 * 收集数据
 */
RolInfoDlg.collectData = function () {
    this.set('id').set('name').set('pnum').set('num').set('orgCode');
};

/**
 * 验证数据是否为空
 */
RolInfoDlg.validate = function () {
    $('#roleInfoForm').data("bootstrapValidator").resetForm();
    $('#roleInfoForm').bootstrapValidator('validate');
    return $("#roleInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加用户
 */
RolInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/role/add", function (data) {
        if(data.code == 403){
            Feng.error("添加失败! " + data.message + "!");
        }else{
            Feng.success("添加成功!");
        }
        window.parent.Role.table.refresh();
        RolInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.roleInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
RolInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/role/edit", function (data) {
        if(data.code == 403){
            Feng.error("修改失败! " + data.message + "!");
        }else{
            Feng.success("修改成功!");
        }
        window.parent.Role.table.refresh();
        RolInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.roleInfoData);
    ajax.start();
};

$(function () {

    if( $("#pnum").val()==1){
        $("#orgId").show();
    }else{
        $("#orgId").hide();
    }

    Feng.initValidator("roleInfoForm", RolInfoDlg.validateFields);

    var porgNameTree = new $ZTree("pOrgNameTree", "/org/selectOrgTreeList");
    porgNameTree.bindOnClick(RolInfoDlg.onClickpOrgName);
    porgNameTree.init();
    RolInfoDlg.pOrgZtree = porgNameTree;

    var pNameTree = new $ZTree("pNameTree", "/role/pRoleTreeList");
    pNameTree.bindOnClick(RolInfoDlg.onClickPName);
    pNameTree.init();
    RolInfoDlg.pNameZtree = pNameTree;
});
