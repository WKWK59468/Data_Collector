package com.jrong.dataCollector.model.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateBotBankHistory {
    private String bank_code;
    private LocalDate date;
    private Double buy_bank_note_rate;
    private Double buy_spot_rate;
    private Double buy_10days_forward_rate;
    private Double buy_30days_forward_rate;
    private Double buy_60days_forward_rate;
    private Double buy_90days_forward_rate;
    private Double buy_120days_forward_rate;
    private Double buy_150days_forward_rate;
    private Double buy_180days_forward_rate;
    private Double sell_bank_note_rate;
    private Double sell_spot_rate;
    private Double sell_10days_forward_rate;
    private Double sell_30days_forward_rate;
    private Double sell_60days_forward_rate;
    private Double sell_90days_forward_rate;
    private Double sell_120days_forward_rate;
    private Double sell_150days_forward_rate;
    private Double sell_180days_forward_rate;
}
