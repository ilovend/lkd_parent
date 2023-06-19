package com.lkd.http.vo;

import lombok.Data;

@Data
public class SetChannelSkuReq{
    private String innerCode;
    private String channelCode;
    private String skuId;
}
