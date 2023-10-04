package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrong.dataCollector.dao.CptHistoryRateMapper;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.model.CptRateData;
import com.jrong.dataCollector.service.ICptRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CptRateService implements ICptRateService {
    private final CptHistoryRateMapper cptHistoryRateMapper;
    private final LineNotifyHelper lineNotifyHelper;
    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public CptRateService(
            CptHistoryRateMapper cptHistoryRateMapper,
            LineNotifyHelper lineNotifyHelper,
            StringRedisTemplate stringRedisTemplate){
        this.cptHistoryRateMapper = cptHistoryRateMapper;
        this.lineNotifyHelper = lineNotifyHelper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean SaveCptHistoryRate(String cptHistoryRateData){
        try {
            List<CptRateData> SaveCptRateDataList = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rateDataMap = mapper.readTree(cptHistoryRateData);

            rateDataMap.forEach( data -> {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    CptRateData newCptRateData = objectMapper.treeToValue(data, CptRateData.class);
                    CptRateData cptRateData = new CptRateData();

                    cptRateData.setBankCode(newCptRateData.getBankCode());
                    cptRateData.setYear(newCptRateData.getYear());
                    cptRateData.setMonth(newCptRateData.getMonth());
                    cptRateData.setTenDays(newCptRateData.getTenDays());
                    cptRateData.setBuy(newCptRateData.getBuy());
                    cptRateData.setSell(newCptRateData.getSell());
                    SaveCptRateDataList.add(cptRateData);

                } catch (JsonProcessingException e) {
                    lineNotifyHelper.SendMessage("SaveBotBankHistoryRate JsonProcessingException: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            });
            return cptHistoryRateMapper.SaveCptHistoryRate(SaveCptRateDataList) > 0;
        } catch (JsonProcessingException e) {
            lineNotifyHelper.SendMessage("SaveBotBankHistoryRate JsonProcessingException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String GetCptCurrentRate(){
        try{
            String cptCurrentData = stringRedisTemplate.opsForValue().get("CptCurrentData");

            if( cptCurrentData == null ){
                lineNotifyHelper.SendMessage("Get Cpt CurrentRate Failure");
            }

            return cptCurrentData;
        }catch (Exception e){
            lineNotifyHelper.SendMessage("Get Cpt Current Rate Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CptRateData> GetCptHistoryRate(){
        return cptHistoryRateMapper.GetCptHistoryRate();
    }
}
