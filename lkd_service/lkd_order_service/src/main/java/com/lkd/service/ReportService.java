package com.lkd.service;

import com.lkd.entity.OrderCollectEntity;
import com.lkd.vo.BarCharVO;
import com.lkd.vo.Pager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {



    /**
     * 获取地区销量统计
     * @param start
     * @param end
     * @return
     */
    BarCharVO getCollectByRegion(LocalDate start, LocalDate end);


    /**
     * 获取合作商分账汇总信息
     * @param pageIndex
     * @param pageSize
     * @param name
     * @param start
     * @param end
     * @return
     */
    Pager<OrderCollectEntity> getPartnerCollect(Long pageIndex, Long pageSize, String name, LocalDateTime start, LocalDateTime end);



    /**
     * 获取合作商前12条点位分账数据
     * @param partnerId
     * @return
     */
    List<OrderCollectEntity> getTop12(Integer partnerId);

    /**
     * 合作商点位分账搜索
     * @param partnerId
     * @param nodeName
     * @param start
     * @param end
     * @return
     */
    Pager<OrderCollectEntity> search(Long pageIndex,Long pageSize,Integer partnerId, String nodeName, LocalDate start, LocalDate end);


    /**
     * 获取一定日期内合作商的收益统计
     * @param partnerId
     * @param start
     * @param end
     * @return
     */
    BarCharVO getCollect(Integer partnerId, LocalDate start, LocalDate end);




}
