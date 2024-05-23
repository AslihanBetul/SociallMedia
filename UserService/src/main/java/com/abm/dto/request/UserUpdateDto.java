package com.abm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class UserUpdateDto {
    String token;
    String phone;
    String photo;
    String address;
    String about;
    String email;



}
