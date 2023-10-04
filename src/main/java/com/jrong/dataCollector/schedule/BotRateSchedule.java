package com.jrong.dataCollector.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.service.impl.BotBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class BotRateSchedule {
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final BotBankService botBankService;
    private final LineNotifyHelper lineNotifyHelper;
    private final AtomicLong lastCheckTime = new AtomicLong(0);


    @Autowired
    public BotRateSchedule(StringRedisTemplate stringRedisTemplate,
                           ObjectMapper objectMapper,
                           BotBankService botBankService,
                           LineNotifyHelper lineNotifyHelper){
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
        this.botBankService = botBankService;
        this.lineNotifyHelper = lineNotifyHelper;
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void SaveBotRateData() {
        long currentTime = System.currentTimeMillis();
        try {
            String value = botBankService.GetBotRateData();
            JsonNode botRateData = objectMapper.readTree(value);

            stringRedisTemplate.opsForValue().set("BotRateData", botRateData.toString());

            lastCheckTime.set(currentTime);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            long lastTime = lastCheckTime.get();
            if (currentTime - lastTime >= 1800000) {
                lineNotifyHelper.SendMessage("BotRate Schedule Update Failure");
                lastCheckTime.set(currentTime);
            }
            lineNotifyHelper.SendMessage("BotRate Schedule Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
