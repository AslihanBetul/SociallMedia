package com.abm.Manager;

import com.abm.constant.EndPoints;
import com.abm.dto.request.UserSaveRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(url = "http://localhost:9091/api/v1/userprofile",name="userProfileManager",dismiss404 = true)
public interface UserProfileManager {
    @PostMapping(EndPoints.SAVE)
    ResponseEntity<Boolean> save(@RequestBody UserSaveRequestDto dto);
    @PutMapping("/updateStatus/{id}")
     ResponseEntity<Boolean> updateStatus(@PathVariable Long id);

    @DeleteMapping("/delete/{id}")
  ResponseEntity<Void> delete(@PathVariable Long id);


}

