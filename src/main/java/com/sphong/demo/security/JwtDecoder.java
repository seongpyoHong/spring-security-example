package com.sphong.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sphong.demo.security.token.JwtPreProcessingToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Component
public class JwtDecoder {
    private Logger logger = LoggerFactory.getLogger(JwtFactory.class);

    /*
     * Signing Key 인증
     *
     */
    public AccountContext decodeJwt(String token) {
        DecodedJWT decodedJWT = isValidJwt(token).orElseThrow(() -> new InvalidJwtException("Invalid JWT Token"));
        String username = decodedJWT.getClaim("USERNAME").asString();
        String role = decodedJWT.getClaim("USER_ROLE").asString();
        return new AccountContext(username, "1234", role);
    }

    private Optional<DecodedJWT> isValidJwt(String token) {
        DecodedJWT jwt = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256("jwttest");
            JWTVerifier verifier = JWT.require(algorithm).build();

            jwt = verifier.verify(token);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        return Optional.ofNullable(jwt);
    }
}
