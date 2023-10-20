package com.jrong.dataCollector.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jrong.dataCollector.model.request.DeleteBotBankHistory;
import com.jrong.dataCollector.model.request.SelectBotBankHistory;
import com.jrong.dataCollector.model.request.UpdateBotBankHistory;
import com.jrong.dataCollector.model.response.DataOperatorResponse;
import com.jrong.dataCollector.model.BotBankRateData;
import com.jrong.dataCollector.service.IBotBankRateService;
import com.jrong.dataCollector.service.IBotBankService;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/botBankRate")
public class BotBankRateController {
    @Autowired
    private IBotBankRateService botBankRateService;
    @Autowired
    private IBotBankService botBankService;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;


    @GetMapping("/bot")
    public ResponseEntity<String> GetRateBotData() throws JsonProcessingException {
        return ResponseEntity.ok().body(botBankService.GetBotRateData());
    }

    @GetMapping("/history")
    public ResponseEntity<List<BotBankRateData>> GetBotBankHistoryRate(@RequestBody SelectBotBankHistory selectBotBankHistory){
        return ResponseEntity.ok().body(botBankRateService.GetBotBankHistoryRate(selectBotBankHistory));
    }

    @GetMapping("/current")
    public String GetBotBankCurrentRate(){
        return botBankRateService.GetBotBankCurrentRate();
    }

    @PostMapping("/history")
    public ResponseEntity<DataOperatorResponse> SaveBotBankHistoryRate() throws JsonProcessingException {
        DataOperatorResponse response = new DataOperatorResponse();
        Optional<Boolean> isSuccess = Optional.of(botBankRateService.SaveBotBankHistoryRate());
        isSuccess.ifPresentOrElse(
                success -> {
                    response.setSuccess(isSuccess.get());
                    response.setMessage("Save Success");
                    lineNotifyHelper.SendMessage("Save Success");
                },
                ()-> {
                    response.setSuccess(isSuccess.get());
                    response.setMessage("Save Failure");
                    lineNotifyHelper.SendMessage("Save Failure");
                }
        );
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/history")
    public ResponseEntity<DataOperatorResponse> DeleteBotBankHistoryRate(@RequestBody DeleteBotBankHistory deleteBotBankHistory){
        DataOperatorResponse response = new DataOperatorResponse();
        Optional<Boolean> isSuccess = Optional.of(botBankRateService.DeleteBotBankHistoryRate(deleteBotBankHistory));
        isSuccess.ifPresentOrElse(
                success -> {
                    response.setMessage("Delete Success");
                    lineNotifyHelper.SendMessage("Delete Success");
                },
                ()->{
                    response.setMessage("Delete Failure");
                    lineNotifyHelper.SendMessage("Delete Failure");
                }
        );
        response.setSuccess(isSuccess.get());
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/history")
    public ResponseEntity<DataOperatorResponse> UpdateBotBankHistoryRate(@RequestBody UpdateBotBankHistory updateBotBankHistory){
        DataOperatorResponse response = new DataOperatorResponse();
        Optional<Boolean> isSuccess = Optional.of(botBankRateService.UpdateBotBankHistoryRate(updateBotBankHistory));
        isSuccess.ifPresentOrElse(
                success -> {
                    response.setMessage("Update Success");
                    lineNotifyHelper.SendMessage("Update Success");
                },
                ()->{
                    response.setMessage("Update Failure");
                    lineNotifyHelper.SendMessage("Update Failure");
                }
        );
        response.setSuccess(isSuccess.get());
        return ResponseEntity.ok().body(response);
    }
}
