package com.sphong.demo.security.provider;

import com.sphong.demo.security.AccountContext;
import com.sphong.demo.security.JwtDecoder;
import com.sphong.demo.security.token.JwtPreProcessingToken;
import com.sphong.demo.security.token.PostAuthorizationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private JwtDecoder jwtDecoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String)authentication.getPrincipal();
        AccountContext context = jwtDecoder.decodeJwt(token);
        return PostAuthorizationToken.getTokenFromAccountContext(context);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtPreProcessingToken.class.isAssignableFrom(authentication);
    }
}
