package com.lkd.contract;
import lombok.Data;
/**
 * 完成工单协议
 */
@Data
public class TaskCompleteContract extends BaseContract {
    private int taskType;//工单类型
}