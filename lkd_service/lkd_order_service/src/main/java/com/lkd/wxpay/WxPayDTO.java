package com.lkd.wxpay;

import lombok.Data;

@Data
public class WxPayDTO {

    private String body;//商品描述
    private String outTradeNo; //订单号
    private int totalFee; //订单金额
    private String openid;
    private int expireSecond;//过期秒数

}
