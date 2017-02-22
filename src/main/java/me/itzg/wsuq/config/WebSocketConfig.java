package me.itzg.wsuq.config;

import me.itzg.wsuq.services.ConsistentUserDestinationResolver;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.UniquelyNamedQueue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.user.UserDestinationResolver;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocket @EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/queue", "/topic", "/exchange", "/amq/queue");
    }

    @Bean
    public MessageConverter eventBusMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public UserDestinationResolver userDestinationResolver() {
        return new ConsistentUserDestinationResolver();
    }

    @Bean
    public Queue userRandomQueue(@Value("${user}") String user) {
        return new Queue(String.format("users.%s.random", user));
    }
}
