package com.abm.service;

import com.abm.domain.Post;
import com.abm.mapper.PostMapper;
import com.abm.rabbitMQ.model.PostSaveModel;
import com.abm.rabbitMQ.model.PostUpdateModel;
import com.abm.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final String queuePostSave = "queue-post-save";
    private final String queuePostUpdate = "queue-post-update";
    private final String queuePostDelete = "queue-post-delete";

    public void save(Post post){
        postRepository.save(post);
    }
    @RabbitListener(queues = queuePostSave)
    public void queuePostSave(PostSaveModel postSaveModel){
        save(PostMapper.INSTANCE.postSaveModelToPost(postSaveModel));
    }


    public void updatePost(Post post){
        post.setId(postRepository.findByPostId(post.getPostId()).orElse(null).getId());
        postRepository.save(post);
    }
    @RabbitListener(queues = queuePostUpdate)
    public void queuePostUpdate(PostUpdateModel postUpdateModel){
        updatePost(PostMapper.INSTANCE.postUpdateModelToPost(postUpdateModel));
    }

    public void deletePostById(String postId){
        postRepository.deleteByPostId(postId);
    }
    @RabbitListener(queues = queuePostDelete )
    public void queuePostDelete(String postId){
        deletePostById(postId);
    }

}
