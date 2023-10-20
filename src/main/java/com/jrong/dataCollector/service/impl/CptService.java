package com.jrong.dataCollector.service.impl;

import com.jrong.dataCollector.service.IDataParserService;
import com.jrong.dataCollector.service.ICptService;
import com.jrong.dataCollector.service.IDataProcessService;
import com.jrong.dataCollector.service.factory.IDataProcessFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CptService implements ICptService {
    @Autowired
    private CheckRateExistService checkRateExistService;
    @Autowired
    private IDataParserService dataParser;
    @Autowired
    private IDataProcessService dataProcess;
    @Autowired
    private IDataProcessFactory dataProcessFactory;

    @Override
    public String GetCptCurrentData() {
        String data = dataProcessFactory.GetCptCurrentData();
        checkRateExistService.CheckRateExist("Cpt Current", data);
        return data;
    }

    @Override
    public String GetCptHistoryData() {
        String data = dataProcessFactory.GetCptHistoryData();
        checkRateExistService.CheckRateExist("Cpt History", data);
        return data;
    }
}