package com.abm.service;

import com.abm.Manager.UserProfileManager;
import com.abm.constant.messages.SuccessMessages;
import com.abm.dto.request.AccountActivationRequestDto;
import com.abm.dto.request.LoginRequestDto;
import com.abm.dto.request.RegisterRequestDto;
import com.abm.dto.request.RepasswordRequestDto;
import com.abm.entity.Auth;
import com.abm.entity.Yetki;
import com.abm.entity.enums.EYetki;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final JwtTokenManger jwtTokenManger;
    private final UserProfileManager userProfileManager;
    private final RabbitTemplate rabbitTemplate;
    private final YetkiService yetkiService;
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
    /**
     * Yeni bir kullanıcı kaydeder, kullanıcı bilgilerini veritabanına kaydeder ve gerekli verileri RabbitMQ aracılığıyla diğer servislere gönderir.
     *
     * Bu metod şu adımları gerçekleştirir:
     * 1. Sağlanan şifrelerin eşleşip eşleşmediğini kontrol eder.
     * 2. Kullanıcı adının zaten alınıp alınmadığını kontrol eder.
     * 3. Kayıt verilerini bir Auth nesnesine dönüştürür.
     * 4. Kullanıcı rolünü, kullanıcı adını (küçük harflerle) ayarlar ve bir aktivasyon kodu oluşturur.
     * 5. Auth nesnesini veritabanına kaydeder.
     * 6. Kullanıcı kayıt modelini dönüştürür ve kaydeder.
     * 7. Aktivasyon kodu modelini dönüştürür ve gönderir.
     *  username yeni kullanıcının kullanıcı adı
     * password yeni kullanıcının şifresi
     * repassword şifrenin doğrulaması
     *  email yeni kullanıcının e-posta adresi
     *
     * @throws AuthMicroServiceException şifreler eşleşmezse veya kullanıcı adı zaten alınmışsa
     */
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
        yetkiService.save(Yetki.builder()
                .authid(saved.getId())
                .yetki(EYetki.USER)
                .build());

       convertAndSaveUserSaveModel(AuthMapper.INSTANCE.authtoSaveRequestModel(saved));
       convertAndSendAcvitationCode(ActivationCodeModel.builder().activationCode(auth.getActivationcode()).email(auth.getEmail()).build());

    }
    /**
     * Kullanıcı kayıt modelini RabbitMQ'ya dönüştürür ve gönderir.
     *
     * @param userSaveRequestModel gönderilecek kullanıcı kayıt modelidir
     */
    public void convertAndSaveUserSaveModel(@RequestBody UserSaveRequestModel userSaveRequestModel){
        rabbitTemplate.convertAndSend("direct-exchange-sociamedia-auth","save-userRegisterFromAuth-key",userSaveRequestModel);
        System.out.println("gönderim başarılı");
    }

    /**
     * Aktivasyon kodu modelini RabbitMQ'ya dönüştürür ve gönderir.
     *
     * @param model gönderilecek aktivasyon kodu modelidir
     */


    public void convertAndSendAcvitationCode(@RequestParam ActivationCodeModel model){
        rabbitTemplate.convertAndSend("direct-exchange-sociamedia-auth","activateCode.key",model);
        System.out.println("gönderim başarılı");
    }

    /**
     * Kullanıcının hesabını doğrular.
     *
     * @param dto hesap doğrulama isteği veri transfer nesnesi
     * @return doğrulama işleminin sonucunu gösteren bir mesaj
     */
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
    /**
     * RabbitMQ kullanarak bir kullanıcının hesabını doğrular.
     *
     * @param dto hesap aktivasyon isteği veri transfer nesnesi
     * @return hesabın başarıyla doğrulandığını belirten bir mesaj
     */

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
    /**
     * Kullanıcı durum güncelleme modelini RabbitMQ'ya dönüştürür ve belirli bir anahtarla belirtilen değişime gönderir.
     *
     * @param userStatusUpdateModel Kullanıcı durum güncelleme modeli
     *        - authId: Güncellenmek istenen kullanıcının kimlik numarası
     */
    public void convertAndSendAuthId(@RequestBody UserStatusUpdateModel userStatusUpdateModel){
        rabbitTemplate.convertAndSend("direct-exchange-sociamedia-auth","save-userprofile-key",userStatusUpdateModel);
        System.out.println("gönderim başarılı");
    }


    /**
     * Kullanıcı giriş işlemini gerçekleştirir.
     *
     * @param dto Giriş isteği veri transfer nesnesi
     *           - username: Kullanıcının kullanıcı adı
     *           - password: Kullanıcının şifresi
     * @return Oluşturulan JWT (Json Web Token)
     * @throws AuthMicroServiceException Kullanıcı adı veya şifre yanlışsa veya hesap aktif değilse fırlatılır
     */
    public String login(LoginRequestDto dto) {
        Auth auth = authRepository.findOptionalByUsernameIgnoreCaseAndPassword(dto.getUsername(), dto.getPassword())
                .orElseThrow(() -> new AuthMicroServiceException(ErrorType.USERNAME_OR_PASSWORD_WRONG));
        if (!auth.getStatus().equals(AuthStatus.ACTIVE)) {
            throw new AuthMicroServiceException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
        return jwtTokenManger.createToken(auth).get();

    }


    /**
     * Belirli bir kullanıcının hesabını yumuşak bir şekilde siler.
     *
     * @param authId Silinmek istenen kullanıcının kimlik numarası
     * @return Hesabın başarıyla silindiğini belirten bir mesaj
     * @throws AuthMicroServiceException Kullanıcı bulunamazsa veya hesap zaten silinmişse fırlatılır
     */
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
    /**
     * Belirli bir kullanıcının e-posta adresini günceller.
     *
     * @param email Yeni e-posta adresi
     * @param authId E-posta adresi güncellenmek istenen kullanıcının kimlik numarası
     * @throws AuthMicroServiceException Kullanıcı bulunamazsa fırlatılır
     */
    public void updateEmail(String email, Long authId) {
        Auth auth = authRepository.findById(authId).orElseThrow(() -> new AuthMicroServiceException(ErrorType.USER_NOT_FOUND));
        auth.setEmail(email);
        authRepository.save(auth);

    }
    /**
     * Unutulan şifreyi sıfırlamak için kullanıcının e-posta adresine bir şifre sıfırlama kodu gönderir.
     *
     * @param email Şifresi sıfırlanacak kullanıcının e-posta adresi
     * @return Şifre sıfırlama kodunun mail adresine gönderildiğini belirten bir mesaj
     * @throws AuthMicroServiceException Kullanıcı bulunamazsa veya hesap aktif değilse fırlatılır
     */
    public String forgetMyPassword(String email) {
        Auth auth = authRepository.findOptionalByEmail(email)
               .orElseThrow(() -> new AuthMicroServiceException(ErrorType.USER_NOT_FOUND));
        if (!auth.getStatus().equals(AuthStatus.ACTIVE)) {
            throw new AuthMicroServiceException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }  //gereksiz olmuş
        auth.setRepasswordcode(CodeGenerator.generateActivationCode());
        authRepository.save(auth);
        convertAndSendRepassword(RepasswordCodeModel.builder().email(auth.getEmail()).repasswordCode(auth.getRepasswordcode()).build());
        return "Kodunuz mail adresinize gönderilmiştir";
    }
    /**
     * Kullanıcının şifresini günceller.
     *
     * @param repasswordRequestDto Şifresi güncellenmek istenen kullanıcının bilgilerini içeren veri transfer nesnesi
     *        - email: Şifresi güncellenmek istenen kullanıcının e-posta adresi
     *        - rePasswordCode: Şifre sıfırlama işlemi için kullanılan kod
     *        - password: Yeni şifre
     *        - confirmPassword: Yeni şifrenin doğrulaması
     * @throws AuthMicroServiceException E-posta veya şifre sıfırlama kodu yanlışsa veya yeni şifreler eşleşmiyorsa fırlatılır
     */
    public void updatePassword(RepasswordRequestDto repasswordRequestDto) {
        Auth auth = authRepository.findByEmailAndRepasswordcode(repasswordRequestDto.getEmail(), repasswordRequestDto.getRePasswordCode())
                .orElseThrow(() -> new AuthMicroServiceException(ErrorType.REPASSWORDCODE_OR_EMAIL_WRONG));
        if (!repasswordRequestDto.getPassword().equals(repasswordRequestDto.getConfirmPassword())){
            throw new AuthMicroServiceException(ErrorType.PASSWORDS_NOT_MATCHED);
        }
        auth.setPassword(repasswordRequestDto.getPassword());
        authRepository.save(auth);

    }
    /**
     * Yeni şifre oluşturma kodu modelini RabbitMQ'ya dönüştürür ve belirli bir anahtarla belirtilen değişime gönderir.
     *
     * @param model Şifre sıfırlama kodu modeli
     *        - email: Şifresi sıfırlanacak kullanıcının e-posta adresi
     *        - repasswordCode: Yeni şifre oluşturma kodu
     */
    private void convertAndSendRepassword(@RequestBody RepasswordCodeModel model){
        rabbitTemplate.convertAndSend("direct-exchange-sociamedia-auth","RepasswordCode.key",model);
        System.out.println("gönderim başarılı");
    }

    public Auth findById(Long authid) {
      return  authRepository.findById(authid).orElseThrow(()-> new AuthMicroServiceException(ErrorType.USER_NOT_FOUND));
    }

    public List<Auth> findAll() {
       return authRepository.findAll();
    }
}
