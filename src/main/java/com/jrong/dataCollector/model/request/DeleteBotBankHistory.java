package com.jrong.dataCollector.model.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DeleteBotBankHistory {
    private LocalDate date;
}
