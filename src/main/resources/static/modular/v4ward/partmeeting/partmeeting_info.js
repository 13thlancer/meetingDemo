/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var partmeetingInfoDlg = {
    partmeetingInfoData: {},
    validateFields: {
    	partmeetingTitle: {
            validators: {
                notEmpty: {
                    message: '分会场主题不能为空'
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
        earlytime: {
        	validators: {
        		notEmpty: {
        			message: '最早签到时间不能为空'
        		}
        	}
        },
        latesttime: {
        	validators: {
        		notEmpty: {
        			message: '最晚签到时间不能为空'
        		}
        	}
        },
        partmeetingLocation: {
            validators: {
                notEmpty: {
                    message: '分会场地点不能为空'
                }
            }
        },
        partmeetingHost: {
            validators: {
                notEmpty: {
                    message: '分会场主持人不能为空'
                }
            }
        },
        scanStatus: {
        	validators: {
        		notEmpty: {
        			message: '扫码绑定不能为空'
        		}
        	}
        },
        partmeetingStatus: {
        	validators: {
        		notEmpty: {
        			message: '分会场状态不能为空'
        		}
        	}
        }
    }
};

/**
 * 清除数据
 */
partmeetingInfoDlg.clearData = function () {
    this.partmeetingInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
partmeetingInfoDlg.set = function (key, val) {
    this.partmeetingInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : val;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
partmeetingInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
partmeetingInfoDlg.close = function () {
    parent.layer.close(window.parent.partmeeting.layerIndex);
};



/**
 * 收集数据
 */
partmeetingInfoDlg.collectData = function () {
    this.set('id').set('meetingId').set('partmeetingTitle').set('starttime').set('endtime')
        .set('earlytime').set('latesttime').set('partmeetingLocation')
        .set('partmeetingHost').set('scanStatus').set('partmeetingStatus');
};


/**
 * 验证数据是否为空
 */
partmeetingInfoDlg.validate = function () {
    $('#partmeetingInfoForm').data("bootstrapValidator").resetForm();
    $('#partmeetingInfoForm').bootstrapValidator('validate');
    return $("#partmeetingInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加会议
 */
partmeetingInfoDlg.addSubmit = function () {
	
    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    
    if(this.get('starttime')>this.get('endtime')){
    	Feng.error("开始时间不能大于结束时间");
    	return;
    }
    if(this.get('earlytime')>this.get('latesttime')){
    	Feng.error("最早签到时间不能大于最晚签到时间");
    	return;
    }

    //提交信息
    var ajax = new $ax_json(Feng.ctxPath + "/partmeeting/partmeetingAdd", function (data) {
    			
        if(data.code == 403){
            Feng.error("添加失败！" + data.message + "!");
        }else{
            Feng.success("添加成功!");
        }
        window.parent.partmeeting.table.refresh();
        partmeetingInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.partmeetingInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
partmeetingInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    
    if(this.get('starttime')>this.get('endtime')){
    	Feng.error("开始时间不能大于结束时间");
    	return;
    }
    if(this.get('earlytime')>this.get('latesttime')){
    	Feng.error("最早签到时间不能大于最晚签到时间");
    	return;
    }

    //提交信息
    var ajax = new $ax_json(Feng.ctxPath + "/partmeeting/partmeetingEdit", function (data) {
        if(data.code == 403){
            Feng.error("修改失败! " + data.message + "!");
        }else{
            Feng.success("修改成功!");
        }
        if (window.parent.partmeeting != undefined) {
            window.parent.partmeeting.table.refresh();
            partmeetingInfoDlg.close();
        }
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.partmeetingInfoData);
    ajax.start();
};


$(function () {
    Feng.initValidator("partmeetingInfoForm", partmeetingInfoDlg.validateFields);
});
