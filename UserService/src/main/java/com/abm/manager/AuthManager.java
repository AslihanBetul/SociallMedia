package com.abm.manager;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "http://localhost:9090/api/v1/auth",name = "authManager",dismiss404 = true)
public interface AuthManager {
    @PutMapping("/updateEmail")
   ResponseEntity<Void> updateEmail(@RequestParam String email, @RequestParam Long authId);

}
