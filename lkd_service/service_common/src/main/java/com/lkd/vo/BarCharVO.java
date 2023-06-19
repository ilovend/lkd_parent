package com.lkd.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 柱状图
 */
@Data
public class BarCharVO implements Serializable {
    /**
     *  X轴
     */
    @JsonProperty("xAxis")
    private List<String> xAxis = Lists.newArrayList();
    /**
     *   数据
     */
    private List<Integer> series = Lists.newArrayList();
}