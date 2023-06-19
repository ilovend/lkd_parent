package com.lkd.test;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.kv.model.PutParams;
import com.ecwid.consul.v1.session.model.NewSession;
import com.ecwid.consul.v1.session.model.Session;

import java.time.LocalDateTime;

public class ConsulDemo2 {

    public static void main(String[] args) {

        ConsulClient consulClient=new ConsulClient("192.168.200.128",8500) ;
        NewSession session = new NewSession();
        //session.setBehavior(Session.Behavior.RELEASE);
        session.setName("session-1");
        //session.setLockDelay(1);
        //session.setTtl(20 + "s"); //锁时长
        String   sessionId = consulClient.sessionCreate(session,null).getValue();
        System.out.println(sessionId);

        PutParams putParams = new PutParams();
        putParams.setAcquireSession(sessionId);//sessionID
        Boolean value = consulClient.setKVValue("session-1", "lock:" + LocalDateTime.now(), putParams).getValue();
        System.out.println(value);

    }

}
