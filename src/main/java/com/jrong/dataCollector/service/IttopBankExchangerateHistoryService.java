package com.jrong.dataCollector.service;

import com.jrong.dataCollector.model.CreatettopBankExchangerateHistoryParameter;
import com.jrong.dataCollector.model.ttopBankExchangerateHistory;

import java.util.List;

public interface IttopBankExchangerateHistoryService {
    List<ttopBankExchangerateHistory> GetttopBankExchangerateHistory();
    void CreatettopBankExchangerateHistory(CreatettopBankExchangerateHistoryParameter ttopBankExchangerateHistory);
}
