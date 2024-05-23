package com.abm.repository;

import com.abm.entity.UserProfile;

import com.abm.enums.UserProfileStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends MongoRepository<UserProfile,String> {
Optional<UserProfile>findByAuthId(String authId);


    Optional<UserProfile> findByUsername(String username);

    List<UserProfile> findByStatus(UserProfileStatus status);
}

