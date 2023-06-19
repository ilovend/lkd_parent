package com.lkd.business.handler;

import com.google.common.collect.Maps;
import com.lkd.business.MsgHandler;
import com.lkd.business.MsgHandlerContext;
import com.lkd.emq.Topic;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MsgHandlerContextImp implements ApplicationContextAware, MsgHandlerContext{
    private ApplicationContext ctx;
    private Map<String, MsgHandler> handlerMap = Maps.newHashMap();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
        Map<String,MsgHandler> map = ctx.getBeansOfType(MsgHandler.class);
        map.values().stream().forEach(v->{
            String topic =  v.getClass().getAnnotation(Topic.class).value();
            handlerMap.put(topic,v);
        });
    }

    public MsgHandler getMsgHandler(String topic){
        return handlerMap.get(topic);
    }
}
