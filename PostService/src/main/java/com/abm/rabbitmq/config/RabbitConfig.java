package com.abm.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    String directExchange = "direct-exchange";

    String queuePostSave = "queue-post-save";
    String queuePostUpdate = "queue-post-update";
    String queuePostDelete = "queue-post-delete";

    String postSaveKey = "save-post-key";
    String postUpdateKey = "update-post-key";
    String postDeleteKey = "delete-post-key";


    @Bean
    public Queue  queuePostSave(){
        return new Queue(queuePostSave);
    }

    @Bean
    public Queue  queuePostUpdate(){
        return new Queue(queuePostUpdate);
    }

    @Bean
    public Queue  queuePostDelete(){
        return new Queue(queuePostDelete);
    }


    @Bean
    public Binding bindingPostSave( Queue queuePostSave, DirectExchange directExchange){
        return BindingBuilder.bind(queuePostSave).to(directExchange).with(postSaveKey);
    }
    @Bean
    public Binding bindingPostUpdate( Queue queuePostUpdate, DirectExchange directExchange){
        return BindingBuilder.bind(queuePostUpdate).to(directExchange).with(postUpdateKey);
    }

    @Bean
    public Binding bindingPostDelete( Queue queuePostDelete, DirectExchange directExchange){
        return BindingBuilder.bind(queuePostDelete).to(directExchange).with(postDeleteKey);
    }


    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(directExchange);
    }


    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }


}
