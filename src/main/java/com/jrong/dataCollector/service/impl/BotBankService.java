package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jrong.dataCollector.helper.ConvertByteArrayToStringHelper;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.service.IBotBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Arrays;

@Service
public class BotBankService implements IBotBankService {
    @Value("${botUrl}")
    private String botUrl;
    private final ConvertByteArrayToStringHelper convertByteArrayToStringHelper;
    private final LineNotifyHelper lineNotifyHelper;
    private final CheckRateExistService checkRateExistService;

    @Autowired
    public BotBankService(
            ConvertByteArrayToStringHelper convertByteArrayToStringHelper,
            LineNotifyHelper lineNotifyHelper,
            CheckRateExistService checkRateExistService){
        this.convertByteArrayToStringHelper = convertByteArrayToStringHelper;
        this.lineNotifyHelper = lineNotifyHelper;
        this.checkRateExistService = checkRateExistService;
    }

    @Override
    public String GetBotRateData() {
        String url = botUrl;
        String data = "";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> results = restTemplate.getForEntity(url, byte[]. class );

        if (results.getStatusCode().is2xxSuccessful()) {
            byte[] responseBody = results.getBody();
            HttpHeaders headers = results.getHeaders();
            MediaType mediaType = headers.getContentType();

            if (mediaType != null && mediaType.isCompatibleWith(MediaType.TEXT_PLAIN)) {
                data = convertByteArrayToStringHelper.ConvertWithBOM(responseBody);
            }
        }

        String[] lines = data.split("\n");
        ObjectMapper jsonArray = new ObjectMapper();
        ArrayNode arrayNode = jsonArray.createArrayNode();
        String json = "";

//        Arrays.stream(lines).forEach();

        for (int i = 0 ; i < lines.length ; i++) {
            String line = lines[i];
            if(i == 0){continue;}
            try {
                String[] values = line.split("\\s+");

                ObjectNode jsonObject = jsonArray.createObjectNode();

                String[] subArray = Arrays.copyOfRange(values, 2, values.length);
                String content = values[1];
                for(String value : subArray){
                    content += " " + value;
                }
                jsonObject.put("bankCode", values[0]);
                jsonObject.put("exchangeContent", content);
                jsonObject.put("createDate", new Timestamp(System.currentTimeMillis()).toString());

                arrayNode.addAll(Arrays.asList(jsonObject));

                json = jsonArray.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
            } catch (Exception e) {
                lineNotifyHelper.SendMessage("BotBankService Error: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        checkRateExistService.CheckRateExist(json);
        return json;
    }
}
