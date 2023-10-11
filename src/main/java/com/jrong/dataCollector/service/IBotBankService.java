package com.jrong.dataCollector.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IBotBankService {
    String GetBotRateData() throws JsonProcessingException;
}
