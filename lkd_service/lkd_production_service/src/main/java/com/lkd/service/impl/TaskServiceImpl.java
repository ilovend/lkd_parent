package com.lkd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.lkd.common.VMSystem;
import com.lkd.dao.TaskDao;
import com.lkd.entity.TaskEntity;
import com.lkd.service.TaskService;
import com.lkd.vo.Pager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class TaskServiceImpl extends ServiceImpl<TaskDao,TaskEntity> implements TaskService{


    @Override
    public Pager<TaskEntity> search(Long pageIndex, Long pageSize, String innerCode, Integer userId, String taskCode, Integer status, Boolean isRepair, String start, String end) {
        Page<TaskEntity> page = new Page<>(pageIndex,pageSize);
        LambdaQueryWrapper<TaskEntity> qw = new LambdaQueryWrapper<>();
        if(!Strings.isNullOrEmpty(innerCode)){
            qw.eq(TaskEntity::getInnerCode,innerCode);
        }
        if(userId != null && userId > 0){
            qw.eq(TaskEntity::getUserId,userId);
        }
        if(!Strings.isNullOrEmpty(taskCode)){
            qw.like(TaskEntity::getTaskCode,taskCode);
        }
        if(status != null && status > 0){
            qw.eq(TaskEntity::getTaskStatus,status);
        }
        if(isRepair != null){
            if(isRepair){
                qw.ne(TaskEntity::getProductTypeId,VMSystem.TASK_TYPE_SUPPLY);
            }else {
                qw.eq(TaskEntity::getProductTypeId,VMSystem.TASK_TYPE_SUPPLY);
            }
        }
        if(!Strings.isNullOrEmpty(start) && !Strings.isNullOrEmpty(end)){
            qw
                    .ge(TaskEntity::getCreateTime,LocalDate.parse(start,DateTimeFormatter.ISO_LOCAL_DATE))
                    .le(TaskEntity::getCreateTime,LocalDate.parse(end,DateTimeFormatter.ISO_LOCAL_DATE));
        }
        //根据最后更新时间倒序排序
        qw.orderByDesc(TaskEntity::getUpdateTime);

        return Pager.build(this.page(page,qw));
    }

}
