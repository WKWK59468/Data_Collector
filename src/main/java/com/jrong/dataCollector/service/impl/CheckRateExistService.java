package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.service.ICheckRateExistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CheckRateExistService implements ICheckRateExistService {
    private final ObjectMapper objectMapper;
    private final LineNotifyHelper lineNotifyHelper;

    @Autowired
    public CheckRateExistService(ObjectMapper objectMapper, LineNotifyHelper lineNotifyHelper){
        this.objectMapper = objectMapper;
        this.lineNotifyHelper = lineNotifyHelper;
    }

    @Override
    public void CheckRateExist(String jsonString) {
        try{
            if(!jsonString.contains("USD")){
                lineNotifyHelper.SendMessage("BotRate Schedule Error: USD is not exist!");
            }
            if(!jsonString.contains("CNY")){
                lineNotifyHelper.SendMessage("BotRate Schedule Error: CNY is not exist!");
            }
            if(!jsonString.contains("JPY")){
                lineNotifyHelper.SendMessage("BotRate Schedule Error: JPY is not exist!");
            }
        }catch (Exception e) {
            lineNotifyHelper.SendMessage("BotRate Schedule Error: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
