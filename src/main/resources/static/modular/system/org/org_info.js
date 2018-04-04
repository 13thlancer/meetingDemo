/**
 * 菜单详情对话框
 */
var OrgInfoDlg = {
    orgInfoData: {},
    ztreeInstance: null,
    validateFields: {
        name: {
            validators: {
                notEmpty: {
                    message: '组织名称不能为空'
                }
            }
        },
        code: {
            validators: {
                notEmpty: {
                    message: '组织编号不能为空'
                }
            }
        },
        pcodeName: {
            validators: {
                notEmpty: {
                    message: '父级组织不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
OrgInfoDlg.clearData = function () {
    this.orgInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
OrgInfoDlg.set = function (key, val) {
    this.orgInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
OrgInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
OrgInfoDlg.close = function () {
    parent.layer.close(window.parent.Org.layerIndex);
}

/**
 * 收集数据
 */
OrgInfoDlg.collectData = function () {
    this.set('id').set('name').set('pcode').set('num');
}

/**
 * 验证数据是否为空
 */
OrgInfoDlg.validate = function () {
    $('#orgInfoForm').data("bootstrapValidator").resetForm();
    $('#orgInfoForm').bootstrapValidator('validate');
    return $("#orgInfoForm").data('bootstrapValidator').isValid();
}

/**
 * 提交添加用户
 */
OrgInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/org/add", function (data) {
        if(data.code == 403){
            Feng.error("添加失败! " + data.message + "!");
        }else{
            Feng.success("添加成功!");
        }
        window.parent.Org.table.refresh();
        OrgInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.orgInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
OrgInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/org/edit", function (data) {
        if(data.code == 200){
            Feng.success("修改成功!");
        }else{
            Feng.error("修改失败! " + data.message + "!");
        }
        window.parent.Org.table.refresh();
        OrgInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.orgInfoData);
    ajax.start();
}

/**
 * 点击父级编号input框时
 */
OrgInfoDlg.onClickDept = function (e, treeId, treeNode) {
    $("#pcodeName").attr("value", OrgInfoDlg.ztreeInstance.getSelectedVal());
    $("#pcode").attr("value", treeNode.id);
};


/**
 * 显示父级菜单选择的树
 */
OrgInfoDlg.showOrgSelectTree = function () {
    Feng.showInputTree("pcodeName", "pcodeTreeDiv", 15, 34);
};

$(function () {
    Feng.initValidator("orgInfoForm", OrgInfoDlg.validateFields);

    var ztree = new $ZTree("pcodeTree", "/org/selectOrgTreeList");
    ztree.bindOnClick(OrgInfoDlg.onClickDept);
    ztree.init();
    OrgInfoDlg.ztreeInstance = ztree;

});
