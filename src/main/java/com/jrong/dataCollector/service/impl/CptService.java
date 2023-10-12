package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jrong.dataCollector.helper.DataParserHelper;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.service.ICptService;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.jrong.dataCollector.helper.ConvertByteArrayToStringHelper;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CptService implements ICptService {
    @Value("${cptUrl}")
    private String cptUrl;
    @Value("${cptHistoryUrl}")
    private String cptHistoryUrl;

    @Autowired
    private DataParserHelper dataParserHelper;
    @Autowired
    private CheckRateExistService checkRateExistService;

    @Override
    public String GetCptCurrentData() {
        String[] lines =  dataParserHelper.DataParser(cptUrl);
        String data = processData(lines,"current");
        checkRateExistService.CheckRateExist("Cpt Current", data);
        return data;
    }

    @Override
    public String GetCptHistoryData() {
        String[] lines = dataParserHelper.DataParser(cptHistoryUrl);
        String data = processData(lines,"history");
        checkRateExistService.CheckRateExist("Cpt History", data);
        return data;
    }

    private static String processData(String[] lines, String dataType) {
        AtomicReference<String> json = new AtomicReference<>("");

        ObjectMapper jsonArray = new ObjectMapper();
        ArrayNode arrayNode = jsonArray.createArrayNode();

        Arrays.stream(lines).forEach(line -> {
            ObjectNode jsonObject = jsonArray.createObjectNode();

            Optional<String> type = Optional.of(dataType);
            type.filter(t -> t.equals("current")).ifPresent(t -> {
                String[] values = line.split("\\s+");

                jsonObject.put("bankCode", values[0]);
                jsonObject.put("year", values[1]);
                jsonObject.put("month", values[2]);
                jsonObject.put("tenDays", values[3]);
                jsonObject.put("buy", values[4]);
                jsonObject.put("sell", values[5]);
            });
            type.filter(t -> t.equals("history")).ifPresent(t -> {
                String[] values = line.split("");

                var year = values[3]+values[4]+values[5];
                var month = values[6]+values[7];
                var tenDays = values[8];
                var buyString = values[9]+values[10]+values[11]+values[12]+values[13]+values[14]+values[15]+values[16];
                var buy = Double.parseDouble(buyString)/100000.0;
                var sellString = values[17]+values[18]+values[19]+values[20]+values[21]+values[22]+values[23]+values[24];
                var sell = Double.parseDouble(sellString)/100000.0;

                jsonObject.put("bankCode", values[0]+values[1]+values[2]);
                jsonObject.put("year", year );
                jsonObject.put("month", month);
                jsonObject.put("tenDays",tenDays);
                jsonObject.put("buy",buy);
                jsonObject.put("sell",sell);
            });

            arrayNode.addAll(List.of(jsonObject));
            json.set(arrayNode.toString());
        });

        return json.get();
    }
}