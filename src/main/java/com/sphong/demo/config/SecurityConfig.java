package com.sphong.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sphong.demo.security.FilterSkipMatcher;
import com.sphong.demo.security.HeaderTokenExtractor;
import com.sphong.demo.security.filter.FormLoginFilter;
import com.sphong.demo.security.filter.JwtAuthenticationFilter;
import com.sphong.demo.security.filter.SocialLoginFilter;
import com.sphong.demo.security.handler.FormLoginAuthenticationFailurehandler;
import com.sphong.demo.security.handler.FormLoginAuthenticationSuccesshandler;
import com.sphong.demo.security.handler.JwtAuthenticationFailureHandler;
import com.sphong.demo.security.provider.FormLoginAuthenticationProvider;
import com.sphong.demo.security.provider.JwtAuthenticationProvider;
import com.sphong.demo.security.provider.SocialLoginAuthenticationProvider;
import com.sphong.demo.security.social.KakaoUserProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private FormLoginAuthenticationSuccesshandler formLoginAuthenticationSuccesshandler;

    @Autowired
    private FormLoginAuthenticationFailurehandler formLoginAuthenticationFailurehandler;

    @Autowired
    private FormLoginAuthenticationProvider formLoginAuthenticationProvider;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private SocialLoginAuthenticationProvider socialLoginAuthenticationProvider;

    @Autowired
    private JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;

    @Autowired
    private HeaderTokenExtractor headerTokenExtractor;


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(KakaoUserProperty.class, new KakaoPropertyDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }

    //AuthenticationManager를 빈으로 등록
    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    protected FormLoginFilter formLoginFilter() throws Exception {
        FormLoginFilter filter = new FormLoginFilter("/formLogin", formLoginAuthenticationSuccesshandler,formLoginAuthenticationFailurehandler);
        filter.setAuthenticationManager(super.authenticationManagerBean());
        return filter;
    }

    protected JwtAuthenticationFilter jwtFilter() throws Exception {
        FilterSkipMatcher matcher = new FilterSkipMatcher(Arrays.asList("/formlogin", "/social"), "/api/**");
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(matcher, jwtAuthenticationFailureHandler, headerTokenExtractor);
        filter.setAuthenticationManager(super.authenticationManagerBean());
        return filter;
    }

    protected SocialLoginFilter socialFilter() throws Exception {
        SocialLoginFilter filter = new SocialLoginFilter("/social",formLoginAuthenticationSuccesshandler);
        filter.setAuthenticationManager(super.authenticationManagerBean());
        return filter;
    }
    //Authentication Manager에 Authentication Provider를 등록
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.formLoginAuthenticationProvider)
            .authenticationProvider(this.socialLoginAuthenticationProvider)
            .authenticationProvider(this.jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.authorizeRequests()
            .antMatchers("/h2-console**").permitAll();
        http.addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(socialFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
