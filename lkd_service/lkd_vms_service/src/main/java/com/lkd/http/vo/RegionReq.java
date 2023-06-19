package com.lkd.http.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegionReq implements Serializable {
    private String regionName;
    private String remark;
}
