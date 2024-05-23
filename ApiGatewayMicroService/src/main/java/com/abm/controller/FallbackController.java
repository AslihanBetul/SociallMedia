package com.abm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fallback")

public class FallbackController {
    @GetMapping("/auth")
    public ResponseEntity<String>getFallbackAuth(){
       // return ResponseEntity.internalServerError().body("Auth service şuan hizmet verememektedir.Lütfen sonra tekrar deneyiniz");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Auth service şuan hizmet verememektedir");
    }

    @GetMapping("/userprofile")
    public ResponseEntity<String>getFallbackUserProfile(){
        //return ResponseEntity.internalServerError().body("UserProfile service şuan hizmet verememektedir.Lütfen sonra tekrar deneyiniz");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("UserProfile service şuan hizmet verememektedir");
    }

    @GetMapping("/post")
    public ResponseEntity<String>getFallbackPost(){
        //return ResponseEntity.internalServerError().body("Post service şuan hizmet verememektedir.Lütfen sonra tekrar deneyiniz");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Post service şuan hizmet verememektedir");
    }
}
