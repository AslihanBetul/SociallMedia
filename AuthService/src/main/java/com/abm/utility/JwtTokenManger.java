package com.abm.utility;

import com.abm.entity.Auth;
import com.abm.enums.AuthRole;
import com.abm.exception.AuthMicroServiceException;
import com.abm.exception.ErrorType;
import com.abm.repository.AuthRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenManger {
    private final AuthRepository authRepository;
    @Value("${authservice.secret.secret-key}")
    String secretKey ;
    @Value("${authservice.secret.issuer}")
    String issuer;

    Long expireTime=1000L*60*15; //15 dakikalık bir zaman

    public JwtTokenManger(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }


    public Optional<String> createToken(Auth auth) {

        /**
         * Claim objeleri içindeki değerler hereks tarafından görülebilir
         * o yuzden claimler ile e mail password gibi herkesin görmemesi gereken bilgileri payload kısmında tutmalıyız
         */
        String token ="";

        try {
            token= JWT.create().
                    withAudience()
                    .withClaim("id",auth.getId())
                    .withClaim("authRole",auth.getRole().toString())
                    .withIssuer(issuer)
                    .withIssuedAt(new Date())  //tokenın yaratıldıgı an
                    .withExpiresAt(new Date(System.currentTimeMillis()+expireTime))
                    .sign(Algorithm.HMAC512(secretKey));
            return Optional.of(token);
        } catch (IllegalArgumentException e) {
            throw new AuthMicroServiceException(ErrorType.TOKEN_CREATION_FAILED);

        } catch (JWTCreationException e) {
            throw new AuthMicroServiceException(ErrorType.TOKEN_CREATION_FAILED);
        }

    }

//    public Boolean verifyToken(String token){
//
//    }

    public Optional<Long> getAuthIdFromToken(String token){
        try {
            Algorithm algorithm =Algorithm.HMAC512(secretKey);
            JWTVerifier verifier =JWT.require(algorithm).withIssuer(issuer).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            if (decodedJWT==null)
               return Optional.empty();

            Long id = decodedJWT.getClaim("id").asLong();
            return Optional.of(id);
        }
        catch (IllegalArgumentException e) {
            throw new AuthMicroServiceException(ErrorType.TOKEN_ARGUMENT_NOTVALID);

        }
        catch (JWTVerificationException e) {
            throw new AuthMicroServiceException(ErrorType.TOKEN_VERIFY_FAILED);
        }

    }
    public Optional<String> getRoleFromToken(String token){
        try {
            Algorithm algorithm =Algorithm.HMAC512(secretKey);
            JWTVerifier verifier =JWT.require(algorithm).withIssuer(issuer).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            if (decodedJWT==null)
                return Optional.empty();

            Long id = decodedJWT.getClaim("id").asLong();
            return Optional.of(decodedJWT.getClaim("authRole").asString());
        }
        catch (IllegalArgumentException e) {
            throw new AuthMicroServiceException(ErrorType.TOKEN_ARGUMENT_NOTVALID);

        }
        catch (JWTVerificationException e) {
            throw new AuthMicroServiceException(ErrorType.TOKEN_VERIFY_FAILED);
        }

    }

    public Optional<String> createTokenOnlyId(Long id) {

        /**
         * Claim objeleri içindeki değerler hereks tarafından görülebilir
         * o yuzden claimler ile e mail password gibi herkesin görmemesi gereken bilgileri payload kısmında tutmalıyız
         */
        String token ="";

        try {
            token= JWT.create().
                    withAudience()
                    .withClaim("id",id)

                    .withIssuer(issuer)
                    .withIssuedAt(new Date())  //tokenın yaratıldıgı an
                    .withExpiresAt(new Date(System.currentTimeMillis()+expireTime))
                    .sign(Algorithm.HMAC512(secretKey));
            return Optional.of(token);
        } catch (IllegalArgumentException e) {
            throw new AuthMicroServiceException(ErrorType.TOKEN_CREATION_FAILED);

        } catch (JWTCreationException e) {
            throw new AuthMicroServiceException(ErrorType.TOKEN_CREATION_FAILED);
        }

    }

    public Optional<String> createTokenByIDAndRole(Long id,String role) {

        /**
         * Claim objeleri içindeki değerler hereks tarafından görülebilir
         * o yuzden claimler ile e mail password gibi herkesin görmemesi gereken bilgileri payload kısmında tutmalıyız
         */
        String token ="";

        try {
            token= JWT.create().
                    withAudience()
                    .withClaim("id",id)
                    .withClaim("authRole",role)
                    .withIssuer(issuer)
                    .withIssuedAt(new Date())  //tokenın yaratıldıgı an
                    .withExpiresAt(new Date(System.currentTimeMillis()+expireTime))
                    .sign(Algorithm.HMAC512(secretKey));
            return Optional.of(token);
        } catch (IllegalArgumentException e) {
            throw new AuthMicroServiceException(ErrorType.TOKEN_CREATION_FAILED);

        } catch (JWTCreationException e) {
            throw new AuthMicroServiceException(ErrorType.TOKEN_CREATION_FAILED);
        }

    }

}
