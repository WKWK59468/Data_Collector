package com.jrong.dataCollector.listener;

import org.springframework.context.ApplicationEvent;

public class RateEvent extends ApplicationEvent {
    private String serviceName;
    private Double rate;

    public RateEvent(Object source, String serviceName ,Double rate) {
        super(source);
        this.rate = rate;
        this.serviceName = serviceName;
    }

    public Double getRate() {
        return rate;
    }

    public String getServiceName(){
        return serviceName;
    }
}
