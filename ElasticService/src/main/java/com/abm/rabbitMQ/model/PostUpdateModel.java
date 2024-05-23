package com.abm.rabbitMQ.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PostUpdateModel {
    String postId;
    String title;
    String content;
    LocalDateTime updateAt;
}
