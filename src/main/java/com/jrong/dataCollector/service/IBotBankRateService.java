package com.jrong.dataCollector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jrong.dataCollector.model.BotBankRateData;
import com.jrong.dataCollector.model.request.DeleteBotBankHistory;
import com.jrong.dataCollector.model.request.SelectBotBankHistory;
import com.jrong.dataCollector.model.request.UpdateBotBankHistory;

import java.util.List;

public interface IBotBankRateService {
    List<BotBankRateData> GetBotBankHistoryRate(SelectBotBankHistory selectBotBankHistory);
    boolean SaveBotBankHistoryRate() throws JsonProcessingException;
    String GetBotBankCurrentRate();
    boolean DeleteBotBankHistoryRate(DeleteBotBankHistory deleteBotBankHistory);
    boolean UpdateBotBankHistoryRate(UpdateBotBankHistory updateBotBankHistory);
}
