package com.jrong.dataCollector.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IBotBank {
    String GetBotRateData() throws JsonProcessingException;
}
