package com.zfjt.eletcpserver.event;

import com.zfjt.eletcpserver.message.DoMessage;

public class DoMessageEvent extends MessageEvent{
    public DoMessageEvent(Object source, String rawMessage, String sn, DoMessage zhcMessage) {
        super(source, rawMessage, sn, zhcMessage);
    }

    @Override
    public DoMessage getMessage() {
        return (DoMessage) super.getMessage();
    }
}
