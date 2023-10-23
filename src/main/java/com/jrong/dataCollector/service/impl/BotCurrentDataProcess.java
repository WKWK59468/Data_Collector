package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jrong.dataCollector.service.IDataParser;
import com.jrong.dataCollector.service.IDataProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class BotCurrentDataProcess implements IDataProcess {
    @Autowired
    private IDataParser dataParser;
    @Autowired
    private CheckRateExist checkRateExist;

    @Override
    public String Process(){
        String[] lines = dataParser.Parser("https://rate.bot.com.tw/xrt/fltxt/0/day");

        AtomicReference<String> json = new AtomicReference<>("");

        ObjectMapper jsonArray = new ObjectMapper();
        ArrayNode arrayNode = jsonArray.createArrayNode();

        Arrays.stream(lines).forEach(line -> {
            ObjectNode jsonObject = jsonArray.createObjectNode();

            String[] values = line.split("\\s+");

            jsonObject.put("bank_code", values[0]);
            jsonObject.put("buy_bank_note_rate", values[2]);
            jsonObject.put("buy_spot_rate", values[3]);
            jsonObject.put("buy_10days_forward_rate", values[4]);
            jsonObject.put("buy_30days_forward_rate", values[5]);
            jsonObject.put("buy_60days_forward_rate", values[6]);
            jsonObject.put("buy_90days_forward_rate", values[7]);
            jsonObject.put("buy_120days_forward_rate", values[8]);
            jsonObject.put("buy_150days_forward_rate", values[9]);
            jsonObject.put("buy_180days_forward_rate", values[10]);
            jsonObject.put("sell_bank_note_rate", values[12]);
            jsonObject.put("sell_spot_rate", values[13]);
            jsonObject.put("sell_10days_forward_rate", values[14]);
            jsonObject.put("sell_30days_forward_rate", values[15]);
            jsonObject.put("sell_60days_forward_rate", values[16]);
            jsonObject.put("sell_90days_forward_rate", values[17]);
            jsonObject.put("sell_120days_forward_rate", values[18]);
            jsonObject.put("sell_150days_forward_rate", values[19]);
            jsonObject.put("sell_180days_forward_rate", values[20]);
            jsonObject.put("create_date", new Timestamp(System.currentTimeMillis()).toString());

            arrayNode.addAll(List.of(jsonObject));
            json.set(arrayNode.toString());
        });

        checkRateExist.CheckRateExist("Bot Rate", json.get());
        return json.get();
    }
}
