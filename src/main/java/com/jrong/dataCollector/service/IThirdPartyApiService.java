package com.jrong.dataCollector.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IThirdPartyApiService {
    Object GetRateBotData() throws JsonProcessingException;
    Object GetPortalData();
}
