package com.sphong.demo.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sphong.demo.dto.FormLoginDto;
import com.sphong.demo.security.token.PreAuthorizationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* Processing URL이 바뀌기 때문에 Filter는 Component로 등록하지 않는다.
* 주의점 : setAuthenticationManager를 통해 AuthenticationManager를 등록하고 이를 통해 Authentication Provider를 설정해야한다.
* */
public class FormLoginFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationSuccessHandler authenticationSuccessHandler;
    private AuthenticationFailureHandler authenticationFailureHandler;

    public FormLoginFilter(String defaultFilterProcessUrl, AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler) {
        super(defaultFilterProcessUrl);

        this.authenticationSuccessHandler = successHandler;
        this.authenticationFailureHandler = failureHandler;
    }

    public FormLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    /*
    * RequestBody에서 FormLoginDto로 매핑하고 이를 통해 획득한 유저정보로 인증 진행
    * */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        FormLoginDto loginDto = new ObjectMapper().readValue(request.getReader(), FormLoginDto.class);
        PreAuthorizationToken token = new PreAuthorizationToken(loginDto);
        //token에 맞는 provider를 통해 인증을 진행
        return super.getAuthenticationManager().authenticate(token);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        this.authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
    }
}
