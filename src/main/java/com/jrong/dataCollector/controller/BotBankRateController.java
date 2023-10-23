package com.jrong.dataCollector.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jrong.dataCollector.model.request.DeleteBotBankHistory;
import com.jrong.dataCollector.model.request.SelectBotBankHistory;
import com.jrong.dataCollector.model.request.UpdateBotBankHistory;
import com.jrong.dataCollector.model.BotBankRateData;
import com.jrong.dataCollector.service.IBotBankRate;
import com.jrong.dataCollector.service.IBotBank;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/botBankRate")
public class BotBankRateController {
    @Autowired
    private IBotBankRate botBankRateService;
    @Autowired
    private IBotBank botBankService;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;


    @GetMapping("/bot")
    public ResponseEntity<String> GetRateBotData() throws JsonProcessingException {
        return ResponseEntity.ok().body(botBankService.GetBotRateData());
    }

    @GetMapping("/history")
    public ResponseEntity<List<BotBankRateData>> GetBotBankHistoryRate(@RequestBody SelectBotBankHistory selectBotBankHistory){
        return botBankRateService.GetBotBankHistoryRate(selectBotBankHistory);
    }

    @GetMapping("/current")
    public ResponseEntity<String> GetBotBankCurrentRate(){
        return botBankRateService.GetBotBankCurrentRate();
    }

    @PostMapping("/history")
    public ResponseEntity<Boolean> SaveBotBankHistoryRate() throws JsonProcessingException {
        return botBankRateService.SaveBotBankHistoryRate();
    }

    @DeleteMapping("/history")
    public ResponseEntity<Boolean> DeleteBotBankHistoryRate(@RequestBody DeleteBotBankHistory deleteBotBankHistory){
        return botBankRateService.DeleteBotBankHistoryRate(deleteBotBankHistory);
    }

    @PutMapping("/history")
    public ResponseEntity<Boolean> UpdateBotBankHistoryRate(@RequestBody UpdateBotBankHistory updateBotBankHistory){
        return botBankRateService.UpdateBotBankHistoryRate(updateBotBankHistory);
    }
}
