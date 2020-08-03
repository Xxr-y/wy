package com.fh.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.cart.model.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CartMapper extends BaseMapper<Cart> {
}
