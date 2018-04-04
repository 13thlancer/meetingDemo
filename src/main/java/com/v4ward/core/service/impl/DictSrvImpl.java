package com.v4ward.core.service.impl;

import com.v4ward.core.exception.BizExceptionEnum;
import com.v4ward.core.exception.BussinessException;
import com.v4ward.core.factory.MutiStrFactory;
import com.v4ward.core.model.Dict;
import com.v4ward.core.dao.DictMapper;
import com.v4ward.core.service.DictSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DictSrvImpl implements DictSrv {

    @Autowired
    private DictMapper dictMapper;

    @Override
    public Dict selectById(String dictId) {
        return dictMapper.selectById(dictId);
    }

    @Override
    public void addDict(String dictName, String dictValues) {
        //判断有没有该字典
        List<Dict> dicts = dictMapper.selectListByNameAndPid(dictName,"0");

        if(dicts != null && dicts.size() > 0){
            throw new BussinessException(BizExceptionEnum.DICT_EXISTED);
        }

        //解析dictValues
        List<Map<String, String>> items = MutiStrFactory.parseKeyValue(dictValues);

        //添加字典
        Dict dict = new Dict();
        dict.setName(dictName);
        dict.setNum("0");
        dict.setPid("0");
        this.dictMapper.insert(dict);

        //添加字典条目
        for (Map<String, String> item : items) {
            String num = item.get(MutiStrFactory.MUTI_STR_KEY);
            String name = item.get(MutiStrFactory.MUTI_STR_VALUE);
            Dict itemDict = new Dict();
            itemDict.setPid(dict.getId());
            itemDict.setName(name);
            try {
                itemDict.setNum(num);
            }catch (NumberFormatException e){
                throw new BussinessException(BizExceptionEnum.DICT_MUST_BE_NUMBER);
            }
            this.dictMapper.insert(itemDict);
        }
    }

    @Override
    public List<Map<String, Object>> list(String condition) {
        return this.dictMapper.list(condition);
    }

    @Override
    public void editDict(String dictId, String dictName, String dictValues) {
        //删除之前的字典
        this.delteDict(dictId);

        //重新添加新的字典
        this.addDict(dictName,dictValues);
    }

    @Override
    public void delteDict(String dictId) {
        //删除这个字典的子词典
        dictMapper.deleteByPid(dictId);

        //删除这个词典
        dictMapper.deleteById(dictId);
    }

    @Override
    public List<Dict> selectListByPid(String dictId) {
        return dictMapper.selectListByPid(dictId);
    }
}
