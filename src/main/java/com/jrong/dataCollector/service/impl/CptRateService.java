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
import java.util.Optional;

import static com.jrong.dataCollector.helper.ExceptionHelper.lambdaWarpper;

@Service
public class CptRateService implements ICptRateService {
    @Autowired
    private CptHistoryRateMapper cptHistoryRateMapper;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean SaveCptHistoryRate(String cptHistoryRateData) throws JsonProcessingException {

        List<CptRateData> SaveCptRateDataList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rateDataMap = mapper.readTree(cptHistoryRateData);

        rateDataMap.forEach(lambdaWarpper(
                data -> {
                    CptRateData cptRateData = new CptRateData();
                    dataProcess(data, cptRateData);
                    SaveCptRateDataList.add(cptRateData);
                },
                JsonProcessingException.class));

        return cptHistoryRateMapper.SaveCptHistoryRate(SaveCptRateDataList) > 0;
    }

    @Override
    public String GetCptCurrentRate() {
        Optional<String> cptCurrentData = Optional.ofNullable(stringRedisTemplate.opsForValue().get("CptCurrentData"));

        cptCurrentData.ifPresentOrElse(
                data -> {
                },
                () -> lineNotifyHelper.SendMessage("Get Cpt CurrentRate Failure")
        );

        return cptCurrentData.orElse(null);
    }

    @Override
    public List<CptRateData> GetCptHistoryRate() {
        return cptHistoryRateMapper.GetCptHistoryRate();
    }

    private static void dataProcess(JsonNode data, CptRateData cptRateData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        CptRateData newCptRateData = objectMapper.treeToValue(data, CptRateData.class);

        cptRateData.setBankCode(newCptRateData.getBankCode());
        cptRateData.setYear(newCptRateData.getYear());
        cptRateData.setMonth(newCptRateData.getMonth());
        cptRateData.setTenDays(newCptRateData.getTenDays());
        cptRateData.setBuy(newCptRateData.getBuy());
        cptRateData.setSell(newCptRateData.getSell());
    }
}
