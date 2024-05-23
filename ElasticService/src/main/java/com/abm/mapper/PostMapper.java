package com.abm.mapper;


import com.abm.domain.Post;

import com.abm.rabbitMQ.model.PostSaveModel;
import com.abm.rabbitMQ.model.PostUpdateModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    PostMapper INSTANCE =Mappers.getMapper(PostMapper.class);






    Post postSaveModelToPost(PostSaveModel postSaveModel);



    Post postUpdateModelToPost(PostUpdateModel postUpdateModel);


}
