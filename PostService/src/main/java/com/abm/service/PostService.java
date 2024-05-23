package com.abm.service;

import com.abm.constant.messages.SuccessMessages;
import com.abm.dto.request.PostSaveDto;
import com.abm.dto.request.PostUpdateDto;
import com.abm.dto.response.PostResponseDto;
import com.abm.entity.Post;
import com.abm.enums.PostStatus;
import com.abm.exception.ErrorType;
import com.abm.exception.PostServiceServiceException;
import com.abm.manager.UserProfileManager;
import com.abm.mapper.PostMapper;
import com.abm.rabbitmq.model.PostSaveModel;
import com.abm.rabbitmq.model.PostUpdateModel;
import com.abm.repository.PostRepository;
import com.abm.utility.JwtTokenManger;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final JwtTokenManger jwtTokenManger;
    private final UserProfileManager userProfileManger;
    private final RabbitTemplate rabbitTemplate;
    private final  String directExchange = "direct-exchange";
    private final String postSaveKey = "save-post-key";
    private final  String postUpdateKey = "update-post-key";
    private final  String postDeleteKey = "delete-post-key";

    @Transactional
    public String savePost(PostSaveDto dto) {
        Long authId = jwtTokenManger.getAuthIdFromToken(dto.getToken()).orElseThrow(() -> new PostServiceServiceException(ErrorType.TOKEN_VERIFY_FAILED));

        Post post = postRepository.save(Post.builder().userId(userProfileManger.getUserIdByAuthId(authId).getBody())
                .title(dto.getTitle()).content(dto.getContent()).imageUrl(dto.getImageUrl()).build());
        convertAndSendSaveModel(PostMapper.INSTANCE.postToPostSaveModel(post));
        return SuccessMessages.POST_SUCCESS;
    }

    private void convertAndSendSaveModel(@RequestBody PostSaveModel postSaveModel){
        rabbitTemplate.convertAndSend(directExchange, postSaveKey, postSaveModel);
    }

       @Transactional
    public List<PostResponseDto> getMyPost(String token) {
        Long authId = jwtTokenManger.getAuthIdFromToken(token).orElseThrow(() -> new PostServiceServiceException(ErrorType.TOKEN_VERIFY_FAILED));
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        postRepository.findAllByUserId(userProfileManger.getUserIdByAuthId(authId).getBody()).forEach(post ->
        {
            if (post.getPostStatus().equals(PostStatus.ACTIVE)) {
                postResponseDtoList.add(PostMapper.INSTANCE.postToPostResponseDto(post));
            }
        });
        if (postResponseDtoList.isEmpty()) {
            throw new PostServiceServiceException(ErrorType.USER_HAS_NOT_POST);
        }
        return postResponseDtoList;
    }
     @Transactional
    public List<PostResponseDto> findAll(String token) {
        jwtTokenManger.getAuthIdFromToken(token).orElseThrow(() -> new PostServiceServiceException(ErrorType.TOKEN_VERIFY_FAILED));
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        postRepository.findAll().forEach(post -> {
            if (post.getPostStatus().equals(PostStatus.ACTIVE)) {
                postResponseDtoList.add(PostMapper.INSTANCE.postToPostResponseDto(post));
            }
        });

        if (postResponseDtoList.isEmpty()) {
            throw new PostServiceServiceException(ErrorType.USER_HAS_NOT_POST);
        }
        return postResponseDtoList;
    }
    @Transactional
    public String deletePost(String token,String postId){
        Long authId = jwtTokenManger.getAuthIdFromToken(token).orElseThrow(() -> new PostServiceServiceException(ErrorType.TOKEN_VERIFY_FAILED));
        Post post = postRepository.findByUserIdAndId(userProfileManger.getUserIdByAuthId(authId).getBody(), postId).orElseThrow(() -> new PostServiceServiceException(ErrorType.POST_NOT_FOUND));
        post.setPostStatus(PostStatus.DELETED);
        postRepository.save(post);
        convertAndSendPostId(post.getId());
        return SuccessMessages.POST_DELETE_SUCCESS;
    }
    private void convertAndSendPostId(@RequestParam String postId){
        rabbitTemplate.convertAndSend(directExchange, postDeleteKey, postId);
    }

    @Transactional
    public PostResponseDto updatePost(PostUpdateDto dto) {

        Long authId = jwtTokenManger.getAuthIdFromToken(dto.getToken()).orElseThrow(() -> new PostServiceServiceException(ErrorType.TOKEN_VERIFY_FAILED));
        Post post = postRepository.findByUserIdAndId(userProfileManger.getUserIdByAuthId(authId).getBody(), dto.getPostId()).orElseThrow(() ->
                new PostServiceServiceException(ErrorType.POST_NOT_FOUND));
        if (!post.getPostStatus().equals(PostStatus.ACTIVE)){
            throw new PostServiceServiceException(ErrorType.POST_UPDATE_FAILED);
        }
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        Post saved = postRepository.save(post);
        convertAndSendUpdateModel(PostMapper.INSTANCE.postToPostUpdateModel(saved));
        return PostMapper.INSTANCE.postToPostResponseDto(saved);

    }

    private void convertAndSendUpdateModel(@RequestBody PostUpdateModel postUpdateModel) {
        rabbitTemplate.convertAndSend(directExchange,postUpdateKey,postUpdateModel);
    }

    public List<Post>findAll(){
        return postRepository.findAll();
    }
}
