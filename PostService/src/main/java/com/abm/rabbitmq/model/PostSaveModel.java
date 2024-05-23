package com.abm.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PostSaveModel {

    String postId;
    String userId;
    String imageUrl;
    String title;
    String content;
    String status;
    LocalDateTime creatAt;
    LocalDateTime updateAt;
}
