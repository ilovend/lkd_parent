package com.lkd.http.controller;
import com.lkd.entity.VendingMachineEntity;
import com.lkd.entity.VmTypeEntity;
import com.lkd.feign.OrderService;
import com.lkd.http.vo.CreateVMReq;
import com.lkd.service.*;
import com.lkd.vo.Pager;
import com.lkd.vo.SkuVO;
import com.lkd.vo.VmVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vm")
public class VendingMachineController {

    @Autowired
    private VendingMachineService vendingMachineService;


    @Autowired
    private ChannelService channelService;

    @Autowired
    private VmTypeService vmTypeService;

    /**
     * 新增
     * @param vm
     * @return 是否成功
     */
    @PostMapping
    public boolean add(@RequestBody CreateVMReq vm){
        return vendingMachineService.add( vm );
    }



    /**
     * 根据id查询
     * @param id
     * @return 实体
     */
    @GetMapping("/{id}")
    public VendingMachineEntity findById(@PathVariable Long id){
        return vendingMachineService.getById(id);
    }

    /**
     * 修改点位
     * @param id
     * @param nodeId
     * @return 是否成功
     */
    @PutMapping("/{id}/{nodeId}")
    public boolean update(@PathVariable String id,@PathVariable String nodeId){
        return vendingMachineService.update(Long.valueOf(id),Long.valueOf(nodeId));
    }




    @GetMapping("/allTypes")
    public List<VmTypeEntity> getAllType(){
        return vmTypeService.list();
    }

    /**
     * 获取在运行的机器列表
     * @param isRunning
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/allByStatus/{isRunning}/{pageIndex}/{pageSize}")
    public Pager<String> getAllByStatus(@PathVariable boolean isRunning,
                                        @PathVariable long pageIndex,
                                        @PathVariable long pageSize){
        return vendingMachineService.getAllInnerCodes(isRunning,pageIndex,pageSize);
    }

    /**
     * 查询设备
     * @param pageIndex
     * @param pageSize
     * @param status
     * @return
     */
    @GetMapping("/search")
    public Pager<VendingMachineEntity> query(
            @RequestParam(value = "pageIndex",required = false,defaultValue = "1") Long pageIndex,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize,
            @RequestParam(value = "status",defaultValue = "",required = false) Integer status,
            @RequestParam(value = "innerCode",defaultValue = "",required = false) String innerCode){
        return vendingMachineService.query(pageIndex,pageSize,status,innerCode);
    }


    /**
     * 获取售货机信息
     * @param innerCode
     * @return
     */
    @GetMapping("/info/{innerCode}")
    public VmVO getByInnerCode(@PathVariable("innerCode") String innerCode){
        return vendingMachineService.findByInnerCode(innerCode);
    }


    /**
     * 获取售货机商品列表
     * @param innerCode
     * @return
     */
    @GetMapping("/skuList/{innerCode}")
    public List<SkuVO> getSkuListByInnerCode(@PathVariable String innerCode){
        return vendingMachineService.getSkuListByInnerCode(innerCode);
    }

    /**
     * 售货机商品是否还有库存
     * @param innerCode
     * @param skuId
     * @return
     */
    @GetMapping("/hasCapacity/{innerCode}/{skuId}")
    public Boolean hasCapacity(@PathVariable String innerCode,@PathVariable Long skuId){
        return vendingMachineService.hasCapacity(innerCode,skuId);
    }


    @Autowired
    private OrderService orderService;


    /**
     * 获取商圈下3个月内销量前10商品
     * @param businessId
     * @return
     */
    @GetMapping("/businessTop10/{businessId}")
    public List<Long> getBusinessTop10Skus(@PathVariable Integer businessId){
        return orderService.getBusinessTop10Skus(businessId);
    }


}
