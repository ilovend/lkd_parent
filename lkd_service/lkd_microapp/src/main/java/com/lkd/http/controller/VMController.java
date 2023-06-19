package com.lkd.http.controller;
import com.lkd.feign.VMService;
import com.lkd.vo.SkuVO;
import com.lkd.vo.VmVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vm")
public class VMController {

    @Autowired
    private VMService vmService;


    /**
     * 根据售货机编号查询设备
     * @param innerCode
     * @return
     */
    @GetMapping("/innerCode/{innerCode}")
    public VmVO getVm(@PathVariable String innerCode){
        return vmService.getVMInfo(innerCode);
    }

    /**
     * 获取售货机商品列表
     * @param innerCode
     * @return
     */
    @GetMapping("/skuList/{innerCode}")
    public List<SkuVO> getSkuListByInnercode(@PathVariable String innerCode){
        return vmService.getSkuListByInnerCode(innerCode);
    }


}
