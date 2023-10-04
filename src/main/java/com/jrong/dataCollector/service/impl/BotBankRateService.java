package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrong.dataCollector.dao.BotBankHistoryRateMapper;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.model.request.SaveBotBankHistoryRate;
import com.jrong.dataCollector.model.BotBankRateData;
import com.jrong.dataCollector.service.IBotBankRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class BotBankRateService implements IBotBankRateService {
    private final BotBankHistoryRateMapper BotBankHistoryRateMapper;
    private final BotBankService botBankService;
    private final LineNotifyHelper lineNotifyHelper;
    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public BotBankRateService(
            BotBankHistoryRateMapper BotBankHistoryRateMapper,
            BotBankService botBankService,
            LineNotifyHelper lineNotifyHelper,
            StringRedisTemplate stringRedisTemplate){
        this.BotBankHistoryRateMapper = BotBankHistoryRateMapper;
        this.botBankService = botBankService;
        this.lineNotifyHelper = lineNotifyHelper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public List<BotBankRateData> GetBotBankHistoryRate(){
        return BotBankHistoryRateMapper.GetBotHistoryRate();
    }

    @Override
    public String GetBotBankCurrentRate(){
        try {
            String value = stringRedisTemplate.opsForValue().get("BotRateData");

            if (value == null) {
                lineNotifyHelper.SendMessage("Get Bot Current Rate Schedule Failure");
            }

            return value;
        }
        catch (Exception e) {
            lineNotifyHelper.SendMessage("Get Bot Current Rate Schedule Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean SaveBotBankHistoryRate() {
        try {
            List<SaveBotBankHistoryRate> SaveBotBankHistoryRateList = new ArrayList<>();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            ObjectMapper mapper = new ObjectMapper();

            String rateData = botBankService.GetBotRateData();
            JsonNode rateDataMap = mapper.readTree(rateData);

            rateDataMap.forEach( data -> {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    SaveBotBankHistoryRate newBotRateRateData = objectMapper.treeToValue(data, SaveBotBankHistoryRate.class);
                    SaveBotBankHistoryRate botBankHistoryRate = new SaveBotBankHistoryRate();

                    botBankHistoryRate.setBankCode(newBotRateRateData.getBankCode());
                    botBankHistoryRate.setExchangeContent(newBotRateRateData.getExchangeContent());
                    botBankHistoryRate.setCreateDate(timestamp.toString());
                    SaveBotBankHistoryRateList.add(botBankHistoryRate);

                } catch (JsonProcessingException e) {
                    lineNotifyHelper.SendMessage("SaveBotBankHistoryRate JsonProcessingException: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            });
            return BotBankHistoryRateMapper.SaveBotBankHistoryRate(SaveBotBankHistoryRateList) > 0;
        } catch (JsonProcessingException e) {
            lineNotifyHelper.SendMessage("SaveBotBankHistoryRate JsonProcessingException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean DeleteBotBankHistoryRate(int id){
        return BotBankHistoryRateMapper.DeleteBotBankHistoryRate(id) > 0;
    }

//    @Override
//    public boolean UpdateBotBankHistoryRate(BotBankRateData botBankRateData){
//        return BotBankHistoryRateMapper.UpdateBotBankHistoryRate(botBankRateData) > 0;
//    }
}
