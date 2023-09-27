package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.service.ICPTService;
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
public class CPTService implements ICPTService {
    @Value("${cptUrl}")
    private String cptUrl;

    @Autowired
    private ConvertByteArrayToStringHelper convertByteArrayToStringHelper;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;

    @Override
    public String GetCPTCurrentData() {
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
                lineNotifyHelper.SendMessage("BOTBankService Error: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return json;
    }
}
