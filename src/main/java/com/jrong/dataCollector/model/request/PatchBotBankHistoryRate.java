package com.jrong.dataCollector.model.request;

public class PatchBotBankHistoryRate {
    private int id;
    private String exchangeContent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExchangeContent() {
        return exchangeContent;
    }

    public void setExchangeContent(String exchangeContent) {
        this.exchangeContent = exchangeContent;
    }

    @Override
    public String toString() {
        return "PatchttopBankExchangerateHistory{" +
                "id=" + id +
                ", exchangeContent='" + exchangeContent + '\'' +
                '}';
    }
}
