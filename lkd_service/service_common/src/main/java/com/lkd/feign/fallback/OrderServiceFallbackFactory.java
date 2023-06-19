package com.lkd.feign.fallback;
import com.google.common.collect.Lists;
import com.lkd.feign.OrderService;
import com.lkd.vo.OrderVO;
import com.lkd.vo.Pager;
import com.lkd.vo.PayVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OrderServiceFallbackFactory implements FallbackFactory<OrderService> {
    @Override
    public OrderService create(Throwable throwable) {
        log.error("调用订单服务失败",throwable);

        return new OrderService() {

            @Override
            public Pager<OrderVO> search(Integer pageIndex, Integer pageSize, String orderNo, String openId, String startDate, String endDate) {
                return new Pager<>();
            }

            @Override
            public List<Long> getBusinessTop10Skus(Integer businessId) {
                return Lists.newArrayList();
            }
        };
    }
}