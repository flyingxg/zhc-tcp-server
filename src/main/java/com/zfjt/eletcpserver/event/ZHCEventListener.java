package com.zfjt.eletcpserver.event;

import cn.hutool.json.JSONUtil;
import com.zfjt.eletcpserver.communicate.ClientFlag;
import com.zfjt.eletcpserver.message.DoReceiveMessage;
import com.zfjt.eletcpserver.rabbitmq.producer.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ZHCEventListener implements ApplicationListener {

    @Autowired
    private Producer producer;

    public static ConcurrentHashMap<String,DoReceiveMessage> rtuStatus = new ConcurrentHashMap<>();



    /**
     * AI采集
     * @param aiMessageEvent
     */
    @EventListener(AiMessageEvent.class)
    public void onAiMessageReceive(AiMessageEvent aiMessageEvent){
        log.debug("AiMessageEvent:{}",aiMessageEvent);
    }


    /**
     * DI采集
     * @param diMessageEvent
     */
    @EventListener(DiMessageEvent.class)
    public void onDiMessageReceive(DiMessageEvent diMessageEvent){
        log.debug("DiMessageEvent:{}",diMessageEvent);
    }


    /**
     * DO控制回显
     * @param doMessageEvent
     */
    @EventListener(DoMessageEvent.class)
    public void onDiMessageReceive(DoMessageEvent doMessageEvent){
        log.debug("DoMessageEvent:{}",doMessageEvent);
    }


    /**
     * 继电器状态上报
     * @param doReceiveMessage
     */
    @EventListener(DoReceiveMessageEvent.class)
    public void onDoReceiveMessageReceive(DoReceiveMessageEvent doReceiveMessage){
        if (rtuStatus.containsKey(doReceiveMessage.getSn())) {
            rtuStatus.put(doReceiveMessage.getSn(),doReceiveMessage.getMessage());
        }
        String jsonStr = JSONUtil.toJsonStr(doReceiveMessage);
        log.debug("DoReceiveMessage:{}",jsonStr);
    }



    /**
     * 原始信息事件
     * @param messageEvent
     */
    @Order(0)
    @EventListener(MessageEvent.class)
    public void onMessage(MessageEvent messageEvent){
        if(ClientFlag.clientFlagMap.get(messageEvent.getSn()) != null){
            if(ClientFlag.clientFlagMap.get(messageEvent.getSn()).equals(messageEvent.getRawMessage().substring(0,4))){
                ClientFlag.clientFlagMap.remove(messageEvent.getSn());
            }
        }
        String jsonStr = JSONUtil.toJsonStr(messageEvent.getMessage());
        producer.send(jsonStr);
        log.debug("rawMessage:{}",jsonStr);
    }


    @Override
    public void onApplicationEvent(ApplicationEvent event) {

    }
}
