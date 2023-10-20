package com.jrong.dataCollector.service;

public interface IDataProcessService {
    String BotCurrentDataProcess(String[] lines);
    String CptCurrentDataProcess(String[] lines);
    String CptHistoryDataProcess(String[] lines);
}
