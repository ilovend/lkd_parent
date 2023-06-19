package com.lkd.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 小程序端支付请求对象
 */
@Data
public class PayVO implements Serializable {
    /**
     * 售货机编号
     */
    private String innerCode;

    /**
     * openId
     */
    private String openId;

    /**
     * 商品Id
     */
    private String skuId;
}