package com.jrong.dataCollector.model;

public class CptRateData {
    private String bankCode;
    private String year;
    private String month;
    private String tenDays;
    private Double buy;
    private Double sell;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTenDays() {
        return tenDays;
    }

    public void setTenDays(String tenDays) {
        this.tenDays = tenDays;
    }

    public Double getBuy() {
        return buy;
    }

    public void setBuy(Double buy) {
        this.buy = buy;
    }

    public Double getSell() {
        return sell;
    }

    public void setSell(Double sell) {
        this.sell = sell;
    }

    @Override
    public String toString() {
        return "CptRateData{" +
                "bankCode='" + bankCode + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", tenDays='" + tenDays + '\'' +
                ", buy=" + buy +
                ", sell=" + sell +
                '}';
    }
}
