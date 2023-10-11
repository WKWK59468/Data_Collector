package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jrong.dataCollector.helper.DataParserHelper;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.service.IBotBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class BotBankService implements IBotBankService {
    @Value("${botUrl}")
    private String botUrl;
    @Autowired
    private DataParserHelper dataParserHelper;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;
    @Autowired
    private CheckRateExistService checkRateExistService;

    @Override
    public String GetBotRateData() {
        String[] lines = dataParserHelper.DataParser(botUrl);
        String data = dataProcess(lines);
        checkRateExistService.CheckRateExist("Bot Rate", data);
        return data;
    }

    private static String dataProcess(String[] lines){
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
