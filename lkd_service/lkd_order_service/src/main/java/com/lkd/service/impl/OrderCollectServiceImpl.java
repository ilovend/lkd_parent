package com.lkd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lkd.dao.OrderCollectDao;
import com.lkd.entity.OrderCollectEntity;
import com.lkd.service.OrderCollectService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderCollectServiceImpl extends ServiceImpl<OrderCollectDao,OrderCollectEntity> implements OrderCollectService{
    @Override
    public List<OrderCollectEntity> getOwnerCollectByDate(Integer ownerId,LocalDate start,LocalDate end){
        QueryWrapper<OrderCollectEntity> qw = new QueryWrapper<>();
        qw.lambda()
                .eq(OrderCollectEntity::getOwnerId,ownerId)
                .ge(OrderCollectEntity::getDate,start)
                .le(OrderCollectEntity::getDate,end)
                .groupBy(OrderCollectEntity::getNodeName,OrderCollectEntity::getDate)
                .orderByDesc(OrderCollectEntity::getDate);

        return this.list(qw);
    }

}
