package com.abm.utility;



import com.abm.exception.ErrorType;
import com.abm.exception.UserProfileServiceException;

import com.abm.repository.UserProfileRepository;
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
    private final UserProfileRepository userRepository;

    @Value("${userservice.secret.secret-key}")
    String secretKey ;
    @Value("${userservice.secret.issuer}")
    String issuer;


    public JwtTokenManger(UserProfileRepository  userRepository) {
        this.userRepository = userRepository;
    }






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
            throw new UserProfileServiceException(ErrorType.TOKEN_ARGUMENT_NOTVALID);

        }
        catch (JWTVerificationException e) {
            throw new UserProfileServiceException(ErrorType.TOKEN_VERIFY_FAILED);
        }

    }




}
