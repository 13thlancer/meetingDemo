/**
 * 分会场管理初始化
 */
var partmeeting = {
    id: "partmeetingTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

var meetingId ;

$(function() {
	
	meetingId = $("#meetingId").val();
	
    var defaultColunms = partmeeting.initColumn();
    var table = new BSTable(partmeeting.id, "/partmeeting/list", defaultColunms);
    table.setPaginationType("server");
    table.setQueryParams(partmeeting.queryParams());
    partmeeting.table = table.init();
});

//查询参数
partmeeting.queryParams = function(){
	var queryData = {};
	queryData['meetingId'] = meetingId;
	queryData['partmeetingTitle'] = $("#partmeetingTitle").val();
	return queryData;
}

/**
 * 查询分会场列表
 */
partmeeting.search = function () {
    partmeeting.table.refresh({query: partmeeting.queryParams()});
};

partmeeting.reset = function () {
	$("#partmeetingTitle").val("");
};

/**
 * 初始化表格的列
 */
partmeeting.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '分会场主题', field: 'partmeetingTitle', align: 'center', valign: 'middle', sortable: true},
        {title: '分会场地点', field: 'partmeetingLocation', align: 'center', valign: 'middle', sortable: true},
        {title: '分会场主持人', field: 'partmeetingHost', align: 'center', valign: 'middle', sortable: true},
        {title: '分会场状态', field: 'partmeetingStatus', align: 'center', valign: 'middle', sortable: true,width:'30px',
        	formatter:function(value, row, index){
        		return 0==value?'创建':1==value?'发布':2==value?'取消':3==value?'完成':'-';
        	}
        },
        {title: '是否允许扫码绑定', field: 'scanStatus', align: 'center', valign: 'middle', sortable: true,width:'30px',
        	formatter:function(value, row, index){
        		return 0==value?'允许':1==value?'不允许':'-';
        	}
        }];
};

/**
 * 检查是否选中
 */
partmeeting.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        partmeeting.seItem = selected[0];
        return true;
    }
};

/**
 * 分会场信息录入录入
 */
partmeeting.openAddPartmeeting = function () {
	
//	layer提供了5种层类型。可传入的值有：0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
    var index = layer.open({
        type: 2,
        title: '添加分会场信息',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/partmeeting/toadd/'+meetingId
    });
    this.layerIndex = index;
};

/**
 * 查看
 */
partmeeting.view = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '分会场查看',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/partmeeting/toview/' + partmeeting.seItem.id
        });
        this.layerIndex = index;
    }
};


/**
 * 参会人员分配
 */
partmeeting.openAssignUser = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '分会场查看',
            area: ['90%', '90%'], //宽高
            fix: false, //不固定
            maxmin: false,
            content: Feng.ctxPath + '/partmeeting/toAssignUser/' + partmeeting.seItem.id
        });
        this.layerIndex = index;
        layer.full(index);
    }
};

/**
 * 修改
 */
partmeeting.modify = function () {
	if (this.check()) {
		var index = layer.open({
			type: 2,
			title: '分会场修改',
			area: ['800px', '420px'], //宽高
			fix: false, //不固定
			maxmin: true,
			content: Feng.ctxPath + '/partmeeting/tomodify/' + partmeeting.seItem.id
		});
		this.layerIndex = index;
	}
};

/**
 * 分会场删除
 */
partmeeting.del = function () {
    if (this.check()) {

        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/partmeeting/delete", function (data) {
                Feng.success("删除成功!");
                partmeeting.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("partmeetingId", partmeeting.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否刪除选中的分会场 ?", operation);
    }
};

/**
 * 跳转议程信息
 */
partmeeting.openSubmeeting = function() {
	if (this.check()) {
		window.location.href = '/submeeting?partmeetingId='
				+ partmeeting.seItem.id + '&partmeetingTitle='
				+ partmeeting.seItem.partmeetingTitle;
	}
}

/**
 * 上传材料
 */
partmeeting.openUpload = function () {
	if (this.check()) {
		var index = layer.open({
			type: 2,
			title: '会议材料',
			area: ['900px', '420px'], //宽高
			fix: false, //不固定
			maxmin: true,
			content: Feng.ctxPath + '/partmeeting/toUpload/' + partmeeting.seItem.id
		});
		this.layerIndex = index;
	}
};


/**
 * 二维码
 *
 */
partmeeting.qrCode = function() {

	if (this.check()) {
		var a = {
				partmeetingId : partmeeting.seItem.id
			};
		var index = layer.open({
			type : 2,
			title : '生成二维码',
			area : [ '300px', '350px' ], //宽高
			fix : false, //不固定
			maxmin : true,
			content : Feng.ctxPath + '/partmeeting/qrCode?data='+encodeURIComponent(JSON.stringify(a))
		});
		this.layerIndex = index;
	}
};


/**
 * 点击选择文件调用隐藏的input
 */
partmeeting.selectFile = function(){
    $("#selectFile").click();
}

var file = $('#selectFile');
file.on('change', function(e) {
	// e.currentTarget.files 是一个数组，如果支持多个文件，则需要遍历
	var filename = e.currentTarget.files[0].name;
	$("#file").text(filename.length > 15 ? filename.substr(0, 15)+'...' : filename);
});


/**
 * 导入
 */
partmeeting.importExcel = function() {

	 var fileObj = document.getElementById("selectFile").files[0]; // js 获取文件对象
     if (typeof (fileObj) == "undefined" || fileObj.size <= 0) {
         Feng.info("请选择导入文件!");
         return;
     }

	if (this.check()) {
		var data = new FormData($('#excel')[0]);
		data.append("partmeetingId",partmeeting.seItem.id);
		
		$.ajax({
			url : Feng.ctxPath + "/partmeeting/importExcel",
			type : "POST",
			async : false,
			data : data,
			processData : false,
			contentType : false,
			success : function(data) {
				if (data.code == 200) {
					Feng.success("导入成功!");
				} else if(data.code ==9002){
					layer.open({
						  content: data.message,
						  scrollbar: false
						});
				}else {
					Feng.error("导入失败," + data.message);
				}
			}
		});
	}
};


partmeeting.excelModel = function(){
    window.location.href=Feng.ctxPath + "/partmeeting/excelModel"
}



/**
 * 跳转签到统计页面
 */
partmeeting.sign = function() {
	if (this.check()) {
		window.location.href = '/sign?partmeetingId='
				+ partmeeting.seItem.id + '&partmeetingTitle='
				+ partmeeting.seItem.partmeetingTitle+'&meetingTheme='+$("#meetingTheme").text();
	}
}

