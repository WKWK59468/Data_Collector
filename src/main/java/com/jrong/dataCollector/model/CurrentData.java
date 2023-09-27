package com.jrong.dataCollector.model;

public class CurrentData {
    private String bankCode;
    private String exchangeContent;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getExchangeContent() {
        return exchangeContent;
    }

    public void setExchangeContent(String exchangeContent) {
        this.exchangeContent = exchangeContent;
    }

    @Override
    public String toString() {
        return "CurrentData{" +
                "bankCode='" + bankCode + '\'' +
                ", exchangeContent='" + exchangeContent + '\'' +
                '}';
    }
}
