package com.lkd.contract;

import lombok.Data;

/**
 * 出货请求协议
 */
@Data
public class VendoutResultContract extends BaseContract{

    /**
     * 是否成功（终端响应数据）
     */
    private boolean success;

    private VendoutData vendoutData;
}
