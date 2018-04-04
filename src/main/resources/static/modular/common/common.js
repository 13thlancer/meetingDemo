$(function () {
    var firstMenus = $.parseJSON($("#menus").val());

    console.log(firstMenus);


    function transData(a, idStr, pidStr, chindrenStr) {
        var r = [], hash = {}, id = idStr, pid = pidStr, children = chindrenStr, i = 0, j = 0, len = a.length;
        for (; i < len; i++) {
            hash[a[i][id]] = a[i];
        }
        for (; j < len; j++) {
            var aVal = a[j], hashVP = hash[aVal[pid]];
            if (hashVP) {
                !hashVP[children] && (hashVP[children] = []);
                hashVP[children].push(aVal);
            } else {
                r.push(aVal);
            }
        }
        return r;
    }

    var menu = transData(firstMenus, 'code', 'pcode', 'children');

    console.log(menu);

    for(var i =0;i<menu.length;i++){
        var html = "<li><a href='"+menu[i].url+"'><i class='fa'></i><span class='nav-label'>"+menu[i].name+"</span><span class='fa arrow'></span></a> <ul id='"+menu[i].code+"' class='nav nav-second-level'></ul></li>";
/*
        var html = "<li id='"+menu[i].id+"' class='treeview'><a href='#'><i class='fa fa-table'></i> <span>"+menu[i].name+"</span><span class='pull-right-container'><i class='fa fa-angle-left pull-right'></i></span></a><ul class='treeview-menu'></ul></li>";
*/
        $("#side-menu").append(html);

        if(menu[i].hasOwnProperty('children')){
            var submenu = menu[i].children;

            for(var j =0;j<submenu.length;j++){
                var subhtml ="<li><a class='J_menuItem' href='"+submenu[j].url+"' name='tabMenuItem'>"+submenu[j].name+"</a></li>";
/*
                var subhtml ="<li id='"+submenu[j].id+"' class='treeview'><a href='#'><i class='fa fa-circle-o'></i>"+submenu[j].name+"<span class='pull-right-container'><i class='fa fa-angle-left pull-right'></i></span></a><ul class='treeview-menu'></ul></li>";
*/
                $("#"+menu[i].code+"").append(subhtml);


            }
        }
    }

})
