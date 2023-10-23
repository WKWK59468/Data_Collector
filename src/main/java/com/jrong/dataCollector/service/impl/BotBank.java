package com.jrong.dataCollector.service.impl;

import com.jrong.dataCollector.service.IBotBank;
import com.jrong.dataCollector.service.factory.DataType;
import com.jrong.dataCollector.service.factory.IDataProcessFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotBank implements IBotBank {
    @Autowired
    private IDataProcessFactory dataProcessFactory;

    @Override
    public String GetBotRateData() {
        return dataProcessFactory.ProcessData(DataType.BotCurrent);
    }
}
