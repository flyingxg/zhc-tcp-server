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
package com.zfjt.eletcpserver.netty;

import com.zfjt.eletcpserver.communicate.ClientFlag;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Channel Repository using HashMap
 *
 * @author Jibeom Jung akka. Manty
 */
@Slf4j
public class ChannelRepository {


    // sn channel
    private static ConcurrentHashMap<String, Channel> channelCache = new ConcurrentHashMap<>();

    // channel sn
    private static ConcurrentHashMap<String , String> channelSn = new ConcurrentHashMap<>();

    public void put(String key, Channel value) {
        if(channelCache.containsKey(key) && !ClientFlag.hasRegistered(value)){
            channelCache.get(key).close();

            remove(value);
        }
        channelSn.put(value.id().toString(),key);
        channelCache.put(key, value);
    }

    public boolean hasChannel(Channel channel){
        return channelSn.containsKey(channel.id().toString());
    }

    public static String getChannelSn (Channel channel){
        return channelSn.get(channel.id().toString());
    }

    public Channel getSnChannel(String sn){
        return this.channelCache.get(sn);
    }

    public void remove(Channel channel){
        if(channelSn.containsKey(channel.id().toString())){
            if(channelCache.containsKey(channel.id().toString())){
                this.channelCache.remove(channelSn.get(channel.id().toString()));
            }
            channel.close();
        }
        this.channelSn.remove(channel.id().toString());
    }

    public int size() {
        return this.channelCache.size();
    }
}
