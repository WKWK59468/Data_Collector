package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrong.dataCollector.dao.ttopBankExchangerateHistoryMapper;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.model.request.CreatettopBankExchangerateHistory;
import com.jrong.dataCollector.model.BotRateData;
import com.jrong.dataCollector.model.CurrentData;
import com.jrong.dataCollector.model.ttopBankExchangerateHistory;
import com.jrong.dataCollector.service.IttopBankExchangerateHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ttopBankExchangerateHistoryService implements IttopBankExchangerateHistoryService {
    @Autowired
    private ttopBankExchangerateHistoryMapper ttopBankExchangerateHistoryMapper;
    @Autowired
    private BOTBankService botBankService;
    @Autowired
    private CPTService cptService;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;

    @Override
    public List<ttopBankExchangerateHistory> GetttopBankExchangerateHistory(){
        return ttopBankExchangerateHistoryMapper.GetttopBankExchangerateHistory();
    }

    @Override
    public boolean SavettopBankExchangerateHistory() throws JsonProcessingException {
        List<CreatettopBankExchangerateHistory> ttopBankExchangerateHistoryList = new ArrayList<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ObjectMapper mapper = new ObjectMapper();

        String rateData = botBankService.GetBotRateData();
        JsonNode rateDataMap = mapper.readTree(rateData);

        rateDataMap.forEach( data -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                BotRateData newBotRateRateData = objectMapper.treeToValue(data, BotRateData.class);
                CreatettopBankExchangerateHistory ttopBankExchangerateHistory = new CreatettopBankExchangerateHistory();

                ttopBankExchangerateHistory.setBankCode(newBotRateRateData.getBankCode());
                ttopBankExchangerateHistory.setExchangeContent(newBotRateRateData.getExchangeContent());
                ttopBankExchangerateHistory.setCreateDate(timestamp.toString());
                ttopBankExchangerateHistoryList.add(ttopBankExchangerateHistory);

            } catch (JsonProcessingException e) {
                lineNotifyHelper.SendMessage("SavettopBankExchangerateHistory JsonProcessingException: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });

        String currentData = cptService.GetCPTCurrentData();
        JsonNode currentDataMap = mapper.readTree(currentData);

        currentDataMap.forEach( data -> {
            try{
                ObjectMapper objectMapper = new ObjectMapper();
                CurrentData newCurrentData = objectMapper.treeToValue(data, CurrentData.class);

                ttopBankExchangerateHistoryList.forEach(ttopBankExchangerateHistory -> {
                    if (ttopBankExchangerateHistory.getBankCode().equals(newCurrentData.getBankCode())){
                        ttopBankExchangerateHistory.setExchangeContent(newCurrentData.getExchangeContent() + " " + ttopBankExchangerateHistory.getExchangeContent());
                    }
                });

            } catch (JsonProcessingException e) {
                lineNotifyHelper.SendMessage("SavettopBankExchangerateHistory JsonProcessingException: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
        return ttopBankExchangerateHistoryMapper.SavettopBankExchangerateHistory(ttopBankExchangerateHistoryList) > 0;
    }

    @Override
    public boolean DeletettopBankExchangerateHistory(int id){
        return ttopBankExchangerateHistoryMapper.DeletettopBankExchangerateHistory(id) > 0;
    }

    @Override
    public boolean UpdatettopBankExchangerateHistory(int id, String exchangeContent){
        return ttopBankExchangerateHistoryMapper.UpdatettopBankExchangerateHistory(id,exchangeContent) > 0;
    }
}
