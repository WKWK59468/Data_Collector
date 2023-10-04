package com.jrong.dataCollector.controller;

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

@RestController
@RequestMapping("/botBankRate")
public class BotBankRateController {
    private final BotBankRateService BotBankRateService;
    private final BotBankService botBankService;
    private final LineNotifyHelper lineNotifyHelper;

    @Autowired
    public BotBankRateController(
            BotBankRateService botBankRateService,
            BotBankService botBankService,
            LineNotifyHelper lineNotifyHelper){
        BotBankRateService = botBankRateService;
        this.botBankService = botBankService;
        this.lineNotifyHelper = lineNotifyHelper;
    }

    @GetMapping("/bot")
    public ResponseEntity<String> GetRateBotData(){
        return ResponseEntity.ok().body(botBankService.GetBotRateData());
    }


    @GetMapping("/history")
    public ResponseEntity<List<BotBankRateData>> GetBotBankHistoryRate(){
        var data = BotBankRateService.GetBotBankHistoryRate();
        if (data == null) {
            lineNotifyHelper.SendMessage("Get BotBank History Rate Failure");
            return ResponseEntity.notFound().build();
        }
        lineNotifyHelper.SendMessage("Get BotBank History Rate Success");
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/current")
    public String GetBotBankCurrentRate(){
        return BotBankRateService.GetBotBankCurrentRate();
    }

    @PostMapping("/history")
    public ResponseEntity<DataOperatorResponse> SaveBotBankHistoryRate(){
        DataOperatorResponse response = new DataOperatorResponse();
        boolean isSuccess = BotBankRateService.SaveBotBankHistoryRate();
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

    @DeleteMapping("/history")
    public ResponseEntity<DataOperatorResponse> DeleteBotBankHistoryRate(@NotNull @RequestBody DeleteBotBankHistoryRate deleteBotBankHistoryRate){
        DataOperatorResponse response = new DataOperatorResponse();
        boolean isSuccess = BotBankRateService.DeleteBotBankHistoryRate(
                deleteBotBankHistoryRate.getId()
        );
        response.setSuccess(isSuccess);
        if (!isSuccess){
            response.setMessage("Delete Exchangerate " + deleteBotBankHistoryRate.getId() + " Failure");
            lineNotifyHelper.SendMessage("Delete Exchangerate " + deleteBotBankHistoryRate.getId() + " Failure");
            return ResponseEntity.badRequest().body(response);
        }
        response.setMessage("Delete Exchangerate " + deleteBotBankHistoryRate.getId() + " Success");
        lineNotifyHelper.SendMessage("Delete Exchangerate " + deleteBotBankHistoryRate.getId() + " Success");
        return ResponseEntity.ok().body(response);
    }

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
