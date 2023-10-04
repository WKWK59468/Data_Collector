package com.jrong.dataCollector.service;

import com.jrong.dataCollector.model.CptRateData;

import java.util.List;

public interface ICptRateService {
    boolean SaveCptHistoryRate(String cptHistoryRateData);
    String GetCptCurrentRate();
    List<CptRateData> GetCptHistoryRate();
}
