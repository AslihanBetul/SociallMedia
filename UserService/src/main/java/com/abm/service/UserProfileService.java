package com.abm.service;

import com.abm.dto.request.UserSaveRequestDto;
import com.abm.dto.request.UserUpdateDto;
import com.abm.dto.response.UserProfileResponseDto;
import com.abm.entity.UserProfile;
import com.abm.enums.UserProfileStatus;
import com.abm.exception.ErrorType;
import com.abm.exception.UserProfileServiceException;
import com.abm.manager.AuthManager;
import com.abm.mapper.UserProfileMapper;
import com.abm.rabbitmq.model.UserSaveRequestModel;
import com.abm.rabbitmq.model.UserStatusUpdateModel;
import com.abm.repository.UserProfileRepository;
import com.abm.utility.JwtTokenManger;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final JwtTokenManger jwtTokenManger;
    private final AuthManager authManager;
    private static  final String KEY="UserList";
    private final RedisTemplate<String, UserProfile> redisTemplate;

    public boolean save(UserSaveRequestDto dto) {
        UserProfile userProfile = userProfileRepository.save(UserProfileMapper.INSTANCE.dtoToUserProfile(dto));
        return !userProfile.getId().isEmpty();
    }

    public boolean saveWithRabbit(UserSaveRequestModel model) {
        UserProfile userProfile = userProfileRepository.save(UserProfileMapper.INSTANCE.userSaveRequestModeltoUserProfile(model));
        redisTemplate.opsForList().rightPush(KEY,userProfile);
        return !userProfile.getId().isEmpty();
    }

    @RabbitListener(queues ="queue-socialmedia-userRegisterFromAuth")
    public void receiveUserSaveRequstModel(UserSaveRequestModel userSaveRequestModel){
        saveWithRabbit(userSaveRequestModel);
    }

    public Boolean updateStatus(Long id) {
        UserProfile findedUserProfile = userProfileRepository.findByAuthId(id.toString()).orElseThrow(() -> new UserProfileServiceException(ErrorType.USER_NOT_FOUND));

        findedUserProfile.setStatus(UserProfileStatus.ACTIVE);
        UserProfile updatedUserProfile = userProfileRepository.save(findedUserProfile);
        if (!updatedUserProfile.getStatus().equals(UserProfileStatus.ACTIVE)){
            throw new UserProfileServiceException(ErrorType.USER_STATUS_UPDATE_FAILED);
        }
        return true;
    }


    public void updateStatusByRabbitMq(Long id) {
        UserProfile findedUserProfile = userProfileRepository.findByAuthId(id.toString()).orElseThrow(() -> new UserProfileServiceException(ErrorType.USER_NOT_FOUND));
        Long index = redisTemplate.opsForList().indexOf(KEY, findedUserProfile);
        findedUserProfile.setStatus(UserProfileStatus.ACTIVE);
        UserProfile updatedUserProfile = userProfileRepository.save(findedUserProfile);
        if (!updatedUserProfile.getStatus().equals(UserProfileStatus.ACTIVE)){
            throw new UserProfileServiceException(ErrorType.USER_STATUS_UPDATE_FAILED);

        }
      updateUserCache(updatedUserProfile,index);

    }
    private void updateUserCache(UserProfile updatedUserProfile,Long index){

        if (index!=null){
            redisTemplate.opsForList().set(KEY,index,updatedUserProfile);
        }

    }

    @RabbitListener(queues ="queue-socialmedia-auth")
    public void receiveSocialMediaAuth(UserStatusUpdateModel userStatusUpdateModel){
        updateStatusByRabbitMq(userStatusUpdateModel.getAuthId());
    }



    public UserProfileResponseDto updateUser(UserUpdateDto dto) {
        Long authId = jwtTokenManger.getAuthIdFromToken(dto.getToken()).orElseThrow(() -> new UserProfileServiceException(ErrorType.INVALID_TOKEN));
        UserProfile userProfile = userProfileRepository.findByAuthId(authId.toString()).orElseThrow(() -> new UserProfileServiceException(ErrorType.USER_NOT_FOUND));
        if (dto.getEmail()!=null){
                userProfile.setEmail(dto.getEmail());
                authManager.updateEmail(dto.getEmail(),authId);
        }
        if (dto.getAbout()!=null){
            userProfile.setAbout(dto.getAbout());
        }
        if (dto.getPhone()!=null){
            userProfile.setPhone(dto.getPhone());
        }
        if (dto.getPhoto()!=null){
            userProfile.setPhoto(dto.getPhoto());
        }
        if (dto.getAddress()!=null){
            userProfile.setAddress(dto.getAddress());
        }

        UserProfile updatedUserProfile = userProfileRepository.save(userProfile);

        return UserProfileMapper.INSTANCE.responseDto(updatedUserProfile);
    }

    public void delete(Long id) {
        UserProfile userProfile = userProfileRepository.findByAuthId(id.toString()).orElseThrow(() -> new UserProfileServiceException(ErrorType.USER_NOT_FOUND));
        Long index = redisTemplate.opsForList().indexOf(KEY, userProfile);
        userProfile.setStatus(UserProfileStatus.DELETED);
        userProfileRepository.save(userProfile);
        updateUserCache(userProfile,index);
    }

    public String getUserIdByAuthId(Long authId) {
        UserProfile userProfile = userProfileRepository.findByAuthId(authId.toString()).orElseThrow(() -> new UserProfileServiceException(ErrorType.USER_NOT_FOUND));
        return userProfile.getId();
    }

    public UserProfile findByUsername(String username) {
        UserProfile userProfile = userProfileRepository.findByUsername(username).orElseThrow(() -> new UserProfileServiceException(ErrorType.USER_NOT_FOUND));
       if (redisTemplate.opsForList().indexOf(KEY, userProfile)==null){
           redisTemplate.opsForList().rightPush(KEY,userProfile);
       }

        return userProfile;
    }


    public List<UserProfile> findByStatus(UserProfileStatus status) {
        if(!(status.equals(UserProfileStatus.ACTIVE) || status.equals(UserProfileStatus.DELETED)|| status.equals(UserProfileStatus.BANNED)|| status.equals(UserProfileStatus.PENDING))){
        throw new UserProfileServiceException(ErrorType.UNKNOWN_STATUS);
        }
        List<UserProfile> userProfiles = userProfileRepository.findByStatus(status);//mongodan gelen güncel status bilgileri
        List<UserProfile> cacheList = redisTemplate.opsForList().range((KEY+status.toString()), 0, -1); //redisten gelen önceki status hali
        cacheList.forEach(userProfile ->{
            if (!userProfiles.contains(userProfile)){
                redisTemplate.opsForList().remove((KEY+status.toString()), 0,userProfile);
            }
        });
        userProfiles.forEach(userProfile ->{
            if (redisTemplate.opsForList().indexOf(KEY+status.toString(), userProfile)==null){
                redisTemplate.opsForList().rightPush(KEY+status.toString(),userProfile);
            }
        });

          return userProfiles;
    }

    public List<UserProfileResponseDto> findAll() {
        List<UserProfileResponseDto>userProfileResponses=new ArrayList<>();
        userProfileRepository.findAll().forEach(userProfile->{
            userProfileResponses.add(UserProfileMapper.INSTANCE.responseDto(userProfile));
        });
        return userProfileResponses;
    }
    public UserProfile findByAuthId(String authId) {
        return userProfileRepository.findByAuthId(authId).orElseThrow(()-> new UserProfileServiceException(ErrorType.USER_NOT_FOUND));
    }
}
