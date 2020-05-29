package com.sphong.demo.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sphong.demo.dto.SocialUserDto;
import com.sphong.demo.security.token.PreAuthorizationToken;
import com.sphong.demo.security.token.SocialPreAuthorizationToken;
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

public class SocialLoginFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationSuccessHandler successHandler;

    protected SocialLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public SocialLoginFilter(String defaultFilterProcessesUrl, AuthenticationSuccessHandler successHandler) {
        super(defaultFilterProcessesUrl);
        this.successHandler = successHandler;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        SocialUserDto dto = objectMapper.readValue(request.getReader(), SocialUserDto.class);

        return super.getAuthenticationManager().authenticate(new SocialPreAuthorizationToken(dto));
    }

    /*
    * Override 하지 않으면 Default 옵션인 redirect를 하기 때문에 직접 구현한 Success Handler를 사용해야한다.
    * */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        this.successHandler.onAuthenticationSuccess(request, response, authResult);
    }

}
