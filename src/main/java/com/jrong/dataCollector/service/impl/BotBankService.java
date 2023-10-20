package com.jrong.dataCollector.service.impl;

import com.jrong.dataCollector.service.IDataParserService;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.service.IBotBankService;
import com.jrong.dataCollector.service.IDataProcessService;
import com.jrong.dataCollector.service.factory.IDataProcessFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotBankService implements IBotBankService {
    @Autowired
    private LineNotifyHelper lineNotifyHelper;
    @Autowired
    private CheckRateExistService checkRateExistService;
    @Autowired
    private IDataParserService dataParser;
    @Autowired
    private IDataProcessService dataProcess;
    @Autowired
    private IDataProcessFactory dataProcessFactory;

    @Override
    public String GetBotRateData() {
        String data = dataProcessFactory.GetBotCurrentData();
        checkRateExistService.CheckRateExist("Bot Rate", data);
        return data;
    }
}
