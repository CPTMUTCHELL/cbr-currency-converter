package com.example.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        value = "rabbitmq.enable",
        havingValue = "true"
)
public class RabbitmqConfig {
    @Value(("${rabbitmq.exchange}"))
    private String exc;
    @Value(("${rabbitmq.queue}"))
    private String queue;
    @Value(("${rabbitmq.routing-key}"))
    private String key;

    @Value(("${rabbitmq.host}"))
    private String host;
    @Value(("${rabbitmq.username}"))
    private String username;
    @Value(("${rabbitmq.password}"))
    private String password;
    @Value(("${rabbitmq.port}"))
    private String port;

    @Bean
    public TopicExchange historyTopic() {
        return new TopicExchange(exc);
    }

    @Bean
    public Queue Queue() {
        return new Queue(queue, true);
    }

    @Bean
    public Binding bindingQueueToHistoryTopic(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(key);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(Integer.parseInt(port));
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        var rabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        rabbitListenerContainerFactory.setMessageConverter(jsonMessageConverter());
        rabbitListenerContainerFactory.setConnectionFactory(connectionFactory());
        return rabbitListenerContainerFactory;

    }


    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setConnectionFactory(connectionFactory());
        return rabbitTemplate;
    }


}
