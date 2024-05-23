package com.abm.repository;

import com.abm.domain.Post;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends ElasticsearchRepository<Post,String> {
    void deleteByPostId(String postId);
   Optional<Post> findByPostId(String postId);
}
