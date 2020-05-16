package com.sphong.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@SpringBootApplication
public class SpringSecurityExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityExampleApplication.class, args);
    }
}
