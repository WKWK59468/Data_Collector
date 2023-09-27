package com.jrong.dataCollector.model;

public class BotRateData {
    private String bankCode;
    private String exchangeContent;
    private String createDate;

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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "CurrentData{" +
                "bankCode='" + bankCode + '\'' +
                ", exchangeContent='" + exchangeContent + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
