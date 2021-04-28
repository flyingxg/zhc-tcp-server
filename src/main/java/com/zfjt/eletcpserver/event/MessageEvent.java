package com.zfjt.eletcpserver.event;

import com.zfjt.eletcpserver.message.ZhcMessage;
import org.springframework.context.ApplicationEvent;

public class MessageEvent extends ApplicationEvent {
    private String rawMessage;
    private String sn;
    private ZhcMessage zhcMessage;

    public MessageEvent(Object source, String rawMessage, String sn,ZhcMessage zhcMessage) {
        super(source);
        this.rawMessage = rawMessage;
        this.sn = sn;
        this.zhcMessage = zhcMessage;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public String getSn() {
        return sn;
    }

    public ZhcMessage getMessage(){
        return zhcMessage;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "rawMessage='" + rawMessage + '\'' +
                ", sn='" + sn + '\'' +
                '}';
    }
}
