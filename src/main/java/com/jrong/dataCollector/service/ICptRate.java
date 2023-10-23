package com.jrong.dataCollector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jrong.dataCollector.model.CptRateData;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICptRate {
    Boolean SaveCptHistoryRate(String cptHistoryRateData) throws JsonProcessingException;
    ResponseEntity<String> GetCptCurrentRate();
    ResponseEntity<List<CptRateData>> GetCptHistoryRate();
}
