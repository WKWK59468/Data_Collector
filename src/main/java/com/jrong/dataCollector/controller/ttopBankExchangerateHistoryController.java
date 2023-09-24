package com.jrong.dataCollector.controller;

import com.jrong.dataCollector.model.CreatettopBankExchangerateHistoryParameter;
import com.jrong.dataCollector.model.ttopBankExchangerateHistory;
import com.jrong.dataCollector.service.impl.ThirdPartyApiService;
import com.jrong.dataCollector.service.impl.ttopBankExchangerateHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ttopBankExchangerateHistory")
public class ttopBankExchangerateHistoryController {
    @Autowired
    private ttopBankExchangerateHistoryService _ttopBankExchangerateHistoryService;

    @Autowired
    private ThirdPartyApiService thirdPartyApiService;

    @GetMapping("/bot")
    public String GetRateBotData(){
        return thirdPartyApiService.GetRateBotData();
    }

    @GetMapping
    public List<ttopBankExchangerateHistory> GetttopBankExchangerateHistory(){
        return _ttopBankExchangerateHistoryService.GetttopBankExchangerateHistory();
    }

    @PostMapping
    public void CreatettopBankExchangerateHistory(@RequestBody CreatettopBankExchangerateHistoryParameter ttopBankExchangerateHistory){
        _ttopBankExchangerateHistoryService.CreatettopBankExchangerateHistory(ttopBankExchangerateHistory);
    }
}
