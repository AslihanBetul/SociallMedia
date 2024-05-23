package com.abm.utility;

import com.abm.entity.Post;
import com.abm.mapper.PostMapper;
import com.abm.rabbitmq.model.PostSaveModel;
import com.abm.service.PostService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ElasticDataSender {
    private final PostService postService;
    private final RabbitTemplate rabbitTemplate;
    String directExchange = "direct-exchange";
    String postSaveKey = "save-post-key";

  //  @PostConstruct
    public void send(){
        List<Post>postList=postService.findAll();
        postList.forEach(post ->{convertAndSendSaveModel(PostMapper.INSTANCE.postToPostSaveModel(post));

        });
    }
    private void convertAndSendSaveModel(@RequestBody PostSaveModel postSaveModel){
        rabbitTemplate.convertAndSend(directExchange, postSaveKey, postSaveModel);
    }

}
