package com.abm.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document
@Data

public class BaseEntity {
    @CreatedDate
    @Builder.Default
    LocalDateTime createdAt=LocalDateTime.now();

    @LastModifiedDate
    LocalDateTime updatedAt;

}
