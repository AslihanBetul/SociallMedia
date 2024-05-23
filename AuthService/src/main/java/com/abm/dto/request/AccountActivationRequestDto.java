package com.abm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AccountActivationRequestDto {
    @Length(min = 3,max = 30)
    String username;
    @Length(min = 8)
    String password;
    String activationcode;
}
