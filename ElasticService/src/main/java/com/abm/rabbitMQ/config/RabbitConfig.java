package com.abm.rabbitMQ.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration//unutma!!!
public class RabbitConfig {
    String directExchange = "direct-exchange";

    String queuePostSave = "queue-post-save";
    String queuePostUpdate = "queue-post-update";
    String queuePostDelete = "queue-post-delete";






    @Bean
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }



    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }


}
