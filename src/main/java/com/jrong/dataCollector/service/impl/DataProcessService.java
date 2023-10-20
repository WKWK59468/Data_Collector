package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jrong.dataCollector.service.IDataParserService;
import com.jrong.dataCollector.service.IDataProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class DataProcessService implements IDataProcessService {
    @Value("${cptUrl}")
    private String cptUrl;
    @Value("${cptHistoryUrl}")
    private String cptHistoryUrl;
    @Value("${botUrl}")
    private String botUrl;
    @Autowired
    private IDataParserService dataParser;

    @Override
    public String CptCurrentDataProcess() {
        String[] lines =  dataParser.DataParser(cptUrl);

        AtomicReference<String> json = new AtomicReference<>("");

        ObjectMapper jsonArray = new ObjectMapper();
        ArrayNode arrayNode = jsonArray.createArrayNode();

        Arrays.stream(lines).forEach(line -> {
            ObjectNode jsonObject = jsonArray.createObjectNode();


            String[] values = line.split("\\s+");

            jsonObject.put("bankCode", values[0]);
            jsonObject.put("year", values[1]);
            jsonObject.put("month", values[2]);
            jsonObject.put("tenDays", values[3]);
            jsonObject.put("buy", values[4]);
            jsonObject.put("sell", values[5]);

            arrayNode.addAll(List.of(jsonObject));
            json.set(arrayNode.toString());
        });

        return json.get();
    }

    @Override
    public String CptHistoryDataProcess() {
        String[] lines = dataParser.DataParser(cptHistoryUrl);

        AtomicReference<String> json = new AtomicReference<>("");

        ObjectMapper jsonArray = new ObjectMapper();
        ArrayNode arrayNode = jsonArray.createArrayNode();

        Arrays.stream(lines).forEach(line -> {
            ObjectNode jsonObject = jsonArray.createObjectNode();

            String[] values = line.split("");

            var year = values[3] + values[4] + values[5];
            var month = values[6] + values[7];
            var tenDays = values[8];
            var buyString = values[9] + values[10] + values[11] + values[12] + values[13] + values[14] + values[15] + values[16];
            var buy = Double.parseDouble(buyString) / 100000.0;
            var sellString = values[17] + values[18] + values[19] + values[20] + values[21] + values[22] + values[23] + values[24];
            var sell = Double.parseDouble(sellString) / 100000.0;

            jsonObject.put("bankCode", values[0] + values[1] + values[2]);
            jsonObject.put("year", year);
            jsonObject.put("month", month);
            jsonObject.put("tenDays", tenDays);
            jsonObject.put("buy", buy);
            jsonObject.put("sell", sell);

            arrayNode.addAll(List.of(jsonObject));
            json.set(arrayNode.toString());
        });

        return json.get();
    }

    @Override
    public String BotCurrentDataProcess() {
        String[] lines = dataParser.DataParser(botUrl);

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

        return json.get();
    }
}
