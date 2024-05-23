package com.abm.mapper;

import com.abm.dto.request.RegisterRequestDto;

import com.abm.dto.request.UserSaveRequestDto;
import com.abm.entity.Auth;
import com.abm.rabbitmq.model.UserSaveRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {
    AuthMapper INSTANCE =Mappers.getMapper(AuthMapper.class);
    Auth dtoToAuth (RegisterRequestDto dto);
   @Mapping(target = "authId",source = "id")
   @Mapping(target = "status",source = "status")
  UserSaveRequestDto authToUserSaveRequestDto(Auth auth);


    @Mapping(target = "authId",source = "id")
    @Mapping(target = "status",source = "status")
   UserSaveRequestModel authtoSaveRequestModel(Auth auth);

}
