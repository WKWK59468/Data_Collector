package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

import java.util.Arrays;

@Service
public class CptService implements ICptService {
    @Value("${cptUrl}")
    private String cptUrl;
    @Value("${cptHistoryUrl}")
    private String cptHistoryUrl;

    @Autowired
    private ConvertByteArrayToStringHelper convertByteArrayToStringHelper;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;
    @Autowired
    private CheckRateExistService checkRateExistService;

    @Override
    public String GetCptCurrentData() {
        String url = cptUrl;
        String data = "";
        RestTemplate restTemplate = new RestTemplate();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        factory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(factory);

        ResponseEntity<byte[]> results = restTemplate.getForEntity(url, byte[].class );

        if (results.getStatusCode().is2xxSuccessful()) {
            byte[] responseBody = results.getBody();
            HttpHeaders headers = results.getHeaders();
            MediaType mediaType = headers.getContentType();

            if (mediaType != null && mediaType.isCompatibleWith(MediaType.APPLICATION_OCTET_STREAM)) {
                data = convertByteArrayToStringHelper.ConvertWithBOM(responseBody);
            }
        }

        String[] lines = data.split("\n");
        ObjectMapper jsonArray = new ObjectMapper();
        ArrayNode arrayNode = jsonArray.createArrayNode();
        String json = "";

        for (int i = 0 ; i < lines.length ; i++) {
            String line = lines[i];
            if(i == 0){continue;}
            try {
                String[] values = line.split("\\s+");

                ObjectNode jsonObject = jsonArray.createObjectNode();

                jsonObject.put("bankCode", values[0]);
                jsonObject.put("exchangeContent", values[1] + "年 " + values[2] + "月 " + values[3] + "旬 買進 " + values[4] + " 賣出 " + values[5]);

                arrayNode.addAll(Arrays.asList(jsonObject));

                json = jsonArray.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
            } catch (Exception  e) {
                lineNotifyHelper.SendMessage("CptService Error: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        checkRateExistService.CheckRateExist(json);
        return json;
    }

    @Override
    public String GetCptHistoryData() {
        String url = cptHistoryUrl;
        String data = "";
        RestTemplate restTemplate = new RestTemplate();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        factory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(factory);

        ResponseEntity<byte[]> results = restTemplate.getForEntity(url, byte[].class );

        if (results.getStatusCode().is2xxSuccessful()) {
            byte[] responseBody = results.getBody();
            HttpHeaders headers = results.getHeaders();
            MediaType mediaType = headers.getContentType();

            if (mediaType != null && mediaType.isCompatibleWith(MediaType.APPLICATION_OCTET_STREAM)) {
                data = convertByteArrayToStringHelper.ConvertWithBOM(responseBody);
            }
        }

        String[] lines = data.split("\n");
        ObjectMapper jsonArray = new ObjectMapper();
        ArrayNode arrayNode = jsonArray.createArrayNode();
        String json = "";

        for (int i = 0 ; i < lines.length ; i++) {
            String line = lines[i];
            try {
                String[] values = line.split("");

                ObjectNode jsonObject = jsonArray.createObjectNode();

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

                arrayNode.addAll(Arrays.asList(jsonObject));

                json = jsonArray.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);

            } catch (Exception  e) {
                lineNotifyHelper.SendMessage("CptRateService Error: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return json;
    }
}