package com.jrong.dataCollector.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrong.dataCollector.listener.RateEvent;
import com.jrong.dataCollector.model.CptRateData;
import com.jrong.dataCollector.model.request.SaveBotBankHistory;
import com.jrong.dataCollector.service.impl.BotBank;
import com.jrong.dataCollector.service.impl.Cpt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.jrong.dataCollector.helper.ExceptionHelper.lambdaWarpper;

@Component
public class RateChangeEventSchedule {
    @Autowired
    private BotBank botBank;
    @Autowired
    private Cpt cpt;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;

    @Scheduled(cron = "0 0 9 * * ?")
    public void BotRateData() throws JsonProcessingException {
        String value = botBank.GetBotRateData();
        JsonNode botRateData = objectMapper.readTree(value);
        Optional<JsonNode> currentDataOpt = Optional.of(botRateData);
        currentDataOpt.ifPresent(data ->
                data.forEach(lambdaWarpper(
                        v -> {
                            SaveBotBankHistory rateData = objectMapper.treeToValue(v, SaveBotBankHistory.class);
                            Optional<SaveBotBankHistory> dataOpt = Optional.of(rateData);

                            dataOpt.filter(opt -> opt.getBank_code().equals("JPY") && opt.getBuy_spot_rate() <= 0.22).ifPresent(opt -> {
                                context.publishEvent(new RateEvent(this, "BOT", rateData.getBuy_spot_rate()));
                            });
                        },
                        JsonProcessingException.class)));
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void CptRateData() throws JsonProcessingException {
        String values = cpt.GetCptCurrentData();
        JsonNode currentData = objectMapper.readTree(values);
        Optional<JsonNode> currentDataOpt = Optional.of(currentData);
        currentDataOpt.ifPresent(data -> data.forEach(lambdaWarpper(
                value -> {
                    CptRateData rateData = objectMapper.treeToValue(value, CptRateData.class);
                    Optional<CptRateData> dataOpt = Optional.of(rateData);

                    dataOpt.filter(opt -> opt.getBankCode().equals("JPY") && opt.getBuy() <= 0.22).ifPresent(opt -> {
                        context.publishEvent(new RateEvent(this, "CPT", rateData.getBuy()));
                    });
                },
                JsonProcessingException.class)));
    }
}
