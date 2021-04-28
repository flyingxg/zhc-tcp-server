package com.zfjt.eletcpserver.message;

import cn.hutool.core.util.HexUtil;

import java.math.BigInteger;
import java.util.Date;

public class DoReceiveMessage extends ZhcMessage {

    private Boolean do1;
    private Boolean do2;
    private Boolean do3;
    private Boolean do4;
    // 询问继电器状态指令
    public static byte[] askStatusCommand = HexUtil.decodeHex("55 01 00 00 00 04 30 1D");

    public DoReceiveMessage(String sn, String rawMessage, Date createTime) {
        super(sn, rawMessage, createTime);
        parse();
    }

    public Boolean getDo1() {
        return do1;
    }

    public void setDo1(Boolean do1) {
        this.do1 = do1;
    }

    public Boolean getDo2() {
        return do2;
    }

    public void setDo2(Boolean do2) {
        this.do2 = do2;
    }

    public Boolean getDo3() {
        return do3;
    }

    public void setDo3(Boolean do3) {
        this.do3 = do3;
    }

    public Boolean getDo4() {
        return do4;
    }

    public void setDo4(Boolean do4) {
        this.do4 = do4;
    }

    public DoReceiveMessage() {
        super();
    }

    @Override
    public void parse() {
        String rawMessage = getRawMessage();
        String format = HexUtil.format(rawMessage);
        String[] s = format.split(" ");
        String status = s[3];
        int i = HexUtil.toBigInteger(status).intValue();
        this.do4 = (i & 8) == 8;
        this.do3 = (i & 4) == 4;
        this.do2 = (i & 2) == 2;
        this.do1 = (i & 1) == 1;
    }

    @Override
    public String toString() {
        return "DoReceiveMessage{" +
                "do1=" + do1 +
                ", do2=" + do2 +
                ", do3=" + do3 +
                ", do4=" + do4 +
                '}';
    }
}
