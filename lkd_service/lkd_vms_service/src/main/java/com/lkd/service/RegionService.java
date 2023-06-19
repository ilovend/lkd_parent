package com.lkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lkd.entity.RegionEntity;
import com.lkd.vo.Pager;

public interface RegionService extends IService<RegionEntity> {

    /**
     * 搜索
     * @param pageIndex
     * @param pageSize
     * @param name
     * @return
     */
    Pager<RegionEntity> search(Long pageIndex, Long pageSize, String name);


    /**
     * 获取区域详情
     * @param regionId
     * @return
     */
    RegionEntity findById(Long regionId);



}
