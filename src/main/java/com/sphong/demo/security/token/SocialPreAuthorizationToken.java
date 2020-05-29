package com.sphong.demo.security.token;

import com.sphong.demo.dto.SocialUserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class SocialPreAuthorizationToken extends UsernamePasswordAuthenticationToken {
    public SocialPreAuthorizationToken(SocialUserDto dto) {
        super(dto.getProvider(),dto);
    }

    public SocialUserDto getDto() {
        return (SocialUserDto)super.getCredentials();
    }
}
