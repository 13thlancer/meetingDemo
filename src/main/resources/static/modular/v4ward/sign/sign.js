/**
 * 签到信息初始化
 */
var sign = {
    id: "signTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

$(function() {
	
    var defaultColunms = sign.initColumn();
    var table = new BSTable(sign.id, "/sign/list", defaultColunms);
    table.setPaginationType("server");
    table.setQueryParams(sign.queryParams());
    sign.table = table.init();
});

sign.queryParams = function(){
	var queryData = {};
	queryData['partmeetingId'] = $("#partmeetingId").val();
	queryData['account'] = $("#account").val();
	queryData['name'] = $("#name").val();
	queryData['signStatus'] = $("#signStatus").val();
	return queryData;
}

/**
 * 查询签到列表
 */
sign.search = function () {
    sign.table.refresh({query: sign.queryParams()});
};

sign.reset = function () {
	$("#account").val("");
	$("#name").val("");
	$("#signStatus").val("");
};

/**
 * 初始化表格的列
 */
sign.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '账户', field: 'account', align: 'center', valign: 'middle', sortable: true},
        {title: '姓名', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '所属部门', field: 'orgName', align: 'center', valign: 'middle', sortable: true},
        {title: '酒店信息', field: 'hotelInfo', align: 'center', valign: 'middle', sortable: true},
        {title: '房间信息', field: 'roomInfo', align: 'center', valign: 'middle', sortable: true},
        {title: '座位信息', field: 'seatInfo', align: 'center', valign: 'middle', sortable: true},
        {title: '签到时间', field: 'signtime', align: 'center', valign: 'middle', sortable: true},
        {title: '签到类型', field: 'bindStatus', align: 'center', valign: 'middle', sortable: true,width:'10%',
        	formatter:function(value, row, index){
        		return 0==value?'后台导入':1==value?'扫码绑定':'-';
        	}
        },
        {title: '签到状态', field: 'signStatus', align: 'center', valign: 'middle', sortable: true,width:'10%',
        	formatter:function(value, row, index){
        		return 0==value?'未签到':1==value?'已签到':2==value?'迟到':3==value?'请假':'-';
        	}
        }
        ];
};

/**
 * 检查是否选中
 */
sign.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        sign.seItem = selected[0];
        return true;
    }
};

/**
 * 签到请假
 */
sign.leave = function() {
	if (this.check()) {
		var ajax = new $ax(Feng.ctxPath + "/sign/leave", function(data) {
			Feng.success("操作成功!");
			sign.table.refresh();
		}, function(data) {
			Feng.error("操作失败!" + data.responseJSON.message + "!");
		});
		ajax.set("id", sign.seItem.id);
		ajax.start();
	};
};


/**
 * 跳转到修改参会人员界面
 */
sign.toUpdateMeetingUser = function() {
    if (this.check()) {
        var index = layer.open({
            type : 2,
            title : '参会人员修改',
            area : [ '900px', '420px' ], // 宽高
            fix : false, // 不固定
            maxmin : true,
            content : Feng.ctxPath + '/sign/toUpdateMeetingUser/' + sign.seItem.id
        });
        this.layerIndex = index;
    }
}

/**
 * 导出
 */
sign.export = function () {
	   window.location.href = Feng.ctxPath + "/sign/export?partmeetingId="
			+ $("#partmeetingId").val() + "&partmeetingTitle="
			+ $("#partmeetingTitle").text();
};

/**
 * 删除参会人员
 */
sign.delMeetingUser = function(){
    if (this.check()) {

        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/sign/delMeetingUser", function (data) {
                Feng.success("删除成功!");
                sign.table.refresh();
            }, function(data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", sign.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否刪除该参会人员?", operation);
    }
}
