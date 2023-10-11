package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.service.ICheckRateExistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CheckRateExistService implements ICheckRateExistService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;

    @Override
    public void CheckRateExist(String service, String jsonString) {
        Optional<String> rateData = Optional.of(jsonString);
        rateData.filter( data -> data.contains("USD"))
                .ifPresentOrElse(
                        data -> {},
                        () -> lineNotifyHelper.SendMessage(service + " Schedule Error: USD is not exist!"));
        rateData.filter( data -> data.contains("CNY"))
                .ifPresentOrElse(
                        data -> {},
                        () -> lineNotifyHelper.SendMessage(service + " Schedule Error: CNY is not exist!"));
        rateData.filter( data -> data.contains("JPY"))
                .ifPresentOrElse(
                        d -> {},
                        () -> lineNotifyHelper.SendMessage(service + " Schedule Error: JPY is not exist!"));
    }
}
