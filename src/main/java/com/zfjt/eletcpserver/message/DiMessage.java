package com.zfjt.eletcpserver.message;

import java.util.Date;

public class DiMessage extends ZhcMessage{
    public DiMessage(String sn, String rawMessage, Date createTime) {
        super(sn, rawMessage, createTime);
    }

    @Override
    public void parse() {
        super.parse();
    }
}
