package com.jrong.dataCollector.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.listener.RateEvent;
import com.jrong.dataCollector.model.BotBankRateData;
import com.jrong.dataCollector.model.request.SaveBotBankHistoryRate;
import com.jrong.dataCollector.service.impl.BotBankService;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class BotRateSchedule {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private BotBankService botBankService;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;
    private final AtomicLong lastCheckTime = new AtomicLong(0);

    @Scheduled(cron = "0 0/1 * * * ?")
    public void SaveBotRateData() {
        long currentTime = System.currentTimeMillis();
        try {
            String value = botBankService.GetBotRateData();
            JsonNode botRateData = objectMapper.readTree(value);
            Optional<JsonNode> currentDataOpt = Optional.of(botRateData);
            currentDataOpt.ifPresent(this::dataProcess);
            stringRedisTemplate.opsForValue().set("BotRateData", botRateData.toString());
            lastCheckTime.set(currentTime);
        } catch (Exception e) {
            Optional<Long> lastTime = Optional.of(lastCheckTime.get());

            lastTime.filter( last -> currentTime - last >= 1800000 ).ifPresent( last -> {
                lineNotifyHelper.SendMessage("BotRate Schedule Update Failure");
                lastCheckTime.set(currentTime);
            });

            lineNotifyHelper.SendMessage("BotRate Schedule Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void dataProcess(JsonNode data) {
        data.forEach( value -> {
            try {
                SaveBotBankHistoryRate rateData = objectMapper.treeToValue(value, SaveBotBankHistoryRate.class);
                Optional<SaveBotBankHistoryRate> dataOpt = Optional.of(rateData);

                dataOpt.filter(opt -> opt.getBank_code().equals("JPY") && opt.getBuy_spot_rate() <= 0.22).ifPresent(opt -> {
                    context.publishEvent(new RateEvent(this, "BOT", rateData.getBuy_spot_rate()));
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
