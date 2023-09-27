package com.jrong.dataCollector.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.stereotype.Component;

@Component
public class ServiceImplCommandLineRunner implements CommandLineRunner {
    @Value("${server.port}")
    private int port;

    @Autowired
    private LineNotifyHelper lineNotifyHelper;

    @Override
    public void run(String...args) throws Exception {
        lineNotifyHelper.SendMessage("Service Running on " + port + " Port");
    }
}
