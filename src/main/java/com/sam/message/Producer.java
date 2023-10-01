package com.sam.message;

import com.sam.config.RabbitMQConfig;
import com.sam.dto.Employee;
import com.sam.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.Objects;

@Component
public class Producer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    @Autowired
    RabbitMQConfig rabbitMQConfig;

    @Autowired
    RabbitTemplate rabbitTemplateJSON;

    @Autowired
    RabbitTemplate rabbitTemplateObj;

    public void sendJsonMessage(User user, int routingKey) {
        if (routingKey == 0) { //fanout
            //We do not need to specify the routing key here as message is published to all queues.
            rabbitTemplateJSON.convertAndSend(rabbitMQConfig.getFanOutExchange(), "", user);
            LOGGER.info("Json message sent (Fanout) -> {}", user);
        } else if (routingKey == 1) { //Queue-1
            rabbitTemplateJSON.convertAndSend(rabbitMQConfig.getDirectExchange(), rabbitMQConfig.getRoutingKey1(), user);
            LOGGER.info("Json message sent (routing key 1) -> {}", user);
        } else if (routingKey == 2) { //Queue-2
            rabbitTemplateJSON.convertAndSend(rabbitMQConfig.getDirectExchange(), rabbitMQConfig.getRoutingKey2(), user);
            LOGGER.info("Json message sent (routing key 2) -> {}", user);
        } else {
            LOGGER.warn("Unrecognized routing key {}", routingKey);
        }
    }

    //The rabbitTemplate adds a header in the message as per the used converter type. This is used by the listener to convert
    public void sendObjectMessage(Employee employee) {
        byte[] byteArray = Objects.requireNonNull(SerializationUtils.serialize(employee));
        //Queue-3
        rabbitTemplateObj.convertAndSend(rabbitMQConfig.getDirectObjExchange(), rabbitMQConfig.getRoutingKey3(), byteArray);
        LOGGER.info("Json message sent (routing key 3) -> {}", employee);
    }
}
