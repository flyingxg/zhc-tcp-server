package com.zfjt.eletcpserver.communicate;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.zfjt.eletcpserver.message.DoReceiveMessage;
import com.zfjt.eletcpserver.netty.ChannelRepository;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ClientFlag {

    @Autowired
    private ChannelRepository channelRepository;

    // 用来存设备号和发出的指令 服务端发送时存入  如果指令正确执行 客户端回显时remove
    public static ConcurrentMap<String,String> clientFlagMap = new ConcurrentHashMap<>();


    // 设备注册池 连接上但是没有发过数据的
    private static ConcurrentHashSet<String> notRegisterPool = new ConcurrentHashSet<>();


    public static boolean hasRegistered(Channel ctx){
        return !notRegisterPool.contains(ctx.id().toString());
    }


    public boolean hasRegistered(ChannelHandlerContext ctx){
        return !notRegisterPool.contains(ctx.channel().id().toString());
    }

    public void registered(ChannelHandlerContext ctx, String sn){
        if(sn.length() != 16){
            // 字节码长度非法 踢下线
            notRegisterPool.remove(ctx.channel().id().toString());
            ctx.close();
            return;
        }
        channelRepository.put(sn, ctx.channel());
        notRegisterPool.remove(ctx.channel().id().toString());
        ctx.writeAndFlush(DoReceiveMessage.askStatusCommand);
    }

    public void addToNotRegisterPool(ChannelHandlerContext ctx){
        notRegisterPool.add(ctx.channel().id().toString());
    }
}
