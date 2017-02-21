# Spring WebSocket/STOMP with User Queues

This is a little Spring Boot application to demonstrate the use of several technologies and techniques:

* [Spring Boot](https://projects.spring.io/spring-boot/)
* [Webjars](http://www.webjars.org/)
* [AngularJS](https://angularjs.org/)
* [WebSocket support in Spring](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html)
* [Spring STOMP use of a full-featured broker](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html#websocket-stomp-handle-broker-relay)
* [RabbitMQ as a STOMP relay](https://www.rabbitmq.com/stomp.html)
* Specific RabbitMQ exchange usage (rather than default)
* [User destinations managed by Spring WebSocket/STOMP](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html#websocket-stomp-user-destination)

To use this Spring Boot application, you will need to also run a STOMP-enabled RabbitMQ instance, which can be
easily done using:

```
docker run -d --name rabbitmq -p 61613:61613 -p 5672:5672 -p 15672:15672 itzg/rabbitmq-stomp
```

or

```
docker-compose up
```

If your RabbitMQ instance/container is running on another host (virtual or external), pass `--spring.rabbitmq.host=HOST`
to override the default value of "localhost".

Being a Spring Application, one of several ways to run it is by using:

```
./mvnw spring-boot:run
```

or on Windows

```
.\mvnw.cmd spring-boot:run
```