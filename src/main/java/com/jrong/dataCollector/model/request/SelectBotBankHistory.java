package com.jrong.dataCollector.model.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SelectBotBankHistory {
    private LocalDate date;
}
