package com.lkd.contract;

import lombok.Data;

/**
 * 出货请求协议
 */
@Data
public class VendoutContract extends BaseContract{
    private VendoutData vendoutData;
}
