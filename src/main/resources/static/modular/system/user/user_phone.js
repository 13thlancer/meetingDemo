/**
 * 系统管理--用户管理的单例对象
 */
var MgrPhone = {
    id: "phoneTable",//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MgrPhone.initColumn = function () {
    var columns = [
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '姓名', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '电话', field: 'phone', align: 'center', valign: 'middle', sortable: true},
        {title: '起始时间', field: 'startTime', align: 'center', valign: 'middle', sortable: true},
        {title: '结束时间', field: 'endTime', align: 'center', valign: 'middle', sortable: true}];
    return columns;
};

MgrPhone.formParams = function() {
    var queryData = {};
    queryData['id'] = $("#userId").val();
    return queryData;
}

$(function () {
    var defaultColunms = MgrPhone.initColumn();
    var phoneTable = new BSTable("phoneTable", "/mgr/listManagerPhone", defaultColunms);
    phoneTable.setQueryParams(MgrPhone.formParams());
    phoneTable.setPaginationType("client");
    MgrPhone.table = phoneTable.init();
});
