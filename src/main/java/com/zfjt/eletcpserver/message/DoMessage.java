package com.zfjt.eletcpserver.message;

import cn.hutool.core.util.HexUtil;

import java.util.Date;

public class DoMessage extends ZhcMessage{

    // 开 00 05 00 00 FF 00 8D EB
    // 关 00 05 00 00 00 00 CC 1B
    Boolean do1;
    // 开 00 05 00 01 FF 00 DC 2B
    // 关 00 05 00 01 00 00 9D DB
    Boolean do2;
    // 开 00 05 00 02 FF 00 2C 2B
    // 关 00 05 00 02 00 00 6D DB
    Boolean do3;
    // 开 00 05 00 03 FF 00 7D EB
    // 关 00 05 00 03 00 00 3C 1B
    Boolean do4;


    public DoMessage(Boolean do1, Boolean do2, Boolean do3, Boolean do4) {
        this.do1 = do1;
        this.do2 = do2;
        this.do3 = do3;
        this.do4 = do4;
    }

    @Override
    public String toString() {
        return "DoMessage{" +
                "do1=" + do1 +
                ", do2=" + do2 +
                ", do3=" + do3 +
                ", do4=" + do4 +
                '}';
    }

    public DoMessage(String sn, String rawMessage, Date createTime) {
        super(sn, rawMessage, createTime);
    }

    public byte[] toBytes(){
        if(do1!= null && true == do1){
            return HexUtil.decodeHex("00 05 00 00 FF 00 8D EB");
        }
        if(do1!= null &&false == do1){
            return HexUtil.decodeHex("00 05 00 00 00 00 CC 1B");
        }
        if(do2!= null &&true == do2){
            return HexUtil.decodeHex("00 05 00 01 FF 00 DC 2B");
        }
        if(do2!= null &&false == do2){
            return HexUtil.decodeHex("00 05 00 01 00 00 9D DB");
        }
        if(do3!= null &&true == do3){
            return HexUtil.decodeHex("00 05 00 02 FF 00 2C 2B");
        }
        if(do3!= null &&false == do3){
            return HexUtil.decodeHex("00 05 00 02 00 00 6D DB");
        }
        if(do4!= null &&true == do4){
            return HexUtil.decodeHex("00 05 00 03 FF 00 7D EB");
        }
        if(do4!= null &&false == do4){
            return HexUtil.decodeHex("00 05 00 03 00 00 3C 1B");
        }
        return null;
    }

    @Override
    public void parse() {
        super.parse();
    }
}
