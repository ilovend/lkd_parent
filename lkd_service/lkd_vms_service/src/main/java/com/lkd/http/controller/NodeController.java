package com.lkd.http.controller;
import com.lkd.entity.NodeEntity;
import com.lkd.entity.VendingMachineEntity;
import com.lkd.exception.LogicException;
import com.lkd.http.vo.NodeReq;
import com.lkd.service.NodeService;
import com.lkd.service.VendingMachineService;
import com.lkd.vo.Pager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/node")
public class NodeController {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private VendingMachineService vendingMachineService;


    /**
     * 根据id查询
     * @param id
     * @return 实体
     */
    @GetMapping("/{id}")
    public NodeEntity findById(@PathVariable String id){
        return nodeService.getById(Long.valueOf(id));
    }

//    @GetMapping("/vmList/{id}")
//    public List<VendingMachineEntity> vmList(@PathVariable Long id) {
//        return vendingMachineService.getByNodeId(id);
//    }

    /**
     * 修改
     * @param id
     * @param req
     * @return 是否成功
     */
    @PutMapping("/{id}")
    public boolean update(@PathVariable String id,@RequestBody NodeReq req) throws LogicException {
        NodeEntity nodeEntity = new NodeEntity();
        BeanUtils.copyProperties( req,nodeEntity );
        nodeEntity.setRegionId(Long.valueOf(req.getRegionId()));
        nodeEntity.setId(Long.valueOf(id));

        return nodeService.updateById(nodeEntity);
    }

    /**
     * 删除
     * @param id
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public  boolean delete(@PathVariable String id){
        return nodeService.removeById( Long.valueOf(id) );
    }


    /**
     * 搜索点位
     * @param page
     * @param paseSize
     * @param name
     * @param regionId
     * @return
     */
    @GetMapping("/search")
    public Pager<NodeEntity> query(@RequestParam(value = "page",required = false,defaultValue = "1") Long page,
                                   @RequestParam(value = "pageSize",required = false,defaultValue = "10") Long paseSize,
                                   @RequestParam(value = "name",required = false) String name,
                                   @RequestParam(value = "regionId",required = false) String regionId
                                  ){
        return nodeService.search(name,regionId,page,paseSize);
    }

    /**
     * 创建点位
     * @param req
     * @return
     * @throws LogicException
     */
    @PostMapping
    public boolean createNode(@RequestBody NodeReq req) throws LogicException {
        NodeEntity nodeEntity = new NodeEntity();
        BeanUtils.copyProperties( req,nodeEntity );
        nodeEntity.setRegionId(Long.valueOf(req.getRegionId()));
        return nodeService.save(nodeEntity);
    }

    /**
     * 获取点位名称
     * @param id
     * @return
     */
    @GetMapping("/nodeName/{id}")
    public String getNodeName(@PathVariable Long id){
        return nodeService.getById(id).getName();
    }


}
