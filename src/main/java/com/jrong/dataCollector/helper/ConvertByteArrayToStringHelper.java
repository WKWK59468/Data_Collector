package com.jrong.dataCollector.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class ConvertByteArrayToStringHelper {
    @Autowired
    private LineNotifyHelper lineNotifyHelper;

    public String ConvertWithBOM(byte[] byteArray) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);

            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            bufferedReader.close();

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            lineNotifyHelper.SendMessage("ConvertByteArrayToStringHelper Error: " + e.toString());
            return null;
        }
    }
}
