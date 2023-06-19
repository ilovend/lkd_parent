package com.lkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lkd.vo.PayVO;
import com.lkd.entity.OrderEntity;
import com.lkd.vo.OrderVO;
import com.lkd.vo.Pager;
import com.lkd.wxpay.WxPayDTO;

import java.util.List;

public interface OrderService extends IService<OrderEntity> {


    /**
     * 通过订单编号获取订单实体
     * @param orderNo
     * @return
     */
    OrderEntity getByOrderNo(String orderNo);


    /**
     * 查询订单
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Pager<OrderVO> search(Integer pageIndex, Integer pageSize, String orderNo, String openId, String startDate, String endDate);


    /**
     * 获取商圈下销量最好的前10商品
     * @return
     */
    List<Long> getTop10Sku(Integer businessId);

}
