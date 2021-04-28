package com.zfjt.eletcpserver.parse;

import cn.hutool.core.util.HexUtil;
import com.zfjt.eletcpserver.message.*;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

public class ParseUtil {
    public static ZhcMessage convertMessage(String rawMessage,String sn){
        rawMessage = rawMessage.replace(" ", "");
        Date receiveTime = new Date();
        if(rawMessage.startsWith("0005")){
            return new DoMessage(sn,rawMessage,receiveTime);
        }

        if(rawMessage.startsWith("5501")){
            return new DoReceiveMessage(sn,rawMessage,receiveTime);
        }
        if(rawMessage.startsWith("5502")){
            return new DiMessage(sn,rawMessage,receiveTime);
        }
        if(rawMessage.startsWith("5504")){
            return new AiMessage(sn,rawMessage,receiveTime);
        }
        return new ZhcMessage(sn,rawMessage,receiveTime);
    }

    public static String hexStringToString(String s) throws UnsupportedEncodingException {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
}
