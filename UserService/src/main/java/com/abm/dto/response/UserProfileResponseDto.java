package com.abm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserProfileResponseDto {
      String username;
    String phone;
    String photo;
    String address;
    String about;
    String email;
}
