package com.fh.address.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.address.model.Address;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AddressMapper extends BaseMapper<Address> {
}
