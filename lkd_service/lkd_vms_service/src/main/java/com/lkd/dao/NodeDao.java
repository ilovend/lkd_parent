package com.lkd.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lkd.entity.NodeEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface NodeDao extends BaseMapper<NodeEntity> {

    @Select("select IFNULL(COUNT(1),0) from tb_node where region_id=#{regionId} ")
    Integer getCountByRegion(Long regionId);

    @Results(id = "nodeMap",value = {
//            @Result(property = "id",column = "id"),
            @Result(property = "vmCount",column = "id",many = @Many(select = "com.lkd.dao.VendingMachineDao.getCountByNodeId")),
            @Result(property = "regionId",column = "region_id"),
            @Result(property = "region",column = "region_id",one = @One(select = "com.lkd.dao.RegionDao.selectById")),
            @Result(property = "businessId",column = "business_id"),
            @Result(property = "businessType",column = "business_id",one = @One(select = "com.lkd.dao.BusinessTypeDao.selectById"))
    })
    @Select("select * from tb_node where id=#{nodeId} limit 1")
    NodeEntity getById(long nodeId);

    @ResultMap(value = "nodeMap")
    @Select("select * from tb_node where `name` like CONCAT('%',#{name},'%')")
    Page<NodeEntity> searchByName(Page<NodeEntity> page,String name);


}
