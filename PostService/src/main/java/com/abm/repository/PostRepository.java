package com.abm.repository;



import com.abm.entity.Post;

import com.abm.enums.PostStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PostRepository extends MongoRepository<Post,String> {


    List<Post>findAllByUserId(String id);
    Optional<Post>findByUserIdAndId(String userId,String id);
//List<Post>findAllByStatus(PostStatus status);



}

