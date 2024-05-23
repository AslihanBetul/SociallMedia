package com.abm.entity;

import com.abm.enums.PostStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Document
public class Post extends BaseEntity{
   @MongoId
    String id;
    String userId;

    @Builder.Default
    PostStatus postStatus =PostStatus.ACTIVE;

    String imageUrl;
    String title;
    String content;
}
