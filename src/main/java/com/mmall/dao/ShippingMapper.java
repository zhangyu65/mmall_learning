package com.mmall.dao;

import com.mmall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByUserIdShippingId(@Param("userId") Integer userId, @Param("userId") Integer shippingId);

    int updateByShipping(Shipping record);

    Shipping selectByUserIdShippingId(@Param("userId")Integer userId, @Param("ShippingId")Integer ShippingId);

    List<Shipping> selectByUserId(Integer UserId);
}