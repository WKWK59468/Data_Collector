package com.jrong.dataCollector.dao;

import com.jrong.dataCollector.model.CreatettopBankExchangerateHistoryParameter;
import com.jrong.dataCollector.model.ttopBankExchangerateHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ttopBankExchangerateHistoryMapper {
    List<ttopBankExchangerateHistory> GetttopBankExchangerateHistory();
    @Insert("insert into digiwin.ttop_bank_exchangerate_history(bankCode, exchangeContent, createDate) values(#{bankCode}, #{exchangeContent}, #{createDate});")
    default void CreatettopBankExchangerateHistory(CreatettopBankExchangerateHistoryParameter ttopBankExchangerateHistory){};
}
