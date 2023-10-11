package com.jrong.dataCollector.listener;

import com.jrong.dataCollector.helper.LineNotifyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RateListener {
    @Autowired
    private LineNotifyHelper lineNotifyHelper;

    @EventListener(RateEvent.class)
    public void BotRateListener(RateEvent rateEvent){
        lineNotifyHelper.SendMessage(rateEvent.getServiceName() + " 當前日元匯率: " + rateEvent.getRate() + ", 監聽目標匯率: 0.22");
    }
}
