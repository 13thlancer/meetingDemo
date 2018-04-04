/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var meetingInfoDlg = {
    meetingInfoData: {},
    validateFields: {
    	meetingTheme: {
            validators: {
                notEmpty: {
                    message: '会议主题不能为空'
                }
            }
        },
        starttime: {
            validators: {
                notEmpty: {
                    message: '开始时间不能为空'
                }
            }
        },
        endtime: {
            validators: {
                notEmpty: {
                    message: '结束时间不能为空'
                }
            }
        },
        meetingLocation: {
            validators: {
                notEmpty: {
                    message: '会议地点不能为空'
                }
            }
        },
        meetingNotice: {
            validators: {
                notEmpty: {
                    message: '会议须知不能为空'
                }
            }
        },
        meetingStatus: {
        	validators: {
        		notEmpty: {
        			message: '会议状态不能为空'
        		}
        	}
        }
    }
};

/**
 * 清除数据
 */
meetingInfoDlg.clearData = function () {
    this.meetingInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
meetingInfoDlg.set = function (key, val) {
    this.meetingInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : val;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
meetingInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
meetingInfoDlg.close = function () {
    parent.layer.close(window.parent.meeting.layerIndex);
};



/**
 * 收集数据
 */
meetingInfoDlg.collectData = function () {
    this.set('id').set('meetingTheme').set('meetingBrief').set('starttime').set('endtime')
        .set('meetingLocation').set('meetingNotice').set('meetingStatus');
};


/**
 * 验证数据是否为空
 */
meetingInfoDlg.validate = function () {
    $('#meetingInfoForm').data("bootstrapValidator").resetForm();
    $('#meetingInfoForm').bootstrapValidator('validate');
    return $("#meetingInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加会议
 */
meetingInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    
    if(this.get('starttime')>this.get('endtime')){
    	Feng.error("开始时间不能大于结束时间");
    	return;
    }

    //提交信息
    var ajax = new $ax_json(Feng.ctxPath + "/meeting/meetingAdd", function (data) {
    			
        if(data.code == 403){
            Feng.error("添加失败！" + data.message + "!");
        }else{
            Feng.success("添加成功!");
        }
        window.parent.meeting.table.refresh();
        meetingInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.meetingInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
meetingInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    
    if(this.get('starttime')>this.get('endtime')){
    	Feng.error("开始时间不能大于结束时间");
    	return;
    }

    //提交信息
    var ajax = new $ax_json(Feng.ctxPath + "/meeting/meetingEdit", function (data) {
        if(data.code == 403){
            Feng.error("修改失败! " + data.message + "!");
        }else{
            Feng.success("修改成功!");
        }
        if (window.parent.meeting != undefined) {
            window.parent.meeting.table.refresh();
            meetingInfoDlg.close();
        }
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.meetingInfoData);
    ajax.start();
};


$(function () {
    Feng.initValidator("meetingInfoForm", meetingInfoDlg.validateFields);
});



/**
 * 提交分配会务管理员
 */
meetingInfoDlg.allotMeetingManager = function () {

	var managerIds = "";
	$("input:checkbox[name=manager]:checked").each(function(i) {

		if (0 == i) {
			managerIds = $(this).val();
		} else {
			managerIds += ("," + $(this).val());
		}
	});

	var ajax = new $ax(Feng.ctxPath + "/meeting/allotMeetingManager",

	function(data) {
		if (data.code == 403) {
			Feng.error("操作失败！" + data.message + "!");
		} else {
			Feng.success("操作成功!");
		}
		meetingInfoDlg.close();
	}, function(data) {
		Feng.error("操作失败!" + data.responseJSON.message + "!");
	});
	ajax.set("meetingId", this.get('meetingId'));
	ajax.set("managerIds", managerIds);
	ajax.start();
};
