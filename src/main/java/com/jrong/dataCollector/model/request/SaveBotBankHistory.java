package com.jrong.dataCollector.model.request;

import lombok.Data;

@Data
public class SaveBotBankHistory {
    private String bank_code;
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
    private String create_date;

    @Override
    public String toString() {
        return "SaveBotBankHistoryRate{" +
                "bank_code='" + bank_code + '\'' +
                ", buy_bank_note_rate=" + buy_bank_note_rate +
                ", buy_spot_rate=" + buy_spot_rate +
                ", buy_10days_forward_rate=" + buy_10days_forward_rate +
                ", buy_30days_forward_rate=" + buy_30days_forward_rate +
                ", buy_60days_forward_rate=" + buy_60days_forward_rate +
                ", buy_90days_forward_rate=" + buy_90days_forward_rate +
                ", buy_120days_forward_rate=" + buy_120days_forward_rate +
                ", buy_150days_forward_rate=" + buy_150days_forward_rate +
                ", buy_180days_forward_rate=" + buy_180days_forward_rate +
                ", sell_bank_note_rate=" + sell_bank_note_rate +
                ", sell_spot_rate=" + sell_spot_rate +
                ", sell_10days_forward_rate=" + sell_10days_forward_rate +
                ", sell_30days_forward_rate=" + sell_30days_forward_rate +
                ", sell_60days_forward_rate=" + sell_60days_forward_rate +
                ", sell_90days_forward_rate=" + sell_90days_forward_rate +
                ", sell_120days_forward_rate=" + sell_120days_forward_rate +
                ", sell_150days_forward_rate=" + sell_150days_forward_rate +
                ", sell_180days_forward_rate=" + sell_180days_forward_rate +
                ", create_date='" + create_date + '\'' +
                '}';
    }
}
