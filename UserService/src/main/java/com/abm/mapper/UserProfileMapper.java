package com.abm.mapper;

import com.abm.dto.request.UserSaveRequestDto;
import com.abm.dto.response.UserProfileResponseDto;
import com.abm.entity.UserProfile;
import com.abm.rabbitmq.model.UserSaveRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserProfileMapper {
    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);
    UserProfile dtoToUserProfile (UserSaveRequestDto dto);
    UserProfileResponseDto responseDto(UserProfile userProfile);

    UserProfile userSaveRequestModeltoUserProfile(UserSaveRequestModel userSaveRequestModel);
}
