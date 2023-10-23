package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jrong.dataCollector.service.IDataParser;
import com.jrong.dataCollector.service.IDataProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CptHistoryDataProcess implements IDataProcess {
    @Autowired
    private IDataParser dataParser;
    @Autowired
    private CheckRateExist checkRateExist;

    @Override
    public String Process() {
        String[] lines = dataParser.Parser("http://portal.sw.nat.gov.tw/APGQ/GC331!downLoad?formBean.downLoadFile=HISTORY_TXT");

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

        checkRateExist.CheckRateExist("Cpt History", json.get());
        return json.get();
    }
}
