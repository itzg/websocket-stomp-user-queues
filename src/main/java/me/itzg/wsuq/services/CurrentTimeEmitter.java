package me.itzg.wsuq.services;

import me.itzg.wsuq.model.CurrentTime;
import me.itzg.wsuq.model.MemoryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CurrentTimeEmitter implements ApplicationListener<BrokerAvailabilityEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentTimeEmitter.class);

    private SimpMessagingTemplate messagingTemplate;
    private boolean brokerAvailable;

    @Autowired
    public CurrentTimeEmitter(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 1000)
    public void emit() {
        if (brokerAvailable) {
            CurrentTime currentTime = new CurrentTime();
            currentTime.setValue(new Date());

            messagingTemplate.convertAndSendToUser("me", "/exchange/amq.direct/current-time", currentTime);

            // Also wanted to exercise a non-user specific topic message. Using free memory just to provide a steadily
            // changing value.
            messagingTemplate.convertAndSend("/topic/memory", new MemoryInfo(Runtime.getRuntime().freeMemory()));
        }
        else {
            LOGGER.warn("Broker is not yet available");
        }
    }

    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent brokerAvailabilityEvent) {
        brokerAvailable = brokerAvailabilityEvent.isBrokerAvailable();
    }
}
