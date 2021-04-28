package com.zfjt.eletcpserver.event;

import com.zfjt.eletcpserver.message.AiMessage;

public class AiMessageEvent extends MessageEvent{
    public AiMessageEvent(Object source, String rawMessage, String sn, AiMessage zhcMessage) {
        super(source, rawMessage, sn, zhcMessage);
    }

    @Override
    public AiMessage getMessage() {
        return (AiMessage) super.getMessage();
    }
}
