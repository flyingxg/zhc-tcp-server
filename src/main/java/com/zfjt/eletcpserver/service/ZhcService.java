package com.zfjt.eletcpserver.service;

import cn.hutool.core.util.HexUtil;
import com.zfjt.eletcpserver.communicate.ClientFlag;
import com.zfjt.eletcpserver.event.ZHCEventListener;
import com.zfjt.eletcpserver.message.DoMessage;
import com.zfjt.eletcpserver.message.DoReceiveMessage;
import com.zfjt.eletcpserver.netty.ChannelRepository;
import com.zfjt.eletcpserver.parse.ParseUtil;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZhcService {
    final int MAX_WAIT_TIMES = 100;
    final int SLEEP_TIME = 200;


    @Autowired
    ChannelRepository channelRepository;
    public DoReceiveMessage getDoStatus(String sn) throws InterruptedException {
        ZHCEventListener.rtuStatus.put(sn,new DoReceiveMessage());
        sendCommand(DoReceiveMessage.askStatusCommand,sn);
        DoReceiveMessage doReceiveMessage = ZHCEventListener.rtuStatus.get(sn);
        ZHCEventListener.rtuStatus.remove(sn);
        return doReceiveMessage;
    }

    public String control(String command,String sn) throws InterruptedException {
        DoMessage doMessage = null;
        if("1F".equals(command)){
             doMessage = new DoMessage(true,null,null,null);
        }
        if("2F".equals(command)){
             doMessage = new DoMessage(null,true,null,null);
        }
        if("3F".equals(command)){
             doMessage = new DoMessage(null,null,true,null);
        }
        if("4F".equals(command)){
             doMessage = new DoMessage(null,null,null,true);
        }
        if("10".equals(command)){
             doMessage = new DoMessage(false,null,null,null);
        }
        if("20".equals(command)){
             doMessage = new DoMessage(null,false,null,null);
        }
        if("30".equals(command)){
             doMessage = new DoMessage(null,null,false,null);
        }
        if("40".equals(command)){
             doMessage = new DoMessage(null,null,null,false);
        }
        if(doMessage == null){
            return "failed";
        }
        return sendCommand(doMessage.toBytes(),sn);
    }


    public String sendCommand(byte[] command,String sn) throws InterruptedException {
        Channel snChannel = channelRepository.getSnChannel(sn);
        if(snChannel == null || !snChannel.isActive()){
            return "offline";
        }

        boolean active = snChannel.isActive();
        System.out.println(active);
        snChannel.writeAndFlush(command);
        ClientFlag.clientFlagMap.put(sn,HexUtil.encodeHexStr(command).substring(0,4));

        int i = 0;
        while (true){
            Thread.sleep(SLEEP_TIME);
            if(ClientFlag.clientFlagMap.get(sn) == null){
                return "success";
            }
            i++;
            if(i > MAX_WAIT_TIMES){
                break;
            }
        }
        ClientFlag.clientFlagMap.remove(sn);
        return "failed";
    }
}
