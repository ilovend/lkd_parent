package com.lkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lkd.entity.TaskEntity;
import com.lkd.vo.Pager;

/**
 * 工单业务逻辑
 */
public interface TaskService extends IService<TaskEntity> {

    /**
     * 通过条件搜索工单列表
     * @param pageIndex
     * @param pageSize
     * @param innerCode
     * @param userId
     * @param taskCode
     * @param isRepair 是否是运维工单
     * @return
     */
    Pager<TaskEntity> search(Long pageIndex, Long pageSize, String innerCode, Integer userId, String taskCode, Integer status, Boolean isRepair, String start, String end);

}
