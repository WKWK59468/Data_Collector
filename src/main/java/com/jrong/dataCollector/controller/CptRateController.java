package com.jrong.dataCollector.controller;

import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.model.CptRateData;
import com.jrong.dataCollector.service.impl.CptRateService;
import com.jrong.dataCollector.service.impl.CptService;
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
    private CptRateService cptRateService;
    @Autowired
    private CptService cptService;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;

    @GetMapping("/current")
    public ResponseEntity<String> GetCptCurrentData(){
        return ResponseEntity.ok().body(cptRateService.GetCptCurrentRate());
    }

    @GetMapping("/history")
    public ResponseEntity<List<CptRateData>> GetCptHistoryData(){
        return ResponseEntity.ok().body(cptRateService.GetCptHistoryRate());
    }
}
