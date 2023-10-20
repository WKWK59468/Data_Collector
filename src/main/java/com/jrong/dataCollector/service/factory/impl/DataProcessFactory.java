package com.jrong.dataCollector.service.factory.impl;

import com.jrong.dataCollector.service.IDataProcessService;
import com.jrong.dataCollector.service.factory.IDataProcessFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataProcessFactory implements IDataProcessFactory {
    @Autowired
    private IDataProcessService dataProcess;

    @Override
    public String GetCptCurrentData(){
        return dataProcess.CptCurrentDataProcess();
    }

    @Override
    public String GetCptHistoryData(){
        return dataProcess.CptHistoryDataProcess();
    };

    @Override
    public String GetBotCurrentData(){
        return dataProcess.BotCurrentDataProcess();
    };
}
