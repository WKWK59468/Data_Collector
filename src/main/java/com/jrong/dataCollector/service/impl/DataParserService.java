package com.jrong.dataCollector.service.impl;

import com.jrong.dataCollector.helper.ConvertByteArrayToStringHelper;
import com.jrong.dataCollector.service.IDataParserService;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class DataParserService implements IDataParserService {
    @Autowired
    private ConvertByteArrayToStringHelper convertByteArrayToStringHelper;

    @Override
    public String[] DataParser(String url){
        AtomicReference<String> data = new AtomicReference<>("");
        RestTemplate restTemplate = new RestTemplate();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        factory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(factory);

        ResponseEntity<byte[]> results = restTemplate.getForEntity(url, byte[]. class );

        Optional<Boolean> is2xxSuccessful = Optional.of(results.getStatusCode().is2xxSuccessful());
        is2xxSuccessful.ifPresent(
                successful -> {
                    byte[] responseBody = results.getBody();
                    HttpHeaders headers = results.getHeaders();
                    Optional<MediaType> mediaType = Optional.ofNullable(headers.getContentType());

                    mediaType.filter(mt -> mt.isCompatibleWith(MediaType.TEXT_PLAIN) || mt.isCompatibleWith(MediaType.APPLICATION_OCTET_STREAM))
                            .ifPresent( m -> data.set(convertByteArrayToStringHelper.ConvertWithBOM(responseBody)));
                }
        );

        return Arrays.stream(data.get().split("\n")).skip(1).toArray(String[]::new);
    }
}
