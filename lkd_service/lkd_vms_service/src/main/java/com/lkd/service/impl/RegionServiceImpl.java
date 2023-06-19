package com.lkd.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.lkd.dao.RegionDao;
import com.lkd.entity.NodeEntity;
import com.lkd.entity.RegionEntity;
import com.lkd.service.NodeService;
import com.lkd.service.RegionService;
import com.lkd.vo.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegionServiceImpl extends ServiceImpl<RegionDao, RegionEntity> implements RegionService {

    @Override
    public Pager<RegionEntity> search(Long pageIndex, Long pageSize, String name) {
        Page<RegionEntity> page = new Page<>(pageIndex,pageSize);
        LambdaQueryWrapper<RegionEntity> wrapper = new LambdaQueryWrapper<>();
        if(!Strings.isNullOrEmpty(name)){
            wrapper.like(RegionEntity::getName,name);
        }
        this.page(page,wrapper);

        return Pager.build(page);
    }

    @Autowired
    private NodeService nodeService;

    @Override
    public RegionEntity findById(Long regionId) {
        RegionEntity regionEntity = this.getById(regionId);
        var qw = new LambdaQueryWrapper<NodeEntity>();
        qw.eq(NodeEntity::getRegionId,regionId);
        regionEntity.setNodeList(nodeService.list(qw));
        return regionEntity;
    }



}
