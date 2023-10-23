package com.jrong.dataCollector.service.factory.impl;

import com.jrong.dataCollector.service.factory.DataType;
import com.jrong.dataCollector.service.factory.IDataProcessFactory;
import com.jrong.dataCollector.service.impl.BotCurrentDataProcess;
import com.jrong.dataCollector.service.impl.CptCurrentDataProcess;
import com.jrong.dataCollector.service.impl.CptHistoryDataProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DataProcessFactory implements IDataProcessFactory {
    @Autowired
    BotCurrentDataProcess botCurrentDataProcess;
    @Autowired
    CptCurrentDataProcess cptCurrentDataProcess;
    @Autowired
    CptHistoryDataProcess cptHistoryDataProcess;

    @Override
    public String ProcessData(DataType dataType){
        switch (dataType){
            case BotCurrent:
                return botCurrentDataProcess.Process();
            case CptCurrent:
                return cptCurrentDataProcess.Process();
            case CptHistory:
                return cptHistoryDataProcess.Process();
            default:
                return null;
        }
    }
}
