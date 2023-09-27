package com.jrong.dataCollector.dao;

import com.jrong.dataCollector.model.request.CreatettopBankExchangerateHistory;
import com.jrong.dataCollector.model.ttopBankExchangerateHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ttopBankExchangerateHistoryMapper {
    List<ttopBankExchangerateHistory> GetttopBankExchangerateHistory();
    int SavettopBankExchangerateHistory(List<CreatettopBankExchangerateHistory> ttopBankExchangerateHistoryList);
    int DeletettopBankExchangerateHistory(int id);
    int UpdatettopBankExchangerateHistory(int id, String exchangeContent);
}
