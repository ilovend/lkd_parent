package com.lkd.http.controller;
import com.lkd.entity.ChannelEntity;
import com.lkd.http.vo.VMChannelConfig;
import com.lkd.service.ChannelService;
import com.lkd.vo.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;


    /**
     * 分页查询
     * @param pageIndex 页码
     * @param pageSize 页大小
     * @param searchMap 条件
     * @return 分页结果
     */
    @GetMapping("/page/{pageIndex}/{pageSize}")
    public Pager<ChannelEntity> findPage(@PathVariable long pageIndex, @PathVariable long pageSize, @RequestParam Map searchMap){
        return channelService.findPage( pageIndex,pageSize,searchMap );
    }

    @GetMapping("/channelList/{innerCode}")
    public List<ChannelEntity> getChannelList(@PathVariable("innerCode") String innerCode){
        return channelService.getChannelesByInnerCode(innerCode);
    }

    @GetMapping("/channelInfo/{innerCode}/{channelCode}")
    public ChannelEntity getChannelInfo(@PathVariable("innerCode") String innerCode,@PathVariable("channelCode") String channelCode){
        return channelService.getChannelInfo(innerCode,channelCode);
    }

    /**
     * 货道配置
     * @param channelConfig
     * @return
     */
    @PutMapping("/channelConfig")
    public Boolean setChannel(@RequestBody VMChannelConfig channelConfig){
        return channelService.mapSku(channelConfig);
    }


}
