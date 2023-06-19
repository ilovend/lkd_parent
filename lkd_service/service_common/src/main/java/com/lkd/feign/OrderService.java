package com.lkd.feign;
import com.lkd.feign.fallback.OrderServiceFallbackFactory;
import com.lkd.vo.OrderVO;
import com.lkd.vo.Pager;
import com.lkd.vo.PayVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "order-service",fallbackFactory = OrderServiceFallbackFactory.class)
public interface OrderService {


    /**
     * 订单查询
     * @param pageIndex
     * @param pageSize
     * @param orderNo
     * @param openId
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/order/search")
    Pager<OrderVO> search(
            @RequestParam(value = "pageIndex",required = false,defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize,
            @RequestParam(value = "orderNo",required = false,defaultValue = "") String orderNo,
            @RequestParam(value = "openId",required = false,defaultValue = "") String openId,
            @RequestParam(value = "startDate",required = false,defaultValue = "") String startDate,
            @RequestParam(value = "endDate",required = false,defaultValue = "") String endDate);



    /**
     * 获取商圈下3个月内销量前10商品
     * @param businessId
     * @return
     */
    @GetMapping("/order/businessTop10/{businessId}")
    public List<Long> getBusinessTop10Skus(@PathVariable Integer businessId);

}