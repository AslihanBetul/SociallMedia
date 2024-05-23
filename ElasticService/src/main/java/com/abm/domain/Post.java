package com.abm.domain;

import com.abm.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document(indexName = "post-index")
public class Post {
    @Id
    String id;
    String postId;
    String userId;
    @Builder.Default
    PostStatus postStatus =PostStatus.ACTIVE;

    String imageUrl;
    String title;
    String content;

}
