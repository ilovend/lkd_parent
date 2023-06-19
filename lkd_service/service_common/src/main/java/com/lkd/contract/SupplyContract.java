package com.lkd.contract;

import lombok.Data;

import java.util.List;
@Data
public class SupplyContract extends BaseContract {
    /**
     * 补货数据
     */
    private List<SupplyChannel> supplyData;


}
