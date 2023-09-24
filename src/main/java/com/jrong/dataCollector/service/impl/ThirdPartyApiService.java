package com.jrong.dataCollector.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrong.dataCollector.service.IThirdPartyApiService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class ThirdPartyApiService implements IThirdPartyApiService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String GetRateBotData() {
        String url ="https://rate.bot.com.tw/xrt/fltxt/0/day" ;
        String data = null;
        ResponseEntity<byte[]> results = restTemplate.exchange(url, HttpMethod.GET, null , byte[]. class );

        if (results.getStatusCode().is2xxSuccessful()) {
            byte[] responseBody = results.getBody();
            HttpHeaders headers = results.getHeaders();
            MediaType mediaType = headers.getContentType();

            // 检查是否是文本类型
            if (mediaType != null && mediaType.isCompatibleWith(MediaType.TEXT_PLAIN)) {
                data = convertByteArrayToStringWithBOM(responseBody);
            }
        }

        String[] lines = data.split("\n");
        JSONArray jsonArray = new JSONArray();

        for (String line : lines) {
            System.out.println(line);
            try {
                String[] values = line.split("\\s+");
                if (values.length == 6) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Currency", values[0]);
                    jsonObject.put("Year", values[1]);
                    jsonObject.put("Month", values[2]);
                    jsonObject.put("TenDay", values[3]);
                    jsonObject.put("Buy", values[4]);
                    jsonObject.put("Sell", values[5]);
                    jsonArray.put(jsonObject);

                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonArray.toString();
    }

    @Override
    public String GetPortalData() {
        return null;
    }

    private static String convertByteArrayToStringWithBOM(byte[] byteArray) {
        try {
            // 使用 ByteArrayInputStream 包装字节数组
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);

            // 使用 InputStreamReader 来处理字节顺序标记（BOM）和编码
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

            // 使用 BufferedReader 读取文本内容
            BufferedReader bufferedReader = new BufferedReader(reader);

            // 读取文本行并拼接到 StringBuilder 中
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            // 关闭资源
            bufferedReader.close();

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
