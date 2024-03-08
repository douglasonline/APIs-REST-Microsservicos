package com.example.Notification.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nome da fila
    public static final String QUEUE_NAME = "fila-pagamento";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, false);
    }
}

