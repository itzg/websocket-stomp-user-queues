package me.itzg.wsuq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebsocketStompUserQueuesApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketStompUserQueuesApplication.class, args);
	}
}
