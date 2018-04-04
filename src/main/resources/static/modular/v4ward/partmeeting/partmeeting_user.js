/**
 * 用户详情对话框（可用于添加和修改对话框）
 */

var userIds = [];
var names = [];
var selects = [];
var delUsers = [];
var init = [];
var flag;
var delflag;

var partmeetingUserDlg = {
    id: "partmeetingUserTable",//表格id
    seItem: null,		//选中的条目
    partmeetingUserData: {},
    ztreeInstance: null,
    table: null,
    validateFields: {

    }
};

/**
 * 初始化表格的列
 */
partmeetingUserDlg.initColumn = function () {
    var columns = [
        {field: 'selectItem', checkbox: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '姓名', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '电话', field: 'phone', align: 'center', valign: 'middle', sortable: true},
        {title: '职务', field: 'position', align: 'center', valign: 'middle', sortable: true},
        {title: '组织全称', field: 'orgName', align: 'center', valign: 'middle', sortable: true}];
    return columns;
};

/**
 * 清除数据
 */
partmeetingUserDlg.clearData = function () {
    this.partmeetingUserData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
partmeetingUserDlg.set = function (key, val) {
    this.partmeetingUserData[key] = (typeof value == "undefined") ? $("#" + key).val() : val;
    return this;
};
/**
 * 搜索
 */
partmeetingUserDlg.search = function () {
    initZtree();
    var searchData = {};
    searchData['name'] = $("#search").val();
    searchData['code'] = null;
    partmeetingUserDlg.table.refresh({query: searchData});
/*
    $("#partmeetingUserTable").bootstrapTable("checkBy", {field:"id", values:userIds})
*/
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
partmeetingUserDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
partmeetingUserDlg.close = function () {
    parent.layer.close(window.parent.partmeeting.layerIndex);
};

partmeetingUserDlg.onClickOrg = function(e, treeId, treeNode){
    $("#search").val("");
    var queryData = {};
    queryData['code'] = treeNode.id;
    queryData['name'] = $("#search").val();
    partmeetingUserDlg.table.refresh({query: queryData});

}

partmeetingUserDlg.clearSearch = function(){
    initZtree();
    $("#search").val("");
    initTable();
}

partmeetingUserDlg.clearUser = function(){

    $("#partmeetingUserTable").bootstrapTable("uncheckAll");
    $("#meetingUser li").remove();
    selects = [];
}

partmeetingUserDlg.addUser = function(){
    var list = $('#partmeetingUserTable').bootstrapTable("getSelections");
    var userIds = [];
    list.forEach(function(e){
        userIds.push(e.id);
    });
    var data = {
        partmeetingId:$("#meetingId").val(),
        userIds:JSON.stringify(userIds),
        delUsers:JSON.stringify(delUsers)
    };
    $.ajax({
        url: Feng.ctxPath + "/partmeeting/addPartmeetingUser",
        type: "POST",
        data: data,
        success: function(data){
            if(data.code != 200){
                Feng.error("修改失败! " + data.message + "!");
            }else{
                Feng.success("修改成功!");
                window.parent.partmeeting.table.refresh();
                partmeetingUserDlg.close();
            }
        }
    });
}

function initZtree() {
    var ztree = new $ZTree("zTree", "/org/selectOrgTreeList");
    ztree.bindOnClick(partmeetingUserDlg.onClickOrg);
    ztree.init();
    partmeetingUserDlg.ztreeInstance = ztree;
}

function initTable(){
    var defaultColunms = partmeetingUserDlg.initColumn();
    var table = new BSTable("partmeetingUserTable", "/mgr/list", defaultColunms);
    table.setPaginationType("client");
    table.setHeight(297);
    table.setPageSize(5);
    table.isShowColumns(false);
    table.isShowRefresh(false);
    table.isPagination(false);
    partmeetingUserDlg.table = table.init();
}

$(function () {
    initTable();
    initZtree();
    var datas = $("#userIds").val();
    loadData(datas);
    flag = true;
});


$('#partmeetingUserTable').on("load-success.bs.table",function(e, row, $element) {
    var checkIds = [];
    if(flag){
        $("#partmeetingUserTable").bootstrapTable("checkBy", {field:"id", values:init});
        flag = false;
    }else{
        selects.forEach(function(e){
            checkIds.push(e.id);
        });
    }
    $("#partmeetingUserTable").bootstrapTable("checkBy", {field:"id", values:checkIds});
});

$('#partmeetingUserTable').on("check.bs.table",function(e, row, $element) {
    delUsers = removedelUsersId(row.id);

    addSingleRefreshUl(row);
});

$('#partmeetingUserTable').on("uncheck.bs.table",function(e, row, $element) {
    delUsers.push(row.id);
    delSingleRefreshUl(row);
});

$('#partmeetingUserTable').on("check-all.bs.table",function(e, row, $element) {
    row.forEach(function(e){
        delUsers = removedelUsersId(e.id);
    });
    addrefreshUl();
});

$('#partmeetingUserTable').on("uncheck-all.bs.table",function(e, row, $element) {
    row.forEach(function(e){
        delUsers.push(e.id);
    });
    delrefreshUl(row);
});

function addrefreshUl(){
    var list = $('#partmeetingUserTable').bootstrapTable("getSelections");
    list.forEach(function(e){
        selects.push(e);
    });
    rebuildUl();
}

function addSingleRefreshUl(row){
    selects.push(row);
    rebuildUl();
}

function delrefreshUl(row){
    if(delflag){
        delflag =false;
    }else {
        for(var i =0;i<row.length;i++){
            selects = removeSelects(row[i]);
        }
        $('#meetingUser li').remove();
        for(var i = 0;i<selects.length;i++){
            var html = "<li class=\"list-group-item\" id=\"\"+selects[i].id+\"\"><span class=\"badge badge-danger\" onclick=\"javascript:delName(this.id)\"><i class=\"fa fa-times\"></i></span>"+selects[i].name+"</li>";
            $("#meetingUser").append(html);
        }
    }
}

function delSingleRefreshUl(row){
    if(delflag){
        delflag =false;
    }else{
        selects = removeSelects(row);
        $('#meetingUser li').remove();
        for(var i = 0;i<selects.length;i++){
            var html = "<li class=\"list-group-item\" id=\"\"+selects[i].id+\"\"><span class=\"badge badge-danger\" onclick=\"javascript:delName(this.id)\"><i class=\"fa fa-times\"></i></span>"+selects[i].name+"</li>";
            $("#meetingUser").append(html);
        }
    }
}

function unique(array) {
    // console.log(array);
    var newArray = [];
    var newId = [];
    array.forEach(function (index) {
        if (newId.indexOf(index.id) == -1) {
            newId.push(index.id);
            newArray.push(index);
        }
    });
    return newArray;
}

function removeSelects(e){
    var i = selects.length;
    while(i--){
        if(selects[i].id==e.id){
            selects.splice(i,1);
        }
    }
    return selects;
}


function removeId(id){
    var i = selects.length;
    while(i--){
        if(selects[i].id==id){
            selects.splice(i,1);
        }
    }
    return selects;
}


function removedelUsersId(id){
    var i = delUsers.length;
    while(i--){
        if(delUsers[i]==id){
            delUsers.splice(i,1);
        }
    }
    return delUsers;
}

function rebuildUl(){
    selects = unique(selects);
    $('#meetingUser li').remove();
    for(var i = 0;i<selects.length;i++){
        var html = "<li class=\"list-group-item\" id='"+selects[i].id+"' ><span class=\"badge badge-danger\"   id='"+selects[i].id+"' onclick=\"javascript:delName(this.id)\"><i class=\"fa fa-times\"></i></span>"+selects[i].name+"</li>";
        $("#meetingUser").append(html);
    }
}

function delName(id) {

    // alert(id);
    $("#"+id+"").remove();
      var uncheckIds = [];
      uncheckIds.push(id);
    delUsers.push(id);
    selects=removeId(id);
    // alert(uncheckIds);
    delflag = true;

    $("#partmeetingUserTable").bootstrapTable("uncheckBy", {field:"id", values:uncheckIds});
}

function loadData(datas){
    var data = $.parseJSON(datas);
    $('#meetingUser li').remove();
    for(var i = 0;i<data.length;i++){
        init.push(data[i].userId);
        var html = "<li class=\"list-group-item\" id='"+data[i].userId+"' ><span class=\"badge badge-danger\"   id='"+data[i].userId+"' onclick=\"javascript:delName(this.id)\"><i class=\"fa fa-times\"></i></span>"+data[i].name+"</li>";
        $("#meetingUser").append(html);
    }

}