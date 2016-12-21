package me.itzg.wsuq.services;

import me.itzg.wsuq.model.CurrentTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CurrentTimeEmitter {

    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public CurrentTimeEmitter(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 1000)
    public void emit() {
        CurrentTime currentTime = new CurrentTime();
        currentTime.setValue(new Date());

        messagingTemplate.convertAndSendToUser("user", "/exchange/amq.direct/current-time", currentTime);
    }
}
