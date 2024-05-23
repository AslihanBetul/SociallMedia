package com.abm.dto.request;

import com.abm.enums.UserProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserSaveRequestDto {
    Long authId;
    String username;
    String email;
    UserProfileStatus status;

}
