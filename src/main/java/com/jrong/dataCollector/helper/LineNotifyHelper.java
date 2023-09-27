package com.jrong.dataCollector.helper;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Component
public class LineNotifyHelper {
    @Value("${lineNotifyToken}")
    private String lineNotifyToken;
    @Value("${lineNotifyUrl}")
    private String lineNotifyUrl;

    public ResponseEntity<JsonNode> SendMessage(String message) {
        String url = lineNotifyUrl;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization","Bearer "+ lineNotifyToken);

        MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("message", message);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(postParameters,headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(url, httpEntity, JsonNode.class);

        return responseEntity;
    }
}
