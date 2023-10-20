package com.jrong.dataCollector.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.listener.RateEvent;
import com.jrong.dataCollector.model.BotBankRateData;
import com.jrong.dataCollector.model.CptRateData;
import com.jrong.dataCollector.service.impl.CptRateService;
import com.jrong.dataCollector.service.impl.CptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class CptRateSchedule {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private CptService cptService;
    @Autowired
    private CptRateService cptRateService;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;
    private final AtomicInteger consecutiveFailures = new AtomicInteger(0);

    @Scheduled(cron = "0 0 0/5 * * ?")
    public void SaveCptCurrentRateData() {
        try {
            String values = cptService.GetCptCurrentData();
            JsonNode currentData = objectMapper.readTree(values);
            stringRedisTemplate.opsForValue().set("CptCurrentData", currentData.toString());
            consecutiveFailures.set(0);

        } catch (Exception e) {
            Optional<Integer> lastConsecutiveFailures = Optional.of(consecutiveFailures.get());
            lastConsecutiveFailures.ifPresent(lastFailures -> consecutiveFailures.set(lastFailures + 1));

            lastConsecutiveFailures.filter(lastFailures -> lastFailures >= 2).ifPresent(lastFailures -> {
                lineNotifyHelper.SendMessage("CptRate Schedule Update Failure");
                consecutiveFailures.set(0);
            });

            lineNotifyHelper.SendMessage("CptRate Schedule Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "0 10 1,8,18 5,6,7,15,16,17,25,26,27 * ?")
    public void CptHistoryRateData() {
        try {
            String value = cptService.GetCptHistoryData();
            JsonNode cptHistoryData = objectMapper.readTree(value);

            lineNotifyHelper.SendMessage("Get History Data Success");

            Optional<Boolean> isSuccess = Optional.of(cptRateService.SaveCptHistoryRate(cptHistoryData.toString()));
            isSuccess.ifPresentOrElse(
                    success -> lineNotifyHelper.SendMessage("Save Cpt History Data Success"),
                    () -> lineNotifyHelper.SendMessage("Save Cpt History Data Failure"));

        } catch (Exception e) {
            lineNotifyHelper.SendMessage("Cpt History Schedule Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
