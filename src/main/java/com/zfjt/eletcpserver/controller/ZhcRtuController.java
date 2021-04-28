package com.zfjt.eletcpserver.controller;

import com.zfjt.eletcpserver.message.DoReceiveMessage;
import com.zfjt.eletcpserver.service.ZhcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control")
public class ZhcRtuController {

    @Autowired
    ZhcService zhcService;

    @RequestMapping("rtu")
    public String rtu(String sn,String command) throws InterruptedException {
        return zhcService.control(command,sn);
    }

    @RequestMapping("rtuStatus")
    public DoReceiveMessage rtuStatus(String sn) throws InterruptedException {
        DoReceiveMessage doStatus = zhcService.getDoStatus(sn);
        return doStatus;
    }


}
