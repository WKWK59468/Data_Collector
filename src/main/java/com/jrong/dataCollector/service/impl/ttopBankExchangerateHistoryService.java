package com.jrong.dataCollector.service.impl;

import com.jrong.dataCollector.dao.ttopBankExchangerateHistoryMapper;
import com.jrong.dataCollector.model.CreatettopBankExchangerateHistoryParameter;
import com.jrong.dataCollector.model.ttopBankExchangerateHistory;
import com.jrong.dataCollector.service.IttopBankExchangerateHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ttopBankExchangerateHistoryService implements IttopBankExchangerateHistoryService {
    @Autowired

    private ttopBankExchangerateHistoryMapper _ttopBankExchangerateHistoryMapper;

    @Override
    public List<ttopBankExchangerateHistory> GetttopBankExchangerateHistory(){
        return _ttopBankExchangerateHistoryMapper.GetttopBankExchangerateHistory();
    }

    @Override
    public void CreatettopBankExchangerateHistory(CreatettopBankExchangerateHistoryParameter ttopBankExchangerateHistory){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ttopBankExchangerateHistory.setCreateDate(timestamp.toString());
        _ttopBankExchangerateHistoryMapper.CreatettopBankExchangerateHistory(ttopBankExchangerateHistory);
    }
}
