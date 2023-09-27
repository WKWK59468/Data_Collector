package com.jrong.dataCollector.model.response;

public class DataOperatorResponse {
    boolean IsSuccess;
    String Message;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public void setSuccess(boolean success) {
        IsSuccess = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
