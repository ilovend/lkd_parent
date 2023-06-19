package com.lkd.feign.fallback;
import com.google.common.collect.Lists;
import com.lkd.feign.VMService;
import com.lkd.vo.SkuVO;
import com.lkd.vo.VmVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class VmServiceFallbackFactory implements FallbackFactory<VMService> {
    @Override
    public VMService create(Throwable throwable) {
        log.error("调用售货机服务失败",throwable);
        return new VMService() {
            @Override
            public VmVO getVMInfo(String innerCode) {
                return null;
            }

            @Override
            public List<SkuVO> getSkuListByInnerCode(String innerCode) {
                return Lists.newArrayList();
            }

            @Override
            public SkuVO getSku(String skuId) {
                return null;
            }

            @Override
            public Boolean hasCapacity(String innerCode, Long skuId) {
                return null;
            }

            @Override
            public String getNodeName(Long id) {
                return null;
            }
        };
    }
}
