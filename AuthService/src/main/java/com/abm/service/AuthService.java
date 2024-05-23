package com.abm.service;

import com.abm.Manager.UserProfileManager;
import com.abm.constant.messages.SuccessMessages;
import com.abm.dto.request.AccountActivationRequestDto;
import com.abm.dto.request.LoginRequestDto;
import com.abm.dto.request.RegisterRequestDto;
import com.abm.dto.request.RepasswordRequestDto;
import com.abm.entity.Auth;
import com.abm.enums.AuthRole;
import com.abm.enums.AuthStatus;
import com.abm.exception.AuthMicroServiceException;
import com.abm.exception.ErrorType;
import com.abm.mapper.AuthMapper;
import com.abm.rabbitmq.model.ActivationCodeModel;
import com.abm.rabbitmq.model.RepasswordCodeModel;
import com.abm.rabbitmq.model.UserSaveRequestModel;
import com.abm.rabbitmq.model.UserStatusUpdateModel;
import com.abm.repository.AuthRepository;
import com.abm.utility.CodeGenerator;
import com.abm.utility.JwtTokenManger;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final JwtTokenManger jwtTokenManger;
    private final UserProfileManager userProfileManager;
    private final RabbitTemplate rabbitTemplate;
//@Transactional metod yerine kullanım
    public String register(RegisterRequestDto dto){

        if (!dto.getPassword().equals(dto.getRepassword())) {
            throw new AuthMicroServiceException(ErrorType.PASSWORDS_NOT_MATCHED);
        }

        if (authRepository.existsByUsernameIgnoreCase(dto.getUsername())) {
            throw new AuthMicroServiceException(ErrorType.USERNAME_ALREADY_TAKEN);
        }
        Auth auth = AuthMapper.INSTANCE.dtoToAuth(dto);
        auth.setRole(AuthRole.USER);
        auth.setActivationcode(CodeGenerator.generateActivationCode());

        try {

            Auth saved = authRepository.save(auth);


            ResponseEntity<Boolean> isUserProfileSaved = userProfileManager.save(AuthMapper.INSTANCE.authToUserSaveRequestDto(auth));
            if (isUserProfileSaved.getBody() != null && isUserProfileSaved.getBody()) {
                return auth.getActivationcode();
            } else {
                authRepository.delete(saved);
                throw new AuthMicroServiceException(ErrorType.USER_SERVICE_CAN_NOT_SAVE_USER_PROFILE);
            }
        } catch (Exception e) {
            authRepository.delete(auth);
            throw new AuthMicroServiceException(ErrorType.USER_SERVICE_NOT_UNVALIABLE);
        }
    }

  @Transactional
    public void registerRabbitMQ(RegisterRequestDto dto){

        if (!dto.getPassword().equals(dto.getRepassword())) {
            throw new AuthMicroServiceException(ErrorType.PASSWORDS_NOT_MATCHED);
        }

        if (authRepository.existsByUsernameIgnoreCase(dto.getUsername())) {
            throw new AuthMicroServiceException(ErrorType.USERNAME_ALREADY_TAKEN);
        }
        Auth auth = AuthMapper.INSTANCE.dtoToAuth(dto);
        auth.setRole(AuthRole.USER);
        auth.setUsername(auth.getUsername().toLowerCase());
        auth.setActivationcode(CodeGenerator.generateActivationCode());
        Auth saved = authRepository.save(auth);

       convertAndSaveUserSaveModel(AuthMapper.INSTANCE.authtoSaveRequestModel(saved));
       convertAndSendAcvitationCode(ActivationCodeModel.builder().activationCode(auth.getActivationcode()).email(auth.getEmail()).build());

    }

    public void convertAndSaveUserSaveModel(@RequestBody UserSaveRequestModel userSaveRequestModel){
        rabbitTemplate.convertAndSend("direct-exchange-sociamedia-auth","save-userRegisterFromAuth-key",userSaveRequestModel);
        System.out.println("gönderim başarılı");
    }

    public void convertAndSendAcvitationCode(@RequestParam ActivationCodeModel model){
        rabbitTemplate.convertAndSend("direct-exchange-sociamedia-auth","activateCode.key",model);
        System.out.println("gönderim başarılı");
    }


     @Transactional
    public String verifyAccount(AccountActivationRequestDto dto) {
        Auth auth = authRepository.findOptionalByUsernameIgnoreCaseAndPassword(dto.getUsername(), dto.getPassword())
                .orElseThrow(() -> new AuthMicroServiceException(ErrorType.USERNAME_OR_PASSWORD_WRONG));

        if (!auth.getActivationcode().equals(dto.getActivationcode())){
            throw new AuthMicroServiceException(ErrorType.ACTIVATION_CODE_WRONG);
        }

        switch (auth.getStatus()) {
            case ACTIVE:
                throw new AuthMicroServiceException(ErrorType.ACCOUNT_ALREADY_ACTIVE);
            case BANNED:
                throw new AuthMicroServiceException(ErrorType.ACCOUNT_BANNED);
            case DELETED:
                throw new AuthMicroServiceException(ErrorType.ACCOUNT_DELETED);
            case PENDING:
                auth.setStatus(AuthStatus.ACTIVE);
                authRepository.save(auth);
                if (Boolean.FALSE.equals(userProfileManager.updateStatus(auth.getId()).getBody())){
                    throw new AuthMicroServiceException(ErrorType.USER_SERVICE_NOT_UNVALIABLE);
                }
                return SuccessMessages.ACTIVATION_SUCCESSFULL;
            default:
                throw new AuthMicroServiceException(ErrorType.INTERNAL_SERVER_ERROR);
            }

        }
    @Transactional
    public String verifyAccountwithRabbit(AccountActivationRequestDto dto) {
        Auth auth = authRepository.findOptionalByUsernameIgnoreCaseAndPassword(dto.getUsername(), dto.getPassword())
                .orElseThrow(() -> new AuthMicroServiceException(ErrorType.USERNAME_OR_PASSWORD_WRONG));

        if (!auth.getActivationcode().equals(dto.getActivationcode())){
            throw new AuthMicroServiceException(ErrorType.ACTIVATION_CODE_WRONG);
        }

        switch (auth.getStatus()) {
            case ACTIVE:
                throw new AuthMicroServiceException(ErrorType.ACCOUNT_ALREADY_ACTIVE);
            case BANNED:
                throw new AuthMicroServiceException(ErrorType.ACCOUNT_BANNED);
            case DELETED:
                throw new AuthMicroServiceException(ErrorType.ACCOUNT_DELETED);
            case PENDING:
                auth.setStatus(AuthStatus.ACTIVE);
                authRepository.save(auth);
                convertAndSendAuthId(UserStatusUpdateModel.builder().authId(auth.getId()).build());

                return SuccessMessages.ACTIVATION_SUCCESSFULL;
            default:
                throw new AuthMicroServiceException(ErrorType.INTERNAL_SERVER_ERROR);


        }


    }

    public void convertAndSendAuthId(@RequestBody UserStatusUpdateModel userStatusUpdateModel){
        rabbitTemplate.convertAndSend("direct-exchange-sociamedia-auth","save-userprofile-key",userStatusUpdateModel);
        System.out.println("gönderim başarılı");
    }



    public String login(LoginRequestDto dto) {
        Auth auth = authRepository.findOptionalByUsernameIgnoreCaseAndPassword(dto.getUsername(), dto.getPassword())
                .orElseThrow(() -> new AuthMicroServiceException(ErrorType.USERNAME_OR_PASSWORD_WRONG));
        if (!auth.getStatus().equals(AuthStatus.ACTIVE)) {
            throw new AuthMicroServiceException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
        return jwtTokenManger.createToken(auth).get();

    }
    @Transactional
    public String softDelete(Long authId) {
        Auth auth  = authRepository.findById(authId).orElseThrow(()-> new AuthMicroServiceException(ErrorType.USER_NOT_FOUND));
        if (auth.getStatus().equals(AuthStatus.DELETED)){
            throw new AuthMicroServiceException(ErrorType.ACCOUNT_ALREADY_DELETED);
        }
        auth.setStatus(AuthStatus.DELETED);
        authRepository.save(auth);
        userProfileManager.delete(auth.getId());
        return SuccessMessages.ACCOUNT_DELETED;
    }

    public String getTokenById(Long id) {

        return jwtTokenManger.createTokenOnlyId(id).get();
    }

    public Long getIdFromToken(String token) {
        return jwtTokenManger.getAuthIdFromToken(token).get();
    }

    public String getRoleFromToken(String token) {
        return jwtTokenManger.getRoleFromToken(token).get();
    }

    public String getTokenByIdAndRole(Long id, String role) {
        return jwtTokenManger.createTokenByIDAndRole(id, role).get();
    }

    public void updateEmail(String email, Long authId) {
        Auth auth = authRepository.findById(authId).orElseThrow(() -> new AuthMicroServiceException(ErrorType.USER_NOT_FOUND));
        auth.setEmail(email);
        authRepository.save(auth);

    }

    public String forgetMyPassword(String email) {
        Auth auth = authRepository.findOptionalByEmail(email)
               .orElseThrow(() -> new AuthMicroServiceException(ErrorType.USER_NOT_FOUND));
        if (!auth.getStatus().equals(AuthStatus.ACTIVE)) {
            throw new AuthMicroServiceException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
        auth.setRepasswordcode(CodeGenerator.generateActivationCode());
        authRepository.save(auth);
        convertAndSendRepassword(RepasswordCodeModel.builder().email(auth.getEmail()).repasswordCode(auth.getRepasswordcode()).build());
        return "Kodunuz mail adresinize gönderilmiştir";
    }

    public void updatePassword(RepasswordRequestDto repasswordRequestDto) {
        Auth auth = authRepository.findByEmailAndRepasswordcode(repasswordRequestDto.getEmail(), repasswordRequestDto.getRePasswordCode())
                .orElseThrow(() -> new AuthMicroServiceException(ErrorType.REPASSWORDCODE_OR_EMAIL_WRONG));
        if (!repasswordRequestDto.getPassword().equals(repasswordRequestDto.getConfirmPassword())){
            throw new AuthMicroServiceException(ErrorType.PASSWORDS_NOT_MATCHED);
        }
        auth.setPassword(repasswordRequestDto.getPassword());
        authRepository.save(auth);

    }

    private void convertAndSendRepassword(@RequestBody RepasswordCodeModel model){
        rabbitTemplate.convertAndSend("direct-exchange-sociamedia-auth","RepasswordCode.key",model);
        System.out.println("gönderim başarılı");
    }
}
