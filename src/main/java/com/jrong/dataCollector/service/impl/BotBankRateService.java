package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrong.dataCollector.dao.BotBankHistoryRateMapper;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.model.request.DeleteBotBankHistory;
import com.jrong.dataCollector.model.request.SaveBotBankHistory;
import com.jrong.dataCollector.model.BotBankRateData;
import com.jrong.dataCollector.model.request.SelectBotBankHistory;
import com.jrong.dataCollector.model.request.UpdateBotBankHistory;
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
    private BotBankHistoryRateMapper botBankHistoryRateMapper;
    @Autowired
    private BotBankService botBankService;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public BotBankRateService() {
    }

    @Override
    public List<BotBankRateData> GetBotBankHistoryRate(SelectBotBankHistory selectBotBankHistory) {
        var data1 = botBankHistoryRateMapper.GetBotHistoryRate(selectBotBankHistory.getDate());
        Optional<List<BotBankRateData>> value = Optional.ofNullable(data1);

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
        List<SaveBotBankHistory> saveBotBankHistoryList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode rateDataMap = mapper.readTree(botBankService.GetBotRateData());

        rateDataMap.forEach(data -> {
            try {
                SaveBotBankHistory botBankHistoryRate = dataProcess(data);
                saveBotBankHistoryList.add(botBankHistoryRate);
            } catch (JsonProcessingException e) {
                throw new RuntimeException();
            }
        });

        return botBankHistoryRateMapper.SaveBotBankHistoryRate(saveBotBankHistoryList) > 0;
    }

    @Override
    public boolean DeleteBotBankHistoryRate(DeleteBotBankHistory deleteBotBankHistory) {
        return botBankHistoryRateMapper.DeleteBotBankHistoryRate(deleteBotBankHistory.getDate()) > 0;
    }

    @Override
    public boolean UpdateBotBankHistoryRate(UpdateBotBankHistory updateBotBankHistory){
        return botBankHistoryRateMapper.UpdateBotBankHistoryRate(updateBotBankHistory) > 0;
    }

    private SaveBotBankHistory dataProcess(JsonNode data) throws JsonProcessingException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ObjectMapper objectMapper = new ObjectMapper();
        SaveBotBankHistory newBotRateRateData = objectMapper.treeToValue(data, SaveBotBankHistory.class);

        SaveBotBankHistory botBankHistoryRate = new SaveBotBankHistory();
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

        return botBankHistoryRate;
    }
}
