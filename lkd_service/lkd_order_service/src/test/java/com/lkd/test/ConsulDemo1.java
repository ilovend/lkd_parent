package com.lkd.test;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.kv.model.PutParams;
import com.ecwid.consul.v1.session.model.NewSession;
import com.ecwid.consul.v1.session.model.Session;

import java.time.LocalDateTime;

public class ConsulDemo1 {

    public static void main(String[] args) {

        ConsulClient consulClient=new ConsulClient("192.168.200.128",8500) ;
        PutParams putParams = new PutParams();
        putParams.setAcquireSession("7b7d013b-f10b-4011-7d46-55592b55ff01");//sessionID
        Boolean value = consulClient.setKVValue("session-1", "lock:" + LocalDateTime.now(), putParams).getValue();
        System.out.println(value);

    }

}
