package com.jrong.dataCollector.controller;

import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.model.CptRateData;
import com.jrong.dataCollector.service.ICptRate;
import com.jrong.dataCollector.service.ICpt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cptRate")
public class CptRateController {
    @Autowired
    private ICptRate cptRateService;
    @Autowired
    private ICpt cptService;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;

    @GetMapping("/current")
    public ResponseEntity<String> GetCptCurrentData(){
        return cptRateService.GetCptCurrentRate();
    }

    @GetMapping("/history")
    public ResponseEntity<List<CptRateData>> GetCptHistoryData(){
        return cptRateService.GetCptHistoryRate();
    }
}
