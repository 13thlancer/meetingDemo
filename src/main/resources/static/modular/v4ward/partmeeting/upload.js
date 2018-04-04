
var $list = $("#thelist");

var uploader = WebUploader.create({

    // swf文件路径
    swf: '/js/Uploader.swf',
    // 文件接收服务端。
    server:'/partmeeting/upload',
    
    //自定义参数
    formData : { "partmeetingId": $("#partmeetingId").val()},

    // 选择文件的按钮。可选。
    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
    pick: '#picker',

    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
    resize: false,
    //只允许pdf上传
    accept: {
        title: 'PDF',
        extensions: 'PDF,pdf',
        mimeTypes: 'application/pdf'
    },
    //设定单个文件大小
    fileSingleSizeLimit : 1024*1024*100
 
});

uploader.on( 'fileQueued', function( file ) {
    $list.append( '<div id="' + file.id + '" class="item">' +
        '<div style="float:right;"> <button  class="btn btn-primary delbtn" onclick="delFile('+file.id+')">删除</button></div>'+
        '<h4 class="info">' + file.name + '</h4>' +
        '<p class="state">等待上传...</p>' +
        '</div>' );
});

uploader.on( 'uploadProgress', function( file, percentage ) {
    var $li = $( '#'+file.id ),
        $percent = $li.find('.progress .progress-bar');

    // 避免重复创建
    if ( !$percent.length ) {
        $percent = $('<div class="progress progress-striped active">' +
            '<div class="progress-bar" role="progressbar" style="width: 0%">' +
            '</div>' +
            '</div>').appendTo( $li ).find('.progress-bar');
    }

    $li.find('p.state').text('上传中');

    $percent.css( 'width', percentage * 100 + '%' );

   /* $list.on('click', '.delbtn', function() {
        uploader.removeFile( file );
    })*/
});

uploader.on( 'uploadSuccess', function( file ) {
    $( '#'+file.id ).find('p.state').text('已上传');
});

uploader.on( 'uploadError', function( file ) {
    $( '#'+file.id ).find('p.state').text('上传出错');
});

uploader.on( 'uploadComplete', function( file ) {
    $( '#'+file.id ).find('.progress').fadeOut();
});

//删除
$list.on("click", ".delbtn", function () {
    var $ele = $(this);
    var id = $ele.parent().parent().attr("id");

    var file = uploader.getFile(id);
    uploader.removeFile(file);
    $('#'+file.id).remove();
});

var $btn = $("#ctlBtn");
$btn.on('click', function () {
	if($list.children("div.item").length==0){
		Feng.alert("请选择文件后再上传!");
		return;
	}
    uploader.upload();
});

//当所有文件上传结束时触发 刷新页面
uploader.on( 'uploadFinished', function(  ) {
	 window.location.reload();
});


/**
* 验证文件格式以及文件大小
*/
uploader.on("error", function (type) {
    if (type == "Q_TYPE_DENIED") {
    	Feng.alert("请上传pdf格式文件");
    } else if (type == "F_EXCEED_SIZE") {
    	Feng.alert("单个文件大小不能超过100M");
    } else if (type == "F_DUPLICATE") {
    	Feng.alert("请选择非重复的文件");
    }else {
    	Feng.alert("上传出错！请检查后重新上传！错误代码"+type);
    }
});


var del = function(id) {
	var operation = function() {
		var ajax = new $ax(Feng.ctxPath + "/partmeeting/deleteResource",
				function(data) {
					Feng.success("删除成功!");
					window.location.reload();
					// partmeeting.table.refresh();
				}, function(data) {
					Feng.error("删除失败!" + data.responseJSON.message + "!");
				});
		ajax.set("id", id);
		ajax.start();
	};

	Feng.confirm("是否刪除此材料 ?", operation);
};


/**
 * 修改
 */
var layerIndex;

var openSet = function (id) {
		var index = layer.open({
			type: 2,
			title: '材料修改',
			area: ['600px', '300px'], //宽高
			fix: false, //不固定
			maxmin: true,
			content: Feng.ctxPath + '/partmeeting/toResourceSet/' + id
		});
		layerIndex = index;
};


var resourceInfoDlg = {
	    validateFields: {
	    	resourcesStatus: {
	            validators: {
	                notEmpty: {
	                    message: '签到类型不能为空'
	                }
	            }
	        },
	        orderCode: {
	            validators: {
	                notEmpty: {
	                    message: '排序号不能为空'
	                }
	            }
	        }
	    }
	};

/**
 * 关闭此对话框
 */
resourceInfoDlg.close = function () {
    parent.layer.close(window.parent.layerIndex);
};

/**
 * 验证数据是否为空
 */
resourceInfoDlg.validate = function () {
    $('#resourceInfoForm').data("bootstrapValidator").resetForm();
    $('#resourceInfoForm').bootstrapValidator('validate');
    return $("#resourceInfoForm").data('bootstrapValidator').isValid();
};


/**
 * 提交修改
 */
resourceInfoDlg.editSubmit = function () {

	var resourceData = {
			"id" :$("#id").val(),
			"resourcesStatus":$("#resourcesStatus").val(),
			"orderCode":$("#orderCode").val()
	}
	
    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax_json(Feng.ctxPath + "/partmeeting/resourceSet", function (data) {
        if(data.code == 403){
        	parent.Feng.error("修改失败! " + data.message + "!");
        }else{
        	parent.Feng.success("修改成功!");
        }
        
    	resourceInfoDlg.close()
        window.parent.location.reload();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(resourceData);
    ajax.start();
};


	
