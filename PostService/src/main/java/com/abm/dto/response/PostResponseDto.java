package com.abm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PostResponseDto {
    String id;
    String userId;
    String imageUrl;
    String title;
    String content;
    LocalDateTime postDate;
}
