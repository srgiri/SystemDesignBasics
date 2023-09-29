package com.sam.message;

import com.sam.config.RabbitMQConfig;
import com.sam.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Producer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    @Autowired
    RabbitMQConfig rabbitMQConfig;

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendJsonMessage(User user, int routingKey) {
        if (routingKey == 0) { //fanout
            //We do not need to specify the routing key here as message is published to all queues.
            rabbitTemplate.convertAndSend(rabbitMQConfig.getFanOutExchange(), "", user);
            LOGGER.info(String.format("Json message sent (Fanout) -> %s", user));
        } else if (routingKey == 1) { //Queue-1
            rabbitTemplate.convertAndSend(rabbitMQConfig.getDirectExchange(), rabbitMQConfig.getRoutingKey1(), user);
            LOGGER.info(String.format("Json message sent (routing key 1) -> %s", user));
        } else if (routingKey == 2) { //Queue-2
            rabbitTemplate.convertAndSend(rabbitMQConfig.getDirectExchange(), rabbitMQConfig.getRoutingKey2(), user);
            LOGGER.info(String.format("Json message sent (routing key 2) -> %s", user));
        } else {
            LOGGER.warn(String.format("Unrecognized routing key %d", routingKey));
        }
    }
}
