package com.lkd.http.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TaskViewModel implements Serializable{
    /**
     * 工单类型
     */
    private int createType;
    /**
     * 关联设备编号
     */
    private String innerCode;
    /**
     * 任务执行人Id
     */
    private Integer userId;

    /**
     * 指派人id
     */
    private Integer assignorId;

    /**
     * 工单类型
     */
    private int productType;
    /**
     * 描述信息
     */
    private String desc;
    /**
     * 工单详情(只有补货工单才涉及)
     */
    private List<TaskDetailsViewModel> details;
}
