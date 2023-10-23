package com.jrong.dataCollector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jrong.dataCollector.model.BotBankRateData;
import com.jrong.dataCollector.model.request.DeleteBotBankHistory;
import com.jrong.dataCollector.model.request.SelectBotBankHistory;
import com.jrong.dataCollector.model.request.UpdateBotBankHistory;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBotBankRate {
    ResponseEntity<List<BotBankRateData>> GetBotBankHistoryRate(SelectBotBankHistory selectBotBankHistory);
    ResponseEntity<String>  GetBotBankCurrentRate();
    ResponseEntity<Boolean> SaveBotBankHistoryRate() throws JsonProcessingException;
    ResponseEntity<Boolean>  DeleteBotBankHistoryRate(DeleteBotBankHistory deleteBotBankHistory);
    ResponseEntity<Boolean>  UpdateBotBankHistoryRate(UpdateBotBankHistory updateBotBankHistory);
}
