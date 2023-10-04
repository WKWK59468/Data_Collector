package com.jrong.dataCollector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jrong.dataCollector.model.BotBankRateData;
import com.jrong.dataCollector.model.request.SaveBotBankHistoryRate;

import java.util.List;

public interface IBotBankRateService {
    List<BotBankRateData> GetBotBankHistoryRate();
    boolean SaveBotBankHistoryRate() throws JsonProcessingException;
    boolean DeleteBotBankHistoryRate(int id);
    String GetBotBankCurrentRate();
}
