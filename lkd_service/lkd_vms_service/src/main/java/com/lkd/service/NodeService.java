package com.lkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lkd.entity.NodeEntity;
import com.lkd.vo.Pager;

public interface NodeService extends IService<NodeEntity> {


    /**
     * 搜索点位
     * @param name
     * @param regionId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Pager<NodeEntity> search(String name, String regionId, long pageIndex, long pageSize);


}
