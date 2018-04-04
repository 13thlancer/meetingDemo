package com.v4ward.core.dict;


import com.v4ward.core.dict.base.AbstractDictMap;

/**
 * 日志的字典
 *
 * @author fengshuonan
 * @date 2017-05-06 15:01
 */
public class LogDict extends AbstractDictMap {

    @Override
    public void init() {
        put("tips","备注");
    }

    @Override
    protected void initBeWrapped() {

    }
}
