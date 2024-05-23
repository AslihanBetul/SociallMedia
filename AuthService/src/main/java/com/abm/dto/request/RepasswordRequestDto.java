package com.abm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RepasswordRequestDto {
    String email;
    String rePasswordCode;
    String password;
    String confirmPassword;
}
