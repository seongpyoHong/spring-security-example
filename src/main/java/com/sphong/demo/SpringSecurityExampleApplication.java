package com.sphong.demo;

import com.sphong.demo.domain.Account;
import com.sphong.demo.domain.AccountRepository;
import com.sphong.demo.domain.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringSecurityExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityExampleApplication.class, args);
    }

    @Bean
    CommandLineRunner bootstrapTestAccount(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Account account = new Account();
            account.setUserId("sphong");
            account.setPassword(passwordEncoder.encode("1234"));
            account.setUserRole(UserRole.USER);
            accountRepository.save(account);
        };
    }
}
