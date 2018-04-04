package com.v4ward.core.dict.base;

import java.util.HashMap;

/**
 * 
 *@Description: 字典映射抽象类
 *
 *
 * @ClassName: AbstractDictMap
 * @Package: com.whxx.core.dict
 * 
 * @author Capricornus
 * @date 2017/12/25 15:33
 */
public abstract class AbstractDictMap {

    private HashMap<String,String> dictory = new HashMap<>();
    private HashMap<String,String> fieldWarpperDictory = new HashMap<>();

    public AbstractDictMap() {
        put("id","主键id");
        init();
        initBeWrapped();
    }

    /**
     *
     * @Description: 初始化字段英文名称和中文名称对应的字典
     *
     * @author Capricornus
     * @date 2017/12/25 15:39
     */
    public abstract void init();

    /**
     *
     * @Description: 初始化需要被包装的字段(例如:性别为1:男,2:女,需要被包装为汉字)
     *
     * @author Capricornus
     * @date 2017/12/25 15:45
     */
    protected abstract void initBeWrapped();

    public String get(String key){
      return this.dictory.get(key);
    }

    public void put(String key,String value){
        this.dictory.put(key,value);
    }

    public String getFieldWarpperMethodName(String key){
        return this.fieldWarpperDictory.get(key);
    }

    public void putFieldWrapperMethodName(String key,String methodName){
        this.fieldWarpperDictory.put(key,methodName);
    }
}
