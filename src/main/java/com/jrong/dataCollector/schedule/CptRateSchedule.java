package com.jrong.dataCollector.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.service.impl.CptRateService;
import com.jrong.dataCollector.service.impl.CptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CptRateSchedule {
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final CptService cptService;
    private final CptRateService cptRateService;
    private final LineNotifyHelper lineNotifyHelper;
    private final AtomicInteger consecutiveFailures  = new AtomicInteger(0);

    @Autowired
    public CptRateSchedule(StringRedisTemplate stringRedisTemplate,
                           ObjectMapper objectMapper,
                           CptService cptService,
                           CptRateService cptRateService,
                           LineNotifyHelper lineNotifyHelper){
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
        this.cptService = cptService;
        this.cptRateService = cptRateService;
        this.lineNotifyHelper = lineNotifyHelper;
    }

    @Scheduled(cron = "0 0 0/5 * * ?")
    public void SaveCptCurrentRateData() {
        long currentTime = System.currentTimeMillis();
        try {
            String value = cptService.GetCptCurrentData();
            JsonNode currentData = objectMapper.readTree(value);

            stringRedisTemplate.opsForValue().set("CptCurrentData", currentData.toString());

            consecutiveFailures.set(0);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            int lastConsecutiveFailures = consecutiveFailures.get();
            consecutiveFailures.set(lastConsecutiveFailures + 1);

            if (lastConsecutiveFailures >= 2) {
                lineNotifyHelper.SendMessage("BotRate Schedule Update Failure");
                consecutiveFailures.set(0);
            }

            lineNotifyHelper.SendMessage("BotRate Schedule Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Scheduled(cron = "0 10 1,8,18 5,6,7,15,16,17,25,26,27 * ?")
    public void CptHistoryRateData() {
        lineNotifyHelper.SendMessage("Scheduled: Get Cpt HistoryRate Data Start");
        try {
            String value = cptService.GetCptHistoryData();
            JsonNode cptHistoryData = objectMapper.readTree(value);

            if (!cptHistoryData.isEmpty()) {
                lineNotifyHelper.SendMessage("Get History Data Success");

                var isSuccess = cptRateService.SaveCptHistoryRate(cptHistoryData.toString());
                if (isSuccess){
                    lineNotifyHelper.SendMessage("Save Cpt History Data Success");
                }else{
                    lineNotifyHelper.SendMessage("Save Cpt History Data Failure");
                }
            }else{
                lineNotifyHelper.SendMessage("Get Cpt History Data Failure");
            }

        } catch (JsonProcessingException e) {
            lineNotifyHelper.SendMessage("Cpt History Schedule JsonProcessingException: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e){
            lineNotifyHelper.SendMessage("Cpt History Schedule Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
