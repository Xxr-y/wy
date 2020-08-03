package com.fh.category.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.category.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    List<Map<String, Object>> queryList();
}
