package com.jrong.dataCollector.service.impl;

import com.jrong.dataCollector.service.IDataParser;
import com.jrong.dataCollector.service.ICpt;
import com.jrong.dataCollector.service.IDataProcess;
import com.jrong.dataCollector.service.factory.DataType;
import com.jrong.dataCollector.service.factory.IDataProcessFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Cpt implements ICpt {
    @Autowired
    private IDataProcessFactory dataProcessFactory;

    @Override
    public String GetCptCurrentData() {
        return dataProcessFactory.ProcessData(DataType.CptCurrent);
    }

    @Override
    public String GetCptHistoryData() {
        return dataProcessFactory.ProcessData(DataType.CptHistory);
    }
}