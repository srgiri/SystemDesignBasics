package com.sam.message;

import com.sam.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    //More than one consumer cannot receive the same message from the same queue
    @RabbitListener(queues = {"${rabbitmq.queue.q1}"})
    public void consumeJsonMessage1(User user) {
        LOGGER.info(String.format("Received JSON message (Queue 1) -> %s", user.toString()));
    }

    //More than one consumer cannot receive the same message from the same queue
    //Any one of the consumers receives and others do not
    @RabbitListener(queues = {"${rabbitmq.queue.q1}"})
    public void consumeJsonMessage1Duplicate(User user) {
        LOGGER.info(String.format("(Duplicate) Received JSON message (Queue 1) -> %s", user.toString()));
    }

    @RabbitListener(queues = {"${rabbitmq.queue.q2}"})
    public void consumeJsonMessage2(User user) {
        LOGGER.info(String.format("Received JSON message (Queue 2) -> %s", user.toString()));
    }
}
