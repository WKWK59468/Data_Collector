package com.jrong.dataCollector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jrong.dataCollector.model.CptRateData;

import java.util.List;

public interface ICptRateService {
    boolean SaveCptHistoryRate(String cptHistoryRateData) throws JsonProcessingException;
    String GetCptCurrentRate();
    List<CptRateData> GetCptHistoryRate();
}
