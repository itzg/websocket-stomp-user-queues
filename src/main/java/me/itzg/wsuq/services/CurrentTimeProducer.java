package me.itzg.wsuq.services;

import me.itzg.wsuq.model.CurrentTime;
import me.itzg.wsuq.model.MemoryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * This service represents a messaging source that would run within the web application and as such
 * can use the {@link SimpMessagingTemplate} to send messages to users.
 */
@Service
public class CurrentTimeProducer implements ApplicationListener<BrokerAvailabilityEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentTimeProducer.class);

    private SimpMessagingTemplate messagingTemplate;
    private final String user;
    private boolean brokerAvailable;

    @Autowired
    public CurrentTimeProducer(SimpMessagingTemplate messagingTemplate, @Value("${user}") String user) {
        this.messagingTemplate = messagingTemplate;
        this.user = user;
    }

    @Scheduled(fixedRate = 1000)
    public void emit() {
        if (brokerAvailable) {
            CurrentTime currentTime = new CurrentTime();
            currentTime.setValue(new Date());

            messagingTemplate.convertAndSendToUser(user, "/exchange/amq.direct/current-time", currentTime);

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
