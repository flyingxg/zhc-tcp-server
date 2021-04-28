/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zfjt.eletcpserver.netty.handler;

import cn.hutool.core.util.HexUtil;
import com.zfjt.eletcpserver.*;
import com.zfjt.eletcpserver.communicate.ClientFlag;
import com.zfjt.eletcpserver.event.*;
import com.zfjt.eletcpserver.message.*;
import com.zfjt.eletcpserver.netty.ChannelRepository;
import com.zfjt.eletcpserver.parse.ParseUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;


/**
 * event handler to login
 *
 * @author Jibeom Jung akka. Manty
 */
@Component
@Slf4j
@RequiredArgsConstructor

@ChannelHandler.Sharable
public class MessageHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private final ChannelRepository channelRepository;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;


    @Autowired
    private ClientFlag clientFlag;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        log.debug("remoteAddress:{}",ctx.channel().remoteAddress());
        log.debug("channelId:{}",ctx.channel().id());
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        System.out.println();
        String rawMessage = HexUtil.encodeHexStr(bytes);
        log.info("server收到:{}", rawMessage);

        // 第一次发送的数据为设备编号 要在服务器注册
        if(!clientFlag.hasRegistered(ctx)){
            clientFlag.registered(ctx,ParseUtil.hexStringToString(rawMessage));
        }

        String sn = channelRepository.getChannelSn(ctx.channel());


        if(sn == null){
            // 找不到channel对应的设备编码
            log.error("找不到设备对应的设备编码{}",rawMessage);
            return;
        }


        ZhcMessage zhcMessage = ParseUtil.convertMessage(rawMessage, sn);

        if(zhcMessage instanceof AiMessage){
            AiMessageEvent aiMessageEvent = new AiMessageEvent(this,rawMessage,sn,(AiMessage)zhcMessage);
            applicationEventPublisher.publishEvent(aiMessageEvent);
        }else if(zhcMessage instanceof DiMessage){
            DiMessageEvent diMessageEvent = new DiMessageEvent(this,rawMessage,sn,(DiMessage)zhcMessage);
            applicationEventPublisher.publishEvent(diMessageEvent);
        }else if (zhcMessage instanceof DoMessage){
            DoMessageEvent doMessageEvent = new DoMessageEvent(this,rawMessage,sn,(DoMessage)zhcMessage);
            applicationEventPublisher.publishEvent(doMessageEvent);
        }else if (zhcMessage instanceof DoReceiveMessage){
            DoReceiveMessageEvent doReceiveMessageEvent = new DoReceiveMessageEvent(this,rawMessage,sn,(DoReceiveMessage)zhcMessage);
            applicationEventPublisher.publishEvent(doReceiveMessageEvent);
        }else {
            MessageEvent messageEvent = new MessageEvent(this,rawMessage,sn,zhcMessage);
            applicationEventPublisher.publishEvent(messageEvent);
        }

        ctx.writeAndFlush(HexUtil.decodeHex(sn));
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("tcp连接成功,远程设备地址:{}",ctx.channel().remoteAddress());
        log.debug("tcp连接成功,channelId:{}",ctx.channel().id());
        clientFlag.addToNotRegisterPool(ctx);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("tcp连接断开,远程设备地址:{}",ctx.channel().remoteAddress());
        log.debug("tcp连接断开,channelId:{}",ctx.channel().id());
        if(channelRepository.hasChannel(ctx.channel())){
            channelRepository.remove(ctx.channel());
        }
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.debug(ctx.channel().remoteAddress() + "");
        if(channelRepository.getChannelSn(ctx.channel()) != null){
            channelRepository.remove(ctx.channel());
        }
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }


}
