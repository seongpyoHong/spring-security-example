package com.sphong.demo.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sphong.demo.dto.TokenDto;
import com.sphong.demo.security.AccountContext;
import com.sphong.demo.security.JwtFactory;
import com.sphong.demo.security.token.PostAuthorizationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Filter가 보내준 Request, Response, 인증된 정보(PostAuthenticationToken)을 같이 보내주는 역할
 */
@Component
public class FormLoginAuthenticationSuccesshandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtFactory factory;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //PostAuthrizationToken에서 넘어온 accountcontext을 통해 token을 생성한다.
        PostAuthorizationToken token = (PostAuthorizationToken) authentication;
        AccountContext context = (AccountContext) token.getPrincipal();
        String tokenString = factory.generateToken(context);
        processResponse(response, writeToDto(tokenString));
    }

    //만들어진 token을 DTO에 기록
    private TokenDto writeToDto(String token) {
        return new TokenDto(token);
    }

    //Response에 token을 추가
    private void processResponse(HttpServletResponse response, TokenDto tokenDto) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(objectMapper.writeValueAsString(tokenDto));
    }
}
