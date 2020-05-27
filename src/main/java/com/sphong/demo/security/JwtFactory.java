package com.sphong.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;

/*
*  JWT 값만 가지고도 유저의 인증 여부 및 권한 정보를 알 수 있다
* */
@Component
public class JwtFactory {
    private static final Logger log = LoggerFactory.getLogger(JwtFactory.class);
    private static String signingKey = "jwttest";
    public String generateToken(AccountContext context) {
        String token = null;
        try {
            token = JWT.create()
                    .withIssuer("sphong")
                    .withClaim("USERNAME", context.getAccount().getUserId())
                    .withClaim("USER_ROLE", context.getAccount().getUserRole().getRoleName())
                    .sign(generateAlgorithm());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return token;
    }

    private Algorithm generateAlgorithm() throws UnsupportedEncodingException {
        return Algorithm.HMAC256(signingKey);
    }
}

