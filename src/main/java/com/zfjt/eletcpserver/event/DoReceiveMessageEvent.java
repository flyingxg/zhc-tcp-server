package com.zfjt.eletcpserver.event;

import cn.hutool.core.util.HexUtil;
import com.zfjt.eletcpserver.message.DoMessage;
import com.zfjt.eletcpserver.message.DoReceiveMessage;

public class DoReceiveMessageEvent extends MessageEvent{

    public DoReceiveMessageEvent(Object source, String rawMessage, String sn, DoReceiveMessage zhcMessage) {
        super(source, rawMessage, sn, zhcMessage);
    }

    @Override
    public DoReceiveMessage getMessage() {
        return (DoReceiveMessage) super.getMessage();
    }

    @Override
    public String toString() {
        return "DoReceiveMessageEvent{" +
                "source=" + source + ",message="+this.getMessage()+
                '}';
    }
}
