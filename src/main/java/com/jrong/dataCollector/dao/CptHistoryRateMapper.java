package com.jrong.dataCollector.dao;

import com.jrong.dataCollector.model.CptRateData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CptHistoryRateMapper {
    int SaveCptHistoryRate(List<CptRateData> CptRateDataList);
    List<CptRateData> GetCptHistoryRate();
}
