package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jrong.dataCollector.service.IDataParser;
import com.jrong.dataCollector.service.IDataProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class CptCurrentDataProcess implements IDataProcess {
    @Autowired
    private IDataParser dataParser;
    @Autowired
    private CheckRateExist checkRateExist;

    @Override
    public String Process() {
        String[] lines =  dataParser.Parser("http://portal.sw.nat.gov.tw/APGQ/GC331!downLoad?formBean.downLoadFile=CURRENT_TXT");

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

        checkRateExist.CheckRateExist("Cpt Current", json.get());
        return json.get();
    }
}
