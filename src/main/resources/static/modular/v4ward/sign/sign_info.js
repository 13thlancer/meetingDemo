/**
 * 菜单详情对话框
 */
var SignInfoDlg = {
    signInfoData: {},

};


/**
 * 清除数据
 */
SignInfoDlg.clearData = function () {
    this.signInfoData = {};
}


/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SignInfoDlg.set = function (key, val) {
    this.signInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}


/**
 * 收集数据
 */
SignInfoDlg.collectData = function () {
    this.set('id').set('hotelInfo').set('roomInfo').set('seatInfo');
}


/**
 * 关闭此对话框
 */
SignInfoDlg.close = function () {
    parent.layer.close(window.parent.sign.layerIndex);
}



/**
 *修改参会人员信息
 */
SignInfoDlg.updateMeetingUser = function() {

    this.clearData();
    this.collectData();

    var ajax = new $ax(Feng.ctxPath + "/sign/updateMeetingUser", function(data) {
        Feng.success("操作成功!");
        window.parent.sign.table.refresh();
        SignInfoDlg.close();
    }, function(data) {
        Feng.error("操作失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.signInfoData);
    ajax.start();
}
