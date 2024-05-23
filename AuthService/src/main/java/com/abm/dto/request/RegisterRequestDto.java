package com.abm.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RegisterRequestDto {
    @NotBlank
    @Length(min = 3,max = 30)
    String username;
    @Length(min = 8)
    String password;
    @Email
    String email;
    @Length(min = 8)
    String repassword;
}
