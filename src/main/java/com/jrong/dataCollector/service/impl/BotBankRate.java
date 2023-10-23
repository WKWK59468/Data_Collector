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
import com.jrong.dataCollector.service.IBotBankRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.jrong.dataCollector.helper.ExceptionHelper.lambdaWarpper;

@Service
public class BotBankRate implements IBotBankRate {
    @Autowired
    private BotBankHistoryRateMapper botBankHistoryRateMapper;
    @Autowired
    private BotBank botBank;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public BotBankRate() {
    }

    @Override
    public ResponseEntity<List<BotBankRateData>> GetBotBankHistoryRate(SelectBotBankHistory selectBotBankHistory) {
        Optional<List<BotBankRateData>> value = Optional.ofNullable(botBankHistoryRateMapper.GetBotHistoryRate(selectBotBankHistory.getDate()));

        value.ifPresentOrElse(
                data -> lineNotifyHelper.SendMessage("Get Bot History Rate Schedule Success"),
                () -> lineNotifyHelper.SendMessage("Get Bot History Rate Schedule Failure"));

        return value
                .map(botBankRateData -> ResponseEntity.ok().body(botBankRateData))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @Override
    public ResponseEntity<String> GetBotBankCurrentRate() {
        Optional<String> value = Optional.ofNullable(stringRedisTemplate.opsForValue().get("BotRateData"));

        value.ifPresentOrElse(
                data -> lineNotifyHelper.SendMessage("Get Bot Current Rate Schedule Success"),
                () -> lineNotifyHelper.SendMessage("Get Bot Current Rate Schedule Failure"));

        return value
                .map(botBankRateData -> ResponseEntity.ok().body(botBankRateData))
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Boolean> SaveBotBankHistoryRate() throws JsonProcessingException {
        List<SaveBotBankHistory> saveBotBankHistoryList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode rateDataMap = mapper.readTree(botBank.GetBotRateData());

        rateDataMap.forEach(lambdaWarpper(
                data -> {
                    SaveBotBankHistory botBankHistoryRate = dataProcess(data);
                    saveBotBankHistoryList.add(botBankHistoryRate);
                    },
                JsonProcessingException.class));

       if(botBankHistoryRateMapper.SaveBotBankHistoryRate(saveBotBankHistoryList) < 0){
           return ResponseEntity.badRequest().build();
       }
        return ResponseEntity.ok().body(true);
    }

    @Override
    public ResponseEntity<Boolean> DeleteBotBankHistoryRate(DeleteBotBankHistory deleteBotBankHistory) {
        if(botBankHistoryRateMapper.DeleteBotBankHistoryRate(deleteBotBankHistory.getDate()) < 0){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(true);
    }

    @Override
    public ResponseEntity<Boolean> UpdateBotBankHistoryRate(UpdateBotBankHistory updateBotBankHistory) {
        if (botBankHistoryRateMapper.UpdateBotBankHistoryRate(updateBotBankHistory) < 0 ){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(true);
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
