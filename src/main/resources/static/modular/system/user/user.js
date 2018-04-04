/**
 * 系统管理--用户管理的单例对象
 */
var MgrUser = {
    id: "managerTable",//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    deptid:0
};

/**
 * 初始化表格的列
 */
MgrUser.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '账号', field: 'account', align: 'center', valign: 'middle', sortable: true},
        {title: '姓名', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '角色', field: 'roleName', align: 'center', valign: 'middle', sortable: true},
        {title: '电话', field: 'phone', align: 'center', valign: 'middle', sortable: true},
        {title: '职务', field: 'position', align: 'center', valign: 'middle', sortable: true},
        {title: '组织全称', field: 'orgName', align: 'center', valign: 'middle', sortable: true},
        {title: '创建时间', field: 'createtime', align: 'center', valign: 'middle', sortable: true},
        {title: '状态', field: 'statusName', align: 'center', valign: 'middle', sortable: true}];
    return columns;
};

/**
 * 检查是否选中
 */
MgrUser.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        MgrUser.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加管理员
 */
MgrUser.openAddMgr = function () {
    var index = layer.open({
        type: 2,
        title: '添加管理员',
        area: ['800px', '450px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/mgr/user_add'
    });
    this.layerIndex = index;
};


/**
 * 点击选择文件调用隐藏的input
 */
MgrUser.selectFile = function(){
    $("#selectFile").click();
}

var file = $('#selectFile');
file.on('change', function(e) {
    // e.currentTarget.files 是一个数组，如果支持多个文件，则需要遍历
    var filename = e.currentTarget.files[0].name;
    $("#file").text(filename.length > 15 ? filename.substr(0, 15)+'...' : filename);
});


/**
 * 点击修改按钮时
 * @param userId 管理员id
 */
MgrUser.openChangeUser = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '编辑管理员',
            area: ['800px', '450px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/mgr/user_edit/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 点击查看手机号按钮时
 * @param userId 管理员id
 */
MgrUser.listMobile = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '手机号历史记录',
            area: ['1000px', '450px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/mgr/user_phone/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};


MgrUser.initIm = function(){
    var ajax = new $ax(Feng.ctxPath + "/mgr/createIm", function (data) {
        if(data.code == 200){
            Feng.success("初始化成功!");
        }else{
            Feng.error("初始化失败! " + data.message + "!");
        }
        MgrUser.table.refresh();
    }, function (data) {
        Feng.error("初始化失败!" + data.responseJSON.message + "!");
    });
    ajax.start();
}

/**
 * 点击角色分配
 * @param
 */
MgrUser.roleAssign = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '角色分配',
            area: ['300px', '400px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/mgr/role_assign/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};


/**
 * 点击组织分配
 *
 */
MgrUser.orgAssign = function (){
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '组织分配',
            area: ['300px', '400px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/mgr/org_assign/' + this.seItem.id
        });
        this.layerIndex = index;
    }
}

/**
 * 删除用户
 */
MgrUser.delMgrUser = function () {
    if (this.check()) {

        var operation = function(){
            var userId = MgrUser.seItem.id;
            var ajax = new $ax(Feng.ctxPath + "/mgr/delete", function (data) {
                if(data.code == 200){
                    Feng.success("删除成功!");
                    MgrUser.table.refresh();
                }else{
                    Feng.error("删除失败! " + data.message + "!");
                }
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("userId", userId);
            ajax.start();
        };

        Feng.confirm("是否删除用户" + MgrUser.seItem.account + "?",operation);
    }
};

/**
 * 冻结用户账户
 * @param userId
 */
MgrUser.freezeAccount = function () {
    if (this.check()) {
        var userId = this.seItem.id;
        var ajax = new $ax(Feng.ctxPath + "/mgr/freeze", function (data) {
            if(data.code == 403){
                Feng.error("冻结失败! " + data.message + "!");
            }else{
                Feng.success("冻结成功!");
            }
            MgrUser.table.refresh();
        }, function (data) {
            Feng.error("冻结失败!" + data.responseJSON.message + "!");
        });
        ajax.set("userId", userId);
        ajax.start();
    }
};

/**
 * 解除冻结用户账户
 * @param userId
 */
MgrUser.unfreeze = function () {
    if (this.check()) {
        var userId = this.seItem.id;
        var ajax = new $ax(Feng.ctxPath + "/mgr/unfreeze", function (data) {
            if(data.code == 403){
                Feng.error("解除冻结失败! " + data.message + "!");
            }else{
                Feng.success("解除冻结成功!");
            }
            MgrUser.table.refresh();
        }, function (data) {
            Feng.error("解除冻结失败!");
        });
        ajax.set("userId", userId);
        ajax.start();
    }
}

/**
 * 重置密码
 */
MgrUser.resetPwd = function () {
    if (this.check()) {
        var userId = this.seItem.id;
        parent.layer.confirm('是否重置密码为111111？', {
            btn: ['确定', '取消'],
            shade: false //不显示遮罩
        }, function () {
            var ajax = new $ax(Feng.ctxPath + "/mgr/reset", function (data){
            if(data.code == 200){
                Feng.success("重置密码成功!");
            }else{
                Feng.error("重置密码失败! " + data.message + "!");
            }
            }, function (data) {
                Feng.error("重置密码失败!");
            });
            ajax.set("userId", userId);
            ajax.start();
        });
    }
};

MgrUser.resetSearch = function () {
    $("#name").val("");
    $("#beginTime").val("");
    $("#endTime").val("");

    MgrUser.search();
}

MgrUser.search = function () {
    var queryData = {};

    queryData['deptid'] = MgrUser.deptid;
    queryData['name'] = $("#name").val();
    queryData['beginTime'] = $("#beginTime").val();
    queryData['endTime'] = $("#endTime").val();

    MgrUser.table.refresh({query: queryData});
}

/**
  * 导出
  */
MgrUser.export = function () {
    window.location.href=Feng.ctxPath + "/mgr/export"
};


MgrUser.excelModel = function(){
    window.location.href=Feng.ctxPath + "/mgr/excelModel"
}



/**
 * 导入
 */
MgrUser.importExcel = function () {
    var data = new FormData($('#excel')[0]);
    $.ajax({
        url: Feng.ctxPath + "/mgr/importExcel",
        type: "POST",
        async: false,
        data: data,
        processData: false,
        contentType: false,
        success: function(data){
            if(data.code == 200){
                Feng.success("导入成功!");
                MgrUser.table.refresh();
            }else if(data.code == 2002){
                Feng.error("导入异常! " + data.message + "!");
            }else {
                Feng.error("导入异常! " + data.message + "!");
                MgrUser.table.refresh();
                window.location.href=Feng.ctxPath + "/mgr/exportErrorData"
            }
        }
    });
};

$(function () {
    var defaultColunms = MgrUser.initColumn();
    var table = new BSTable("managerTable", "/mgr/list", defaultColunms);
    table.setPaginationType("client");
    MgrUser.table = table.init();
});

laydate.render({
    elem : '#beginTime' //指定元素
    ,type: 'date'
});

laydate.render({
    elem : '#endTime' //指定元素
    ,type: 'date'
});