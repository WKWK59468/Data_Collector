package com.jrong.dataCollector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jrong.dataCollector.model.ttopBankExchangerateHistory;

import java.util.List;

public interface IttopBankExchangerateHistoryService {
    List<ttopBankExchangerateHistory> GetttopBankExchangerateHistory();
    boolean SavettopBankExchangerateHistory() throws JsonProcessingException;
    boolean DeletettopBankExchangerateHistory(int id);
    boolean UpdatettopBankExchangerateHistory(int id, String exchangeContent);
}
