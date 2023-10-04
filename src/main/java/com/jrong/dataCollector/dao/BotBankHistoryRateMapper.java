package com.jrong.dataCollector.dao;

import com.jrong.dataCollector.model.request.SaveBotBankHistoryRate;
import com.jrong.dataCollector.model.BotBankRateData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BotBankHistoryRateMapper {
    List<BotBankRateData> GetBotHistoryRate();
    int SaveBotBankHistoryRate(List<SaveBotBankHistoryRate> botBankHistoryRateList);
    int DeleteBotBankHistoryRate(int id);
    int UpdateBotBankHistoryRate(List<SaveBotBankHistoryRate> botBankHistoryRateList);
}
