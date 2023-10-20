package com.jrong.dataCollector.service.factory.impl;

import com.jrong.dataCollector.service.IDataParserService;
import com.jrong.dataCollector.service.IDataProcessService;
import com.jrong.dataCollector.service.factory.IDataProcessFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DataProcessFactory implements IDataProcessFactory {
    @Value("${cptUrl}")
    private String cptUrl;
    @Value("${cptHistoryUrl}")
    private String cptHistoryUrl;
    @Value("${botUrl}")
    private String botUrl;

    @Autowired
    private IDataProcessService dataProcess;
    @Autowired
    private IDataParserService dataParser;

    @Override
    public String DataProcess(String dataType){
        String[] lines = null;
        switch (dataType){
            case "cptCurrent" :
                lines =  dataParser.DataParser(cptUrl);
                return dataProcess.CptCurrentDataProcess(lines);
            case "cptHistory" :
                lines = dataParser.DataParser(cptHistoryUrl);
                return dataProcess.CptHistoryDataProcess(lines);
            case "botCurrent" :
                lines = dataParser.DataParser(botUrl);
                return dataProcess.BotCurrentDataProcess(lines);
            default:
                return null;
        }
    }
}
