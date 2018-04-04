package com.v4ward.core.factory;


import com.github.pagehelper.PageHelper;
import com.v4ward.core.kit.HttpKit;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @Description: BootStrap Table默认的分页参数创建
 *
 * @ClassName: PageFactory
 *
 * @author huangxiang
 * @date 2017/12/26 14:43
 */
public class PageFactory<T> {

    public void defaultPage() {
        HttpServletRequest request = HttpKit.getRequest();
        int limit = Integer.valueOf(request.getParameter("limit"));     //每页多少条数据
        int offset = Integer.valueOf(request.getParameter("offset"));   //每页的偏移量(本页当前有多少条)
        String sort = request.getParameter("sort");         //排序字段名称
        String order = request.getParameter("order");       //asc或desc(升序或降序)

//        Page<T> page = new Page<>((offset / limit + 1), limit);

        PageHelper.startPage((offset / limit + 1), limit);
//            page.setOpenSort(false);
//        return page;

//        if (ToolUtil.isEmpty(sort)) {
//            Page<T> page = new Page<>((offset / limit + 1), limit);
//            page.setOpenSort(false);
//            return page;
//        } else {
//            Page<T> page = new Page<>((offset / limit + 1), limit, sort);
//            if (Order.ASC.getDes().equals(order)) {
//                page.setAsc(true);
//            } else {
//                page.setAsc(false);
//            }
//            return page;
//        }
    }
}
