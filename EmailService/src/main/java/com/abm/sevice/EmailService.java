package com.abm.sevice;

import com.abm.rabbitmq.model.ActivationCodeModel;
import com.abm.rabbitmq.model.RepasswordCodeModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {


    private  final  JavaMailSender mailSender;

    public void sendEmail(String email,String code){
        SimpleMailMessage message=new SimpleMailMessage();
       message.setTo(email);

        message.setSubject("your activation code");
        message.setText("activation code"+code);
        mailSender.send(message);
    }

  @RabbitListener(queues = "queue.activateCode")
    public void activationCode(ActivationCodeModel activationCodeModel){
      sendEmail(activationCodeModel.getEmail(),activationCodeModel.getActivationCode());
    }

    @RabbitListener(queues = "queue.repasswordCode")
    public void repasswordCode(RepasswordCodeModel repasswordCodeModel){
      sendEmail( repasswordCodeModel.getEmail(),repasswordCodeModel.getRepasswordCode());
    }
}
