package com.lkd.test;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.kv.model.PutParams;
import com.ecwid.consul.v1.session.model.NewSession;
import com.ecwid.consul.v1.session.model.Session;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.*;

@Slf4j
public class DistributedLock1 {
    private ConsulClient consulClient;
    private String sessionId;

    /**
     *
     * @param consulHost consul的Agent主机名或IP
     * @param consulPort 端口
     */
    public DistributedLock1(String consulHost, int consulPort){
        consulClient = new ConsulClient(consulHost,consulPort);
    }

    /**
     * 获取锁
     * @param lockName 锁的名称(key)
     * @param ttlSeconds 锁的超时时间
     * @return
     */
    public LockContext getLock(String lockName,int ttlSeconds){
        LockContext lockContext = new LockContext();
        if(ttlSeconds<10 || ttlSeconds > 86400) ttlSeconds = 60;
        String sessionId = createSession(lockName,ttlSeconds);
        boolean success = lock(lockName,sessionId);
        if(!success){
            consulClient.sessionDestroy(sessionId,null);
            lockContext.setGetLock(false);
            return lockContext;
        }
        lockContext.setSession(sessionId);
        lockContext.setGetLock(true);

        return lockContext;
    }

    public void releaseLock(String sessionId){
        consulClient.sessionDestroy(sessionId,null);
    }

    private String createSession(String lockName,int ttlSeconds){
        NewSession session = new NewSession();
        session.setBehavior(Session.Behavior.DELETE);
        session.setName("session-"+lockName);
        session.setLockDelay(1);
        session.setTtl((ttlSeconds+5) + "s"); //锁时长
        sessionId = consulClient.sessionCreate(session,null).getValue();

        return sessionId;
    }


    private boolean lock(String lockName,String sessionId){
        PutParams putParams = new PutParams();
        putParams.setAcquireSession(sessionId);

        return consulClient.setKVValue(lockName,"lock:"+ LocalDateTime.now(),putParams).getValue();
    }

    /**
     * 锁上下文对象
     */
    @Data
    public class LockContext{
        private String session;
        private boolean isGetLock;
    }
}
