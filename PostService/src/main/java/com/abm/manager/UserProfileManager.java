package com.abm.manager;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "http://localhost:9091/api/v1/userprofile",name="userProfileManager",dismiss404 = true)
public interface UserProfileManager {
    @GetMapping("/getUserIdByAuthId")
     ResponseEntity<String> getUserIdByAuthId( @RequestParam Long authId);
}
