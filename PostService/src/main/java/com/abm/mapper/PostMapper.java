package com.abm.mapper;


import com.abm.dto.response.PostResponseDto;
import com.abm.entity.Post;
import com.abm.rabbitmq.model.PostSaveModel;
import com.abm.rabbitmq.model.PostUpdateModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    PostMapper INSTANCE =Mappers.getMapper(PostMapper.class);
    @Mapping(target = "postDate", source = "createdAt")
    PostResponseDto postToPostResponseDto(Post post);

    @Mapping(target = "postId", source = "id")
    PostSaveModel postToPostSaveModel(Post post);

    @Mapping(target = "postId", source = "id")
    PostUpdateModel postToPostUpdateModel(Post post);
}
