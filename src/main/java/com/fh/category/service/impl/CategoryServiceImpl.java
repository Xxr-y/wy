package com.fh.category.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.fh.category.mapper.CategoryMapper;
import com.fh.category.service.CategoryService;
import com.fh.common.ServerResponse;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse queryList() {
        // 判断缓存中是否存在该数据
        boolean exist = RedisUtil.exist(SystemConstant.REDIS_CATEGARY_KEY);
        if(exist){
            // 存在的话
            String s = RedisUtil.get(SystemConstant.REDIS_CATEGARY_KEY);
            List<Map> mapList = JSONArray.parseArray(s, Map.class);
            return ServerResponse.success(mapList);
        }
        List<Map<String ,Object>>  allList =   categoryMapper.queryList();
        List<Map<String ,Object>>  parentList = new ArrayList<Map<String, Object>>();
        for (Map  map : allList) {
            if(map.get("pid").equals(0)){
                parentList.add(map);
            }
        }
        selectChildren(parentList,allList);
        String jsonString = JSONArray.toJSONString(parentList);
        // 把查询出来的数据放入到缓存中
        RedisUtil.set(SystemConstant.REDIS_CATEGARY_KEY,jsonString);
        return ServerResponse.success(parentList);
    }


    public void selectChildren(List<Map<String ,Object>>  parentList, List<Map<String ,Object>>  allList){
        for (Map<String, Object> pmap : parentList) {
            List<Map<String ,Object>>  childrenList = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> amap : allList) {
                if(pmap.get("id").equals(amap.get("pid"))){
                    childrenList.add(amap);
                }
            }
            if(childrenList!=null && childrenList.size()>0){
                pmap.put("children",childrenList);
                selectChildren(childrenList,allList);
            }

        }
    }

}
