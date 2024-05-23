package com.abm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    USERNAME_ALREADY_TAKEN(1001,
            "Bu username daha önce kullanılmış. Yeniden deneyiniz.",
            HttpStatus.BAD_REQUEST),
    USERNAME_OR_PASSWORD_WRONG(1002,
            "Kullanıcı adı veya parola yanlış.",
            HttpStatus.BAD_REQUEST),
    PASSWORDS_NOT_MATCHED(1003,
            "Girdiğiniz parolalar uyuşmamaktadır. Lütfen kontrol ediniz.",
            HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(2001,
            "Token geçersizdir.",
            HttpStatus.BAD_REQUEST),
    TOKEN_CREATION_FAILED(2002,
            "Token yaratmada hata meydana geldi.",
            HttpStatus.SERVICE_UNAVAILABLE),
    TOKEN_VERIFY_FAILED(2003,
            "Token verify etmede bir hata meydana geldi.",
            HttpStatus.SERVICE_UNAVAILABLE),
    TOKEN_ARGUMENT_NOTVALID(2003,
            "Token argümanı yanlış formatta.",
            HttpStatus.BAD_REQUEST),
    URUN_NOT_FOUND(5003,
            "Böyle bir Ürün bulunamadı.",
            HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(5004,
            "Böyle bir kullanıcı bulunamadı.",
            HttpStatus.NOT_FOUND),

    ACTIVATION_CODE_WRONG(1004,
            "aktivasyon kodu geçersiz",
            HttpStatus.BAD_REQUEST),
    BAD_REQUEST_ERROR(1001,"Girilen parametreler hatalıdır. Lütfen düzeltiniz.",HttpStatus.BAD_REQUEST),

    ACCOUNT_NOT_ACTIVE(1002,"hesabınız aktif değil",HttpStatus.BAD_REQUEST),
    ACCOUNT_ALREADY_ACTIVE(1002,"hesabınız zaten aktif",HttpStatus.BAD_REQUEST),
    ACCOUNT_BANNED(1003,"hesabınız banlıdır",HttpStatus.BAD_REQUEST),
    ACCOUNT_DELETED(1004,"hesabınız silinmiştir",HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(1005,"hesabınız silinmiştir",HttpStatus.BAD_REQUEST),
    ACCOUNT_ALREADY_DELETED(1006,"hesabınız  zaten silinmiştir",HttpStatus.BAD_REQUEST),
    USER_STATUS_UPDATE_FAILED(1007,"güncelleme işleminde hata alındı",HttpStatus.BAD_REQUEST),
    UNKNOWN_STATUS(10008,"hatalı status girildi" ,HttpStatus.BAD_REQUEST );
    private Integer code;
    private String message;
    private HttpStatus httpStatus;

}

