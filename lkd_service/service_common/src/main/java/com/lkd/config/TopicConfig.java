package com.lkd.config;

/**
 * 消息队列中的主题配置
 */
public class TopicConfig {

    /**
     * 完成运维工单主题
     */
    public final static String VMS_COMPLETED_TOPIC = "server/vms/completed";


    /**
     * 补货工单主题
     */
    public final static String VMS_SUPPLY_TOPIC = "server/vms/supply";

    /**
     * 状态上报主题
     */
    public final static String VMS_STATUS_TOPIC = "vms/status";


    /**
     * 通知出货主题
     */
    public final static String VMS_VENDOUT_TOPIC = "server/vms/vendout";

    /**
     * 出货结果主题（终端->服务端）
     */
    public final static String VMS_RESULT_TOPIC = "server/vms/result";

    /**
     * 发送到售货机终端 出货协议
     * @param innerCode
     * @return
     */
    public static String getVendoutTopic(String innerCode){
        return "vm/"+innerCode+"/vendout";
    }

    /**
     * 通知出货主题
     */
    public final static String TASK_SUPPLY_TOPIC = "server/task/supply";

    /**
     * 延迟订单主题
     */
    public final static String ORDER_CHECK_TOPIC = "server/order/check";



}
