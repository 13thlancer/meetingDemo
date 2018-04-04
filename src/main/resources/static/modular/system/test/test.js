/**
 * 系统管理--用户管理的单例对象
 */
var Test = {
    id: "managerTable",//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    deptid:0
};

/**
 * 初始化表格的列
 */
Test.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '账号', field: 'account', align: 'center', valign: 'middle', sortable: true},
        {title: '姓名', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '角色', field: 'roleName', align: 'center', valign: 'middle', sortable: true},
        {title: '电话', field: 'phone', align: 'center', valign: 'middle', sortable: true},
        {title: '创建时间', field: 'createtime', align: 'center', valign: 'middle', sortable: true},
        {title: '状态', field: 'statusName', align: 'center', valign: 'middle', sortable: true}];
    return columns;
};


Test.resetSearch = function () {
    $("#name").val("");
    $("#beginTime").val("");
    $("#endTime").val("");

    MgrUser.search();
}

/**
 * 发送邮件
 *
 */
Test.sendMail = function () {
    var title = $("#title").val();
    var content = $("#content").val();
    var ajax = new $ax(Feng.ctxPath + "/test/sendMail", function (data) {
        alert(data);
        if(data.code == 403){
            Feng.error("发送失败! " + data.message + "!");
        }else{
            Feng.success("发送成功!");
        }
    }, function (data) {
        alert(data.responseJSON);
        Feng.error("发送失败!" + data.responseJSON.message + "!");
    });
    ajax.set("title", title);
    ajax.set("content", content);
    ajax.start();

}

/**
 * 二维码
 *
 */
Test.qrCode = function () {
    var data = $("#qrcode").val();

    var index = layer.open({
        type: 2,
        title: '角色分配',
        area: ['300px', '400px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/test/qrCode?data=' + data
    });
    this.layerIndex = index;
}
/**
 * 导出
 */
Test.export = function () {
    /*    $.post("/mgr/export",function(data,status){
        });*/
    window.location.href=Feng.ctxPath + "/test/export"
};

/**
 * 导入
 */
Test.importExcel = function () {
    var data = new FormData($('#excel')[0]);
    $.ajax({
        url: Feng.ctxPath + "/test/importExcel",
        type: "POST",
        async: false,
        data: data,
        processData: false,
        contentType: false,
        success: function(data){
            alert(data);
        }
    });
};

$(function () {
    var defaultColunms = Test.initColumn();
    var table = new BSTable("managerTable", "/mgr/list", defaultColunms);
    table.setPaginationType("client");
    Test.table = table.init();
});
