package com.abm.entity;

import com.abm.enums.UserProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.lang.annotation.Documented;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document
public class UserProfile implements Serializable {
    @MongoId
    String id;
    String authId;
    String username;
    String email;
    String phone;
    String photo;
    String address;
    String about;

    UserProfileStatus status;

}
