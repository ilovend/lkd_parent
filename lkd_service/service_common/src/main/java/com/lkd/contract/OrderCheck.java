package com.lkd.contract;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderCheck extends BaseContract implements Serializable {
    private String orderNo;
}