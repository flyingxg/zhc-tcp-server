package com.zfjt.eletcpserver.event;

import com.zfjt.eletcpserver.message.DiMessage;

public class DiMessageEvent extends MessageEvent{
    public DiMessageEvent(Object source, String rawMessage, String sn, DiMessage zhcMessage) {
        super(source, rawMessage, sn, zhcMessage);
    }

    @Override
    public DiMessage getMessage() {
        return (DiMessage) super.getMessage();
    }
}
