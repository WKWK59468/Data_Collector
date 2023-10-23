package com.jrong.dataCollector.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.service.IBotBankRate;
import com.jrong.dataCollector.service.IBotBank;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class BotRateSchedule {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private IBotBank botBank;
    @Autowired
    private IBotBankRate botBankRate;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;
    private final AtomicLong lastCheckTime = new AtomicLong(0);

    @Scheduled(cron = "0 0/1 * * * ?")
    public void SaveBotRateData() {
        long currentTime = System.currentTimeMillis();
        try {
            String value = botBank.GetBotRateData();
            JsonNode botRateData = objectMapper.readTree(value);
            stringRedisTemplate.opsForValue().set("BotRateData", botRateData.toString());
            lastCheckTime.set(currentTime);
        } catch (Exception e) {
            Optional<Long> lastTime = Optional.of(lastCheckTime.get());

            lastTime.filter(last -> currentTime - last >= 1800000).ifPresent(last -> {
                lineNotifyHelper.SendMessage("BotRate Schedule Update Failure");
                lastCheckTime.set(currentTime);
            });

            lineNotifyHelper.SendMessage("BotRate Schedule Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "0 59 16 * * ?")
    public void SaveLastBotRateData() throws JsonProcessingException {
        botBankRate.SaveBotBankHistoryRate();
    }
}
