package com.lkd.test;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.session.model.NewSession;
import com.ecwid.consul.v1.session.model.Session;

public class ConsulDemo {

    public static void main(String[] args) {

        DistributedLock1 distributedLock1=new DistributedLock1("192.168.200.128",8500);
        DistributedLock1.LockContext abc = distributedLock1.getLock("abc", 15);

        System.out.println(abc.isGetLock());

    }
}
