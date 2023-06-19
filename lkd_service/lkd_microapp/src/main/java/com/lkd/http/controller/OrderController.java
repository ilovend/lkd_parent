package com.lkd.http.controller;
import com.google.common.base.Strings;
import com.lkd.config.WXConfig;
import com.lkd.exception.LogicException;
import com.lkd.feign.OrderService;
import com.lkd.utils.OpenIDUtil;
import com.lkd.vo.OrderVO;
import com.lkd.vo.Pager;
import com.lkd.vo.PayVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {


    @Autowired
    private WXConfig wxConfig;

    /**
     * 获取openId
     * @param jsCode
     * @return
     */
    @GetMapping("/openid/{jsCode}")
    public String getOpenid(@PathVariable("jsCode")  String jsCode){
        System.out.println("------------------"+jsCode);
        return OpenIDUtil.getOpenId( wxConfig.getAppId(),wxConfig.getAppSecret(),jsCode );
    }

    @Autowired
    private OrderService orderService;


    /**
     * 订单搜索
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


}
