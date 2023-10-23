package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.service.ICheckRateExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CheckRateExist implements ICheckRateExist {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;

    @Override
    public void CheckRateExist(String service, String jsonString) {
        List<String> bankCodesToCheck = Arrays.asList("USD", "CNY", "JPY");

        Optional<Boolean> containsMatchBankCodes = Optional.of(
                bankCodesToCheck.stream().allMatch(jsonString::contains)
        );

        containsMatchBankCodes.filter(isMatch -> !isMatch).ifPresent( isMatch -> lineNotifyHelper.SendMessage(service + " Schedule Error: Some BankCodes don't exist!"));
    }
}
