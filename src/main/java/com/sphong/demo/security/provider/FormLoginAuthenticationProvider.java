package com.sphong.demo.security.provider;

import com.sphong.demo.domain.Account;
import com.sphong.demo.domain.AccountRepository;
import com.sphong.demo.security.AccountContext;
import com.sphong.demo.security.AccountContextService;
import com.sphong.demo.security.token.PostAuthorizationToken;
import com.sphong.demo.security.token.PreAuthorizationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;


@Component
public class FormLoginAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private AccountContextService accountContextService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PreAuthorizationToken token = (PreAuthorizationToken) authentication;
        String username = token.getUsername();
        String password = token.getPassword();

        Account account = accountRepository.findByUserId(username).orElseThrow(() -> new NoSuchElementException("UserID Not Found!"));
        if (isCorrectPassword(password, account)) {
            return PostAuthorizationToken.getTokenFromAccountContext(AccountContext.fromAccountModel(account));
        }
        throw new NoSuchElementException("Authentication is not allowed!");

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthorizationToken.class.isAssignableFrom(authentication);
    }

    private boolean isCorrectPassword(String password, Account account) {
        return passwordEncoder.matches(account.getPassword(), password);
    }
}
