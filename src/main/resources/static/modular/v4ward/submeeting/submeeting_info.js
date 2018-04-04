/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var submeetingInfoDlg = {
    submeetingInfoData: {},
    validateFields: {
    	submeetingTheme: {
            validators: {
                notEmpty: {
                    message: '议程主题不能为空'
                }
            }
        },
        starttime: {
            validators: {
                notEmpty: {
                    message: '议程开始时间不能为空'
                }
            }
        },
        endtime: {
            validators: {
                notEmpty: {
                    message: '议程结束时间不能为空'
                }
            }
        },
        submeetingLocation: {
            validators: {
                notEmpty: {
                    message: '议程地点不能为空'
                }
            }
        },
       submeetingHost: {
            validators: {
                notEmpty: {
                    message: '主持人不能为空'
                }
            }
        },
        submeetingStatus: {
        	validators: {
        		notEmpty: {
        			message: '议程状态不能为空'
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
 * 清除数据
 */
submeetingInfoDlg.clearData = function () {
    this.submeetingInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
submeetingInfoDlg.set = function (key, val) {
    this.submeetingInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : val;
    return this;
};

/**
 * 取对话框中的数据
 *
 * @param key 数据的名称
 */
submeetingInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
submeetingInfoDlg.close = function () {
    parent.layer.close(window.parent.submeeting.layerIndex);
};


/**
 * 收集数据
 */
submeetingInfoDlg.collectData = function () {
    this.set('id').set('partmeetingId').set('submeetingTheme').set('starttime').set('endtime')
        .set('submeetingLocation').set('submeetingHost').set('submeetingStatus').set('orderCode');
};


/**
 * 验证数据是否为空
 */
submeetingInfoDlg.validate = function () {
    $('#submeetingInfoForm').data("bootstrapValidator").resetForm();
    $('#submeetingInfoForm').bootstrapValidator('validate');
    return $("#submeetingInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加议程
 */
submeetingInfoDlg.addSubmit = function () {

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
    var ajax = new $ax_json(Feng.ctxPath + "/submeeting/submeetingAdd", function (data) {
    			
        if(data.code == 403){
            Feng.error("添加失败！" + data.message + "!");
        }else{
            Feng.success("添加成功!");
        }
        window.parent.submeeting.table.refresh();
        submeetingInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.submeetingInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
submeetingInfoDlg.editSubmit = function () {

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
    var ajax = new $ax_json(Feng.ctxPath + "/submeeting/submeetingEdit", function (data) {
        if(data.code == 403){
            Feng.error("修改失败! " + data.message + "!");
        }else{
            Feng.success("修改成功!");
        }
        if (window.parent.submeeting != undefined) {
            window.parent.submeeting.table.refresh();
            submeetingInfoDlg.close();
        }
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.submeetingInfoData);
    ajax.start();
};


$(function () {
    Feng.initValidator("submeetingInfoForm", submeetingInfoDlg.validateFields);
});
