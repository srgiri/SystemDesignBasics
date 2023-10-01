package com.sam.message;

import com.sam.dto.Employee;
import com.sam.dto.User;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    //More than one consumer cannot receive the same message from the same queue
    @RabbitListener(queues = {"${rabbitmq.queue.q1}"}, messageConverter = "converterJson")
    public void consumeJsonMessage1(User user) {
        LOGGER.info("Received JSON message (Queue 1) -> {}", user.toString());
    }

    //More than one consumer cannot receive the same message from the same queue
    //Any one of the consumers receives and others do not
    @RabbitListener(queues = {"${rabbitmq.queue.q1}"}, messageConverter = "converterJson")
    public void consumeJsonMessage1Duplicate(User user) {
        LOGGER.info("(Duplicate) Received JSON message (Queue 1) -> {}", user.toString());
    }

    @RabbitListener(queues = {"${rabbitmq.queue.q2}"}, messageConverter = "converterJson")
    public void consumeJsonMessage2(User user) {
        LOGGER.info("Received JSON message (Queue 2) -> {}", user.toString());
    }

    //When more than one message converter is used, we have to specify 'messageConverter' in the listener.
    //Default converter is 'SimpleMessageConverter' (when nothing is specified and no converter Bean is used)
    //If we have only one Bean for converter, then 'messageConverter' may not be specified
    @RabbitListener(queues = {"${rabbitmq.queue.q3}"}, messageConverter = "converterSimple")
    public void consumeObjectMessage(byte[] byteArray) {
        Employee employee = SerializationUtils.deserialize(byteArray);
        LOGGER.info("Received Byte Array message (Queue 3) -> {}", employee.toString());
    }
}
