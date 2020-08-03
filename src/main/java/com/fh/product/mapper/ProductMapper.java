package com.fh.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.product.model.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    @Select("select count(*) from t_product")
    Long queryTotalCount();


    @Select("select * from t_product order by id desc limit #{start},#{pageSize}")
    List<Product> queryList(@Param("start") Long start, @Param("pageSize") Long pageSize);

    Long updateStock(@Param("id") Integer id, @Param("count") int count);
}
