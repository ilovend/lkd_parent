package com.lkd.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.lkd.dao.NodeDao;
import com.lkd.entity.NodeEntity;
import com.lkd.service.NodeService;
import com.lkd.vo.Pager;
import org.springframework.stereotype.Service;
@Service
public class NodeServiceImpl  extends ServiceImpl<NodeDao, NodeEntity> implements NodeService{

    @Override
    public Pager<NodeEntity> search(String name, String regionId, long pageIndex, long pageSize) {
        Page<NodeEntity> page =  new Page<>(pageIndex,pageSize);
        LambdaQueryWrapper<NodeEntity> queryWrapper = new LambdaQueryWrapper<>();
        if(!Strings.isNullOrEmpty(name)){
            queryWrapper.like(NodeEntity::getName,name);
        }
        if(!Strings.isNullOrEmpty(regionId)){
            Long regionIdLong = Long.valueOf(regionId);
            queryWrapper.eq(NodeEntity::getRegionId,regionIdLong);
        }
        return Pager.build(this.page(page,queryWrapper));
    }

}
