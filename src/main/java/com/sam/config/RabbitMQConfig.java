package com.sam.config;

import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.q1}")
    @Getter
    private String queue1;

    @Value("${rabbitmq.queue.q2}")
    @Getter
    private String queue2;

    @Value("${rabbitmq.exchange.direct}")
    @Getter
    private String directExchange;

    @Value("${rabbitmq.exchange.fanOut}")
    @Getter
    private String fanOutExchange;

    @Value("${rabbitmq.routing.key.k1}")
    @Getter
    private String routingKey1;

    @Value("${rabbitmq.routing.key.k2}")
    @Getter
    private String routingKey2;


    @Bean
    public Queue queue1() {
        return new Queue(queue1);
    }

    @Bean
    public Queue queue2() {
        return new Queue(queue2);
    }


    @Bean
    public TopicExchange directExchange() {
        return new TopicExchange(directExchange);
    }

    @Bean
    public FanoutExchange fanOutExchange() {
        return new FanoutExchange(fanOutExchange);
    }

    // relationship between an exchange, key and a queue
    @Bean
    public Binding jsonBinding1() {
        return BindingBuilder
                .bind(queue1())
                .to(directExchange())
                .with(routingKey1);
    }

    @Bean
    public Binding jsonBinding2() {
        return BindingBuilder
                .bind(queue2())
                .to(directExchange())
                .with(routingKey2);
    }

    //fanout binding
    @Bean
    public Binding fanOutBinding1(FanoutExchange fanOutExchange,
                                  Queue queue1) {
        return BindingBuilder.bind(queue1).to(fanOutExchange);
    }

    @Bean
    public Binding fanOutBinding2(FanoutExchange fanOutExchange,
                                  Queue queue2) {
        return BindingBuilder.bind(queue2).to(fanOutExchange);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
