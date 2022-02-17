package com.example.convertservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class RabbitmqConfig {
//    @Value(("${spring.rabbitmq.exchange}"))
//    private String exc;
//    @Value(("${spring.rabbitmq.queue}"))
//    private String queue;
//    @Value(("${spring.rabbitmq.routing-key}"))
//    private String key;
//    @Bean
//    public TopicExchange historyTopic() {
//        return new TopicExchange(exc);
//    }
//    @Bean
//    public Queue Queue() {
//        return new Queue(queue,false);
//    }
//    @Bean
//    public Binding bindingQueueToHistoryTopic(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue)
//                .to(exchange)
//                .with(key);
//    }
//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//
//}
