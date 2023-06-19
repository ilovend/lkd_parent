package com.lkd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lkd.dao.ChannelDao;
import com.lkd.entity.ChannelEntity;
import com.lkd.entity.SkuEntity;
import com.lkd.exception.LogicException;
import com.lkd.http.vo.VMChannelConfig;
import com.lkd.service.ChannelService;
import com.lkd.service.SkuService;
import com.lkd.vo.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ChannelServiceImpl extends ServiceImpl<ChannelDao,ChannelEntity> implements ChannelService{


    @Override
    public Pager<ChannelEntity> findPage(long pageIndex, long pageSize, Map searchMap) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ChannelEntity> page =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageIndex,pageSize);

        QueryWrapper queryWrapper = createQueryWrapper( searchMap );
        this.page(page,queryWrapper);

        Pager<ChannelEntity> pageResult = new Pager<>();
        pageResult.setCurrentPageRecords(page.getRecords());
        pageResult.setPageIndex(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotalCount(page.getTotal());
        return pageResult;
    }

    @Override
    public List<ChannelEntity> getChannelesByInnerCode(String innerCode) {
        LambdaQueryWrapper<ChannelEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChannelEntity::getInnerCode,innerCode);

        return this.list(queryWrapper);
    }

    @Override
    public ChannelEntity getChannelInfo(String innerCode, String channelCode) {
        LambdaQueryWrapper<ChannelEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ChannelEntity::getInnerCode,innerCode)
                .eq(ChannelEntity::getChannelCode,channelCode);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<ChannelEntity> findList(Map searchMap) {
        QueryWrapper queryWrapper = createQueryWrapper( searchMap );

        return this.list(queryWrapper);
    }

    /**
     * 条件构建
     * @param searchMap
     * @return
     */
    private QueryWrapper createQueryWrapper(Map searchMap){
        QueryWrapper queryWrapper=new QueryWrapper(  );
        if(searchMap!=null){
            queryWrapper.allEq(searchMap);
        }
        return queryWrapper;
    }

    @Autowired
    private SkuService skuService;

    /**
     * 设置商品
     * @param innerCode
     * @param channelCode
     * @param skuId
     * @return
     */
    private boolean configSku(String innerCode, String channelCode, long skuId) {

        if(skuId<=0){
            return false;
        }
        SkuEntity skuEntity = skuService.getById(skuId); //查询商品
        if(skuEntity==null){
            return false;
        }

        ChannelEntity channel = this.getChannelInfo(innerCode,channelCode);
        if(channel == null){
            throw new LogicException("该货道不存在");
        }
        channel.setSkuId(skuId);
        channel.setPrice(skuEntity.getPrice());
        return this.updateById(channel);
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean mapSku(VMChannelConfig channelConfig) {
        channelConfig.getChannelList().forEach(c->
                configSku(channelConfig.getInnerCode(),c.getChannelCode(),Long.valueOf(c.getSkuId())));
        return true;
    }


}
