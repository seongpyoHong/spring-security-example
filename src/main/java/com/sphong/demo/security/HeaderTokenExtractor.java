package com.sphong.demo.security;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/*
* Header에서 JWT Token의 값을 추출하는 역할
* */
@Component
public class HeaderTokenExtractor {
    public static final String HEADER_PREFIX = "Bearer ";

    public String extract(String header) {
        if (StringUtils.isEmpty(header) | header.length() < HEADER_PREFIX.length()) {
            throw new InvalidJwtException("Invalid Token Information");
        }

        return header.substring(HEADER_PREFIX.length(),header.length());
    }
}
