package com.sphong.demo.security.token;

import com.sphong.demo.dto.FormLoginDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class PreAuthorizationToken extends UsernamePasswordAuthenticationToken {

    private PreAuthorizationToken(String username, String password) {
       super(username, password);
    }

    public PreAuthorizationToken(FormLoginDto dto) {
        this(dto.getId(), dto.getPassword());
    }
    public String getUsername() {
        return (String)super.getPrincipal();
    }

    public String getPassword() {
        return (String)super.getCredentials();
    }
}
