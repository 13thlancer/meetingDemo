/**
 * 组织管理的单例
 */
var Org = {
    id: "orgTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Org.initColumn = function () {
   var columns = [
        {field: 'selectItem', radio: true},
        {title: '组织编码', field: 'num', align: 'center',valign: 'middle',  sortable: true},
        {title: '组织名称', field: 'name', align: 'center', valign: 'middle',  sortable: true},
        {title: '组织全称', field: 'fullNames', align: 'center', valign: 'middle',  sortable: true}]
   /* var columns = [
        {field: 'selectItem', radio: true},
        {title: '菜单编号', field: 'num', align: 'center',visible:'false', valign: 'middle'},
        {title: '菜单名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '17%'},
        {title: '菜单父编号', field: 'pcode', align: 'center', valign: 'middle', sortable: true},
        {title: '请求地址', field: 'url', align: 'center', valign: 'middle', sortable: true, width: '15%'},
        {title: '层级', field: 'levels', align: 'center', valign: 'middle', sortable: true},
        {title: '是否是菜单', field: 'isMenuName', align: 'center', valign: 'middle', sortable: true}]*/
    return columns;
};


/**
 * 检查是否选中
 */
Org.check = function () {
    var selected = $('#' + this.id).bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Org.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加菜单
 */
Org.openAddOrg = function () {
    var index = layer.open({
        type: 2,
        title: '添加菜单',
        area: ['830px', '450px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/org/org_add'
    });
    this.layerIndex = index;
};

/**
 * 点击修改
 */
Org.openChangeOrg = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '修改菜单',
            area: ['800px', '450px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/org/org_edit/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
Org.delOrg = function () {
    if (this.check()) {

        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/org/remove", function (data) {
                if(data.code != 200){
                    Feng.error("删除失败! " + data.message + "!");
                }else{
                    Feng.success("删除成功!");
                }
                Org.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("orgId", Org.seItem.id);
            ajax.start();
        };

        Feng.confirm("若删除该组织，与该组织绑定的角色也将被删除，确定刪除?", operation);
    }
};

/**
 * 搜索
 */
Org.search = function () {
    var queryData = {};

    queryData['orgName'] = $("#orgName").val();

    Org.table.refresh({query: queryData});
}

/**
 * 点击选择文件调用隐藏的input
 */
Org.selectFile = function(){
    $("#selectFile").click();
}

var file = $('#selectFile');
file.on('change', function(e) {
    // e.currentTarget.files 是一个数组，如果支持多个文件，则需要遍历
    var filename = e.currentTarget.files[0].name;
    $("#file").text(filename.length > 15 ? filename.substr(0, 15)+'...' : filename);
});

/**
 * 上移组织
 */
Org.upSeq = function () {
    if (this.check()) {
        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/org/upSeq", function (data) {
            if (data.code == 200) {
                Feng.success("操作成功!");
                Org.table.refresh();
            } else {
                Feng.error("操作失败! " + data.message + "!");
            }
        }, function (data) {
            Feng.error("操作失败!" + data.responseJSON.message + "!");
        });
        ajax.set("orgId", Org.seItem.id);
        ajax.start();
    }
}

/**
 * 下移组织
 */
Org.downSeq = function () {
    if (this.check()) {
        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/org/downSeq", function (data) {
            if (data.code == 200) {
                Feng.success("操作成功!");
                Org.table.refresh();
            } else {
                Feng.error("操作失败! " + data.message + "!");
            }
        }, function (data) {
            Feng.error("操作失败!" + data.responseJSON.message + "!");
        });
        ajax.set("orgId", Org.seItem.id);
        ajax.start();
    }
}

/**
  * 导出
  */
Org.export = function () {
    window.location.href=Feng.ctxPath + "/org/export"
};



Org.excelModel = function(){
    window.location.href=Feng.ctxPath + "/org/excelModel"
}


/**
 * 导入
 */
Org.importExcel = function () {
    var data = new FormData($('#excel')[0]);
    $.ajax({
        url: Feng.ctxPath + "/org/importExcel",
        type: "POST",
        async: false,
        data: data,
        processData: false,
        contentType: false,
        success: function(data){
            if(data.code != 200){
                Feng.error("导入失败! " + data.message + "!");
            }else{
                Feng.success("导入成功!");
                Org.table.refresh();
            }
        }
    });
};


$(function () {
    var defaultColunms = Org.initColumn();
    var table = new BSTreeTable(Org.id, "/org/list", defaultColunms);
    table.setRootCodeValue("1");
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("code");
    table.setParentCodeField("pcode");
    table.setExpandAll(true);

    table.init();
    Org.table = table;
});
