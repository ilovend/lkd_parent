package com.lkd.contract;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseContract implements Serializable{
    /**
     * InnerCode售货机编号
     */
    private String innerCode;
}
