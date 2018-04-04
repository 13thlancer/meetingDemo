package com.v4ward.core.service;

import com.v4ward.core.model.Dict;

import java.util.List;
import java.util.Map;

public interface DictSrv {
    Dict selectById(String dictId);

    void addDict(String dictName, String dictValues);

    List<Map<String,Object>> list(String condition);

    void editDict(String dictId, String dictName, String dictValues);

    void delteDict(String dictId);

    List<Dict> selectListByPid(String dictId);
}
