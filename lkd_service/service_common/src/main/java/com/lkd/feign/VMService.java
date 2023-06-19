package com.lkd.feign;

import com.lkd.feign.fallback.VmServiceFallbackFactory;
import com.lkd.vo.SkuVO;
import com.lkd.vo.VmVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "vm-service",fallbackFactory = VmServiceFallbackFactory.class)
public interface VMService{

    /**
     * 根据售货机编号查询售货机
     * @param innerCode
     * @return
     */
    @GetMapping("/vm/info/{innerCode}")
    VmVO getVMInfo(@PathVariable String innerCode);

    /**
     * 根据售货机编号查询售货机商品列表
     * @param innerCode
     * @return
     */
    @GetMapping("/vm/skuList/{innerCode}")
    List<SkuVO> getSkuListByInnerCode(@PathVariable String innerCode);

    /**
     * 根据商品id查询商品
     * @param skuId
     * @return
     */
    @GetMapping("/sku/{skuId}")
    SkuVO getSku(@PathVariable String skuId);


    /**
     * 查询某售货机是否存在某商品
     * @param innerCode
     * @param skuId
     * @return
     */
    @GetMapping("/vm/hasCapacity/{innerCode}/{skuId}")
    Boolean hasCapacity(@PathVariable String innerCode,@PathVariable Long skuId);

    /**
     * 获取点位名称
     * @param id
     * @return
     */
    @GetMapping("/node/nodeName/{id}")
    String getNodeName(@PathVariable Long id);


}
