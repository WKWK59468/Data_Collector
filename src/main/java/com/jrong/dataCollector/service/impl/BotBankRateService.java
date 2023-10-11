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
import java.util.Optional;

@Service
public class BotBankRateService implements IBotBankRateService {
    @Autowired
    private BotBankHistoryRateMapper BotBankHistoryRateMapper;
    @Autowired
    private BotBankService botBankService;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<BotBankRateData> GetBotBankHistoryRate(){
        Optional<List<BotBankRateData>> value = Optional.ofNullable(BotBankHistoryRateMapper.GetBotHistoryRate());

        value.ifPresentOrElse(
                data -> lineNotifyHelper.SendMessage("Get Bot History Rate Schedule Success"),
                () -> lineNotifyHelper.SendMessage("Get Bot History Rate Schedule Failure"));

        return value.orElse(null);
    }

    @Override
    public String GetBotBankCurrentRate() {
        Optional<String> value = Optional.ofNullable(stringRedisTemplate.opsForValue().get("BotRateData"));

        value.ifPresentOrElse(
                data -> lineNotifyHelper.SendMessage("Get Bot Current Rate Schedule Success"),
                () -> lineNotifyHelper.SendMessage("Get Bot Current Rate Schedule Failure"));

        return value.orElse(null);
    }

    @Override
    public boolean SaveBotBankHistoryRate() throws JsonProcessingException {
        List<SaveBotBankHistoryRate> SaveBotBankHistoryRateList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode rateDataMap = mapper.readTree(botBankService.GetBotRateData());

        rateDataMap.forEach( data -> {
            SaveBotBankHistoryRate botBankHistoryRate = new SaveBotBankHistoryRate();
            dataProcess(data, botBankHistoryRate);
            SaveBotBankHistoryRateList.add(botBankHistoryRate);
        });

        return BotBankHistoryRateMapper.SaveBotBankHistoryRate(SaveBotBankHistoryRateList) > 0;
    }

    private static void dataProcess(JsonNode data, SaveBotBankHistoryRate botBankHistoryRate) {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            ObjectMapper objectMapper = new ObjectMapper();
            SaveBotBankHistoryRate newBotRateRateData = objectMapper.treeToValue(data, SaveBotBankHistoryRate.class);

            botBankHistoryRate.setBank_code(newBotRateRateData.getBank_code());
            botBankHistoryRate.setBuy_bank_note_rate(newBotRateRateData.getBuy_bank_note_rate());
            botBankHistoryRate.setBuy_spot_rate(newBotRateRateData.getBuy_spot_rate());
            botBankHistoryRate.setBuy_10days_forward_rate(newBotRateRateData.getBuy_10days_forward_rate());
            botBankHistoryRate.setBuy_30days_forward_rate(newBotRateRateData.getBuy_30days_forward_rate());
            botBankHistoryRate.setBuy_60days_forward_rate(newBotRateRateData.getBuy_60days_forward_rate());
            botBankHistoryRate.setBuy_90days_forward_rate(newBotRateRateData.getBuy_90days_forward_rate());
            botBankHistoryRate.setBuy_120days_forward_rate(newBotRateRateData.getBuy_120days_forward_rate());
            botBankHistoryRate.setBuy_150days_forward_rate(newBotRateRateData.getBuy_150days_forward_rate());
            botBankHistoryRate.setBuy_180days_forward_rate(newBotRateRateData.getBuy_180days_forward_rate());
            botBankHistoryRate.setSell_bank_note_rate(newBotRateRateData.getSell_bank_note_rate());
            botBankHistoryRate.setSell_spot_rate(newBotRateRateData.getSell_spot_rate());
            botBankHistoryRate.setSell_10days_forward_rate(newBotRateRateData.getSell_10days_forward_rate());
            botBankHistoryRate.setSell_30days_forward_rate(newBotRateRateData.getSell_30days_forward_rate());
            botBankHistoryRate.setSell_60days_forward_rate(newBotRateRateData.getSell_60days_forward_rate());
            botBankHistoryRate.setSell_90days_forward_rate(newBotRateRateData.getSell_90days_forward_rate());
            botBankHistoryRate.setSell_120days_forward_rate(newBotRateRateData.getSell_120days_forward_rate());
            botBankHistoryRate.setSell_150days_forward_rate(newBotRateRateData.getSell_150days_forward_rate());
            botBankHistoryRate.setSell_180days_forward_rate(newBotRateRateData.getSell_180days_forward_rate());
            botBankHistoryRate.setCreate_date(timestamp.toString());

        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
