package com.lkd.contract;

import lombok.Data;

@Data
public class VendoutData{
    /**
     * 商品Id
     */
    private long skuId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 货道编号
     */
    private String channelCode;

}
