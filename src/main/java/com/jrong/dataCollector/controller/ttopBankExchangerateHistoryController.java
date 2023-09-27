package com.jrong.dataCollector.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jrong.dataCollector.model.request.DeletettopBankExchangerateHistory;
import com.jrong.dataCollector.model.request.PatchttopBankExchangerateHistory;
import com.jrong.dataCollector.model.response.DataOperatorResponse;
import com.jrong.dataCollector.model.ttopBankExchangerateHistory;
import com.jrong.dataCollector.service.impl.BOTBankService;
import com.jrong.dataCollector.service.impl.CPTService;
import com.jrong.dataCollector.helper.LineNotifyHelper;
import com.jrong.dataCollector.service.impl.ttopBankExchangerateHistoryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ttopBankExchangerateHistory")
public class ttopBankExchangerateHistoryController {
    @Autowired
    private ttopBankExchangerateHistoryService ttopBankExchangerateHistoryService;
    @Autowired
    private BOTBankService botBankService;
    @Autowired
    private CPTService cptService;
    @Autowired
    private LineNotifyHelper lineNotifyHelper;

    @GetMapping("/bot")
    public ResponseEntity<String> GetRateBotData(){
        return ResponseEntity.ok().body(botBankService.GetBotRateData());
    }

    @GetMapping("/current")
    public ResponseEntity<String> GetCurrentData(){
        return ResponseEntity.ok().body(cptService.GetCPTCurrentData());
    }

    @GetMapping
    public ResponseEntity<List<ttopBankExchangerateHistory>> GetttopBankExchangerateHistory(){
        var data = ttopBankExchangerateHistoryService.GetttopBankExchangerateHistory();
        if (data == null) {
            lineNotifyHelper.SendMessage("Get ttopBankExchangerateHistory Failure");
            return ResponseEntity.notFound().build();
        }
        lineNotifyHelper.SendMessage("Get ttopBankExchangerateHistory Success");
        return ResponseEntity.ok().body(data);
    }

    @PostMapping
    public ResponseEntity<DataOperatorResponse> SavettopBankExchangerateHistory() throws JsonProcessingException {
        DataOperatorResponse response = new DataOperatorResponse();
        boolean isSuccess = ttopBankExchangerateHistoryService.SavettopBankExchangerateHistory();
        response.setSuccess(isSuccess);
        if(!isSuccess){
            response.setMessage("Save Exchangerate Failure");
            lineNotifyHelper.SendMessage("Save Exchangerate Failure");
            return ResponseEntity.badRequest().body(response);
        }
        response.setMessage("Save Exchangerate Success");
        lineNotifyHelper.SendMessage("Save Exchangerate Success");
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<DataOperatorResponse> DeletettopBankExchangerateHistory(@NotNull @RequestBody DeletettopBankExchangerateHistory deletettopBankExchangerateHistory){
        DataOperatorResponse response = new DataOperatorResponse();
        boolean isSuccess = ttopBankExchangerateHistoryService.DeletettopBankExchangerateHistory(
                deletettopBankExchangerateHistory.getId()
        );
        response.setSuccess(isSuccess);
        if (!isSuccess){
            response.setMessage("Delete Exchangerate " + deletettopBankExchangerateHistory.getId() + " Failure");
            lineNotifyHelper.SendMessage("Delete Exchangerate " + deletettopBankExchangerateHistory.getId() + " Failure");
            return ResponseEntity.badRequest().body(response);
        }
        response.setMessage("Delete Exchangerate " + deletettopBankExchangerateHistory.getId() + " Success");
        lineNotifyHelper.SendMessage("Delete Exchangerate " + deletettopBankExchangerateHistory.getId() + " Success");
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping
    public ResponseEntity<DataOperatorResponse> UpdatettopBankExchangerateHistory(@NotNull @RequestBody PatchttopBankExchangerateHistory patchttopBankExchangerateHistory){
        DataOperatorResponse response = new DataOperatorResponse();
        boolean isSuccess = ttopBankExchangerateHistoryService.UpdatettopBankExchangerateHistory(
                patchttopBankExchangerateHistory.getId(),
                patchttopBankExchangerateHistory.getExchangeContent()
        );
        response.setSuccess(isSuccess);
        if (!isSuccess){
            response.setMessage("Update Exchangerate " + patchttopBankExchangerateHistory.getId() + " Failure");
            lineNotifyHelper.SendMessage("Update Exchangerate " + patchttopBankExchangerateHistory.getId() + " Failure");
            return ResponseEntity.badRequest().body(response);
        }
        response.setMessage("Update Exchangerate " + patchttopBankExchangerateHistory.getId() + " Success");
        lineNotifyHelper.SendMessage("Update Exchangerate " + patchttopBankExchangerateHistory.getId() + " Success");
        return ResponseEntity.ok().body(response);
    }
}
