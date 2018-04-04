/**
 * 议程管理初始化
 */
var submeeting = {
    id: "submeetingTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

var partmeetingId ;

$(function() {
	
	partmeetingId = $("#partmeetingId").val();
	
    var defaultColunms = submeeting.initColumn();
    var table = new BSTable(submeeting.id, "/submeeting/list", defaultColunms);
    table.setPaginationType("server");
    table.setQueryParams(submeeting.queryParams());
    submeeting.table = table.init();
});

//查询参数
submeeting.queryParams = function(){
	var queryData = {};
	queryData['partmeetingId'] = partmeetingId;
	queryData['submeetingTheme'] = $("#submeetingTheme").val();
	return queryData;
}

/**
 * 查询议程列表
 */
submeeting.search = function () {
    submeeting.table.refresh({query: submeeting.queryParams()});
};

submeeting.reset = function () {
	$("#submeetingTheme").val("");
};

/**
 * 初始化表格的列
 */
submeeting.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '议程主题', field: 'submeetingTheme', align: 'center', valign: 'middle', sortable: true},
        {title: '议程开始时间', field: 'starttime', align: 'center', valign: 'middle', sortable: true},
        {title: '议程结束时间', field: 'endtime', align: 'center', valign: 'middle', sortable: true},
        {title: '主持人', field: 'submeetingHost', align: 'center', valign: 'middle', sortable: true},
        {title: '议程地点', field: 'submeetingLocation', align: 'center', valign: 'middle', sortable: true},
        {title: '议程状态', field: 'submeetingStatus', align: 'center', valign: 'middle', sortable: true,
        	formatter:function(value, row, index){
        		return 0==value?'显示':1==value?'不显示':'-';
        	}
        },
        {title: '排序号', field: 'orderCode', align: 'center', valign: 'middle', sortable: false}
      ]
};

/**
 * 检查是否选中
 */
submeeting.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        submeeting.seItem = selected[0];
        return true;
    }
};

/**
 * 议程信息录入录入
 */
submeeting.openAddSubmeeting = function () {
	
//	layer提供了5种层类型。可传入的值有：0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
    var index = layer.open({
        type: 2,
        title: '添加议程信息',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/submeeting/toadd?partmeetingId='+partmeetingId
    });
    this.layerIndex = index;
};

/**
 * 查看
 */
submeeting.view = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '议程查看',
            area: ['800px', '350px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/submeeting/toview/' + submeeting.seItem.id
        });
        this.layerIndex = index;
    }
};
/**
 * 修改
 */
submeeting.modify = function () {
	if (this.check()) {
		var index = layer.open({
			type: 2,
			title: '议程修改',
			area: ['800px', '420px'], //宽高
			fix: false, //不固定
			maxmin: true,
			content: Feng.ctxPath + '/submeeting/tomodify/' + submeeting.seItem.id
		});
		this.layerIndex = index;
	}
};

/**
 * 议程删除
 */
submeeting.del = function () {
    if (this.check()) {

        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/submeeting/delete", function (data) {
                Feng.success("删除成功!");
                submeeting.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("submeetingId", submeeting.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否刪除选中的议程 ?", operation);
    }
};
	

