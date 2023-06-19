package com.lkd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lkd.entity.VendingMachineEntity;
import com.lkd.http.vo.CreateVMReq;
import com.lkd.vo.Pager;
import com.lkd.vo.SkuVO;
import com.lkd.vo.VmVO;

import java.util.List;

public interface VendingMachineService extends IService<VendingMachineEntity> {

    /**
     * 根据nodeId查询设备列表
     *
     * @param nodeId
     * @return
     */
    List<VendingMachineEntity> getByNodeId(Long nodeId);


    /**
     * 新增
     * @param vendingMachine
     * @return
     */
    boolean add(CreateVMReq vendingMachine);

    /**
     * 修改售货机点位
     * @param id
     * @param nodeId
     * @return
     */
    boolean update(Long id,Long nodeId);

    /**
     * 根据机器状态获取机器编号列表
     * @param isRunning
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Pager<String> getAllInnerCodes(boolean isRunning, long pageIndex, long pageSize);

    /**
     * 根据状态获取售货机列表
     * @param status
     * @return
     */
    Pager<VendingMachineEntity> query(Long pageIndex, Long pageSize, Integer status,String innerCode);


    /**
     * 根据售货机编号查找
     * @param innerCode
     * @return
     */
    VmVO findByInnerCode(String innerCode);

    /**
     * 更改售货机状态
     * @param innerCode
     * @param vmStatus
     */
    boolean updateStatus(String innerCode, Integer vmStatus);


    /**
     * 根据售货机编号查询商品列表
     * @param innerCode
     * @return
     */
    List<SkuVO> getSkuListByInnerCode(String innerCode);


    /**
     * 商品是否还有余量
     * @param skuId
     * @return
     */
    Boolean hasCapacity(String innerCode,Long skuId);

}
