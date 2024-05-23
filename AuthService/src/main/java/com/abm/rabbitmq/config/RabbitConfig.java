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
    String directExchangeSocialMediaAuth = "direct-exchange-sociamedia-auth";

    String queueSocialMediaAuth = "queue-socialmedia-auth";

    String queueSocialMediaUserRegisterFromAuth = "queue-socialmedia-userRegisterFromAuth";

    String updateBindingKey = "save-userprofile-key";

    String saveUserRegisterFromAuth = "save-userRegisterFromAuth-key";

    String queueActivateCode= "queue.activateCode";
    String ActivateCode = "activateCode.key";

    //password
    String queueReppaswordCode= "queue.repasswordCode";
    String RepasswordCode = "RepasswordCode.key";
    @Bean
    public Queue  queueReppaswordCode(){
        return new Queue(queueReppaswordCode);
    }

    @Bean
    public Binding bindingReppaswordCode(@Qualifier("queueReppaswordCode") Queue queueReppaswordCode, DirectExchange directExchangeSocialMediaAuth){
        return BindingBuilder.bind(queueReppaswordCode).to(directExchangeSocialMediaAuth).with(RepasswordCode);
    }

    @Bean
    public DirectExchange directExchangeSocialMediaAuth(){
        return new DirectExchange(directExchangeSocialMediaAuth);
    }

    @Bean
    public Queue queueAuth(){
        return new Queue(queueSocialMediaAuth);
    }

    @Bean
    public Queue queueSocialMediaUserRegisterFromAuth(){
        return new Queue(queueSocialMediaUserRegisterFromAuth);
    }

    @Bean
    public Binding bindingSocialMediaDirectExchange(@Qualifier("queueAuth") Queue queueSocialMediaAuth, DirectExchange directExchangeSocialMediaAuth ){
        return BindingBuilder.bind(queueSocialMediaAuth).to(directExchangeSocialMediaAuth).with(updateBindingKey);
    }

    @Bean
    public Binding bindingSocialMediaUserRegister(@Qualifier("queueSocialMediaUserRegisterFromAuth") Queue queueSocialMediaUserRegisterFromAuth, DirectExchange directExchangeSocialMediaAuth ){
        return BindingBuilder.bind(queueSocialMediaUserRegisterFromAuth).to(directExchangeSocialMediaAuth).with(saveUserRegisterFromAuth);
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

    @Bean
    public Queue queueActivateCode(){
        return new Queue(queueActivateCode);
    }
    @Bean
    public Binding bindingActivateCode(@Qualifier("queueActivateCode") Queue queueActivateCode, DirectExchange directExchangeSocialMediaAuth){
        return BindingBuilder.bind(queueActivateCode).to(directExchangeSocialMediaAuth).with(ActivateCode);
    }
}
