package com.abm.controller;

import com.abm.constant.EndPoints;
import com.abm.dto.request.UserSaveRequestDto;
import com.abm.dto.request.UserUpdateDto;
import com.abm.dto.response.UserProfileResponseDto;
import com.abm.entity.UserProfile;
import com.abm.enums.UserProfileStatus;
import com.abm.exception.ErrorType;
import com.abm.exception.UserProfileServiceException;
import com.abm.repository.UserProfileRepository;
import com.abm.service.UserProfileService;
import jakarta.websocket.Endpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

@RestController
@RequestMapping(EndPoints.USERPROFILE)
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;


    @PostMapping(EndPoints.SAVE)
    public ResponseEntity<Boolean> save(@RequestBody UserSaveRequestDto dto){

            return ResponseEntity.ok( userProfileService.save(dto));
    }
    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<Boolean> updateStatus(@PathVariable Long id){
        try {
            Boolean aBoolean = userProfileService.updateStatus(id);
            return ResponseEntity.ok(aBoolean);
        } catch (UserProfileServiceException e) {
           return ResponseEntity.ok(false);
        }
    }

    @PutMapping("/userUpdate")
    public ResponseEntity<UserProfileResponseDto>updateUser(@RequestBody UserUpdateDto dto){
        return ResponseEntity.ok(userProfileService.updateUser(dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userProfileService.delete(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/getUserIdByAuthId")
    public ResponseEntity<String> getUserIdByAuthId( @RequestParam  Long authId){
        return ResponseEntity.ok(userProfileService.getUserIdByAuthId(authId));
    }

    @GetMapping("/findByUsername")
    public ResponseEntity<UserProfile> findByUsername(@RequestParam String username){
        return ResponseEntity.ok(userProfileService.findByUsername(username));
    }

    @GetMapping("/findByStatus")
    public ResponseEntity <List<UserProfile>> findByStatus(UserProfileStatus status){
        return ResponseEntity.ok(userProfileService.findByStatus(status));


    }

    @GetMapping("/findall")
    public ResponseEntity<List<UserProfileResponseDto>> findAll(){
        return ResponseEntity.ok(userProfileService.findAll());
    }


}
