package com.sphong.demo.security.token;

import com.sphong.demo.domain.SocialProvider;
import com.sphong.demo.dto.FormLoginDto;
import com.sphong.demo.dto.SocialUserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class PreAuthorizationToken extends UsernamePasswordAuthenticationToken {

    private PreAuthorizationToken(String username, String password) {
       super(username, password);
    }
    private PreAuthorizationToken(SocialProvider provider, SocialUserDto dto) {
        super(provider, dto);
    }

    public PreAuthorizationToken(FormLoginDto dto) {
        this(dto.getId(), dto.getPassword());
    }
    public String getUsername() {
        return (String)super.getPrincipal();
    }
    public PreAuthorizationToken(SocialUserDto dto) {
        this(dto.getProvider(), dto);
    }

    public String getPassword() {
        return (String)super.getCredentials();
    }
}
