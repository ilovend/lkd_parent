package com.lkd.http.controller;
import com.lkd.service.OrderService;
import com.lkd.utils.ConvertUtils;
import com.lkd.vo.OrderVO;
import com.lkd.vo.Pager;
import com.lkd.vo.PayVO;
import com.lkd.wxpay.WXConfig;
import com.lkd.wxpay.WxPayDTO;
import com.lkd.wxpay.WxPaySDKUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;


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
    @GetMapping("/search")
    public Pager<OrderVO> search(
            @RequestParam(value = "pageIndex",required = false,defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize,
            @RequestParam(value = "orderNo",required = false,defaultValue = "") String orderNo,
            @RequestParam(value = "openId",required = false,defaultValue = "") String openId,
            @RequestParam(value = "startDate",required = false,defaultValue = "") String startDate,
            @RequestParam(value = "endDate",required = false,defaultValue = "") String endDate){
        return orderService.search(pageIndex,pageSize,orderNo,openId,startDate,endDate);
    }


    /**
     * 获取商圈下3个月内销量前10商品
     * @param businessId
     * @return
     */
    @GetMapping("/businessTop10/{businessId}")
    public List<Long> getBusinessTop10Skus(@PathVariable Integer businessId){
        return orderService.getTop10Sku(businessId);
    }

}
