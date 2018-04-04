/**
 * 会议管理初始化
 */
var meeting = {
    id: "meetingTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

var roleType ;

$(function() {
	
	roleType = $("#roleType").val();
	
    var defaultColunms = meeting.initColumn();
    var table = new BSTable(meeting.id, "/meeting/list", defaultColunms);
    table.setPaginationType("server");
    table.setQueryParams(meeting.queryParams());
    meeting.table = table.init();
});

meeting.queryParams = function(){
	var queryData = {};
	queryData['meetingTheme'] = $("#meetingTheme").val();
	return queryData;
}

/**
 * 查询会议列表
 */
meeting.search = function () {
    meeting.table.refresh({query: meeting.queryParams()});
};

meeting.reset = function () {
	$("#meetingTheme").val("");
};

/**
 * 初始化表格的列
 */
meeting.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '会议所属部门', field: 'orgName', align: 'center', valign: 'middle', sortable: true},
        {title: '会议主题', field: 'meetingTheme', align: 'center', valign: 'middle', sortable: true},
        {title: '会议简介', field: 'meetingBrief', align: 'center', valign: 'middle', sortable: true},
/*        {title: '会议简介', field: 'meetingNotice', align: 'center', valign: 'middle', sortable: true},
*/        {title: '会议状态', field: 'meetingStatus', align: 'center', valign: 'middle', sortable: true,width:'10%',
        	formatter:function(value, row, index){
        		return 0==value?'创建':1==value?'完成':'-';
        	}
        }];
};

/**
 * 检查是否选中
 */
meeting.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        meeting.seItem = selected[0];
        return true;
    }
};

/**
 * 会议录入
 */
meeting.openAddmeeting = function () {
	if(roleType.indexOf("1")<0){
		Feng.error("您无权限执行此操作!");
		return;
	}
//	layer提供了5种层类型。可传入的值有：0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
    var index = layer.open({
        type: 2,
        title: '添加会议信息',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/meeting/toadd'
    });
    this.layerIndex = index;
};

/**
 * 查看
 */
meeting.view = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '会议查看',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/meeting/toview/' + meeting.seItem.id
        });
        this.layerIndex = index;
    }
};
/**
 * 修改
 */
meeting.modify = function () {
	if (this.check()) {
		var index = layer.open({
			type: 2,
			title: '会议修改',
			area: ['800px', '420px'], //宽高
			fix: false, //不固定
			maxmin: true,
			content: Feng.ctxPath + '/meeting/tomodify/' + meeting.seItem.id
		});
		this.layerIndex = index;
	}
};

/**
 * 会议删除
 */
meeting.del = function () {
    if (this.check()) {

        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/meeting/delete", function (data) {
                Feng.success("删除成功!"); 
                meeting.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("meetingId", meeting.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否刪除会议 ?", operation);
    }
};

/**
 * 跳转分会场管理
 */
meeting.openPartmeeting = function() {
	if (this.check()) {
		window.location.href = '/partmeeting?meetingId=' + meeting.seItem.id
				+ '&meetingTheme=' + meeting.seItem.meetingTheme;

	}
}

/**
 * 跳转分配会务管理员
 */
meeting.openAllotManager = function() {
	
	if(roleType.indexOf("1")<0){
		Feng.error("您无权限执行此操作!");
		return;
	}
	if (this.check()) {
		var index = layer.open({
			type: 2,
			title: '分配会务管理员',
			area: ['800px', '350px'], //宽高
			fix: false, //不固定
			maxmin: true,
			content: Feng.ctxPath + '/meeting/openAllotManager?meetingId=' + meeting.seItem.id
				+ '&meetingTheme=' + meeting.seItem.meetingTheme
		});
		this.layerIndex = index;
	}
}


