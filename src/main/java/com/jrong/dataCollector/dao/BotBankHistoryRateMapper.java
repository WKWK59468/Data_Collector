package com.jrong.dataCollector.dao;

import com.jrong.dataCollector.model.request.SaveBotBankHistory;
import com.jrong.dataCollector.model.BotBankRateData;
import com.jrong.dataCollector.model.request.UpdateBotBankHistory;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface BotBankHistoryRateMapper {
    List<BotBankRateData> GetBotHistoryRate(@Param("date") LocalDate date);
    Integer SaveBotBankHistoryRate(List<SaveBotBankHistory> botBankHistoryRateList);
    Integer DeleteBotBankHistoryRate(@Param("date") LocalDate date);
    Integer UpdateBotBankHistoryRate(UpdateBotBankHistory updateBotBankHistory);
}
