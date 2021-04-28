package com.zfjt.eletcpserver.message;

import java.util.Date;

public class ZhcMessage {
    private String sn;
    private String rawMessage;
    private Date createTime;

    public ZhcMessage(String sn, String rawMessage, Date createTime) {
        this.sn = sn;
        this.rawMessage = rawMessage;
        this.createTime = createTime;
    }

    public ZhcMessage() {

    }


    public void parse(){

    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
