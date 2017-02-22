package me.itzg.wsuq.services;

import me.itzg.wsuq.model.ValueHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * This service acts like it is running external to the web application and only has access to
 * {@link RabbitTemplate} for sending messages to a user connected via the web app to STOMP.
 */
@Service
public class ExternalMessageProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalMessageProducer.class);

    private final Random rand = new Random();

    private final RabbitTemplate rabbitTemplate;

    private String user;

    @Autowired
    public ExternalMessageProducer(RabbitTemplate rabbitTemplate, @Value("${user}") String user) {
        this.rabbitTemplate = rabbitTemplate;
        this.user = user;
    }

    @Scheduled(fixedRate = 1000)
    public void randomEmitter() {
        LOGGER.debug("Sending random value to user {}", user);
        final ValueHolder valueHolder = new ValueHolder();
        valueHolder.setValue(rand.nextInt(100));
        rabbitTemplate.convertAndSend(String.format("users.%s.random", user), valueHolder);
    }
}
