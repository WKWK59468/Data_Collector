package com.jrong.dataCollector.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jrong.dataCollector.model.request.DeleteBotBankHistoryRate;
import com.jrong.dataCollector.model.request.PatchBotBankHistoryRate;
import com.jrong.dataCollector.model.response.DataOperatorResponse;
import com.jrong.dataCollector.model.BotBankRateData;
import com.jrong.dataCollector.service.impl.BotBankService;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.service.impl.BotBankRateService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/botBankRate")
public class BotBankRateController {
    @Autowired
    private BotBankRateService botBankRateService;
    @Autowired
    private BotBankService botBankService;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;


    @GetMapping("/bot")
    public ResponseEntity<String> GetRateBotData() {
        return ResponseEntity.ok().body(botBankService.GetBotRateData());
    }

    @GetMapping("/history")
    public ResponseEntity<List<BotBankRateData>> GetBotBankHistoryRate(){
        return ResponseEntity.ok().body(botBankRateService.GetBotBankHistoryRate());
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
                    response.setMessage("Save Bot History Rate Success");
                    lineNotifyHelper.SendMessage("Save Bot History Rate Success");
                },
                ()-> {
                    response.setSuccess(isSuccess.get());
                    response.setMessage("Save Bot History Rate Failure");
                    lineNotifyHelper.SendMessage("Save Bot History Rate Failure");
                }
        );
        return ResponseEntity.ok().body(response);
    }

//    @DeleteMapping("/history")
//    public ResponseEntity<DataOperatorResponse> DeleteBotBankHistoryRate(@NotNull @RequestBody DeleteBotBankHistoryRate deleteBotBankHistoryRate){
//        DataOperatorResponse response = new DataOperatorResponse();
//        boolean isSuccess = BotBankRateService.DeleteBotBankHistoryRate(
//                deleteBotBankHistoryRate.getId()
//        );
//        response.setSuccess(isSuccess);
//        if (!isSuccess){
//            response.setMessage("Delete Exchangerate " + deleteBotBankHistoryRate.getId() + " Failure");
//            lineNotifyHelper.SendMessage("Delete Exchangerate " + deleteBotBankHistoryRate.getId() + " Failure");
//            return ResponseEntity.badRequest().body(response);
//        }
//        response.setMessage("Delete Exchangerate " + deleteBotBankHistoryRate.getId() + " Success");
//        lineNotifyHelper.SendMessage("Delete Exchangerate " + deleteBotBankHistoryRate.getId() + " Success");
//        return ResponseEntity.ok().body(response);
//    }

//    @PatchMapping("/history")
//    public ResponseEntity<DataOperatorResponse> UpdateBotBankHistoryRate(@NotNull @RequestBody PatchBotBankHistoryRate patchBotBankHistoryRate){
//        DataOperatorResponse response = new DataOperatorResponse();
//        boolean isSuccess = BotBankRateService.UpdateBotBankHistoryRate(
//                patchBotBankHistoryRate.getId(),
//                patchBotBankHistoryRate.getExchangeContent()
//        );
//        response.setSuccess(isSuccess);
//        if (!isSuccess){
//            response.setMessage("Update Exchangerate " + patchBotBankHistoryRate.getId() + " Failure");
//            lineNotifyHelper.SendMessage("Update Exchangerate " + patchBotBankHistoryRate.getId() + " Failure");
//            return ResponseEntity.badRequest().body(response);
//        }
//        response.setMessage("Update Exchangerate " + patchBotBankHistoryRate.getId() + " Success");
//        lineNotifyHelper.SendMessage("Update Exchangerate " + patchBotBankHistoryRate.getId() + " Success");
//        return ResponseEntity.ok().body(response);
//    }
}
