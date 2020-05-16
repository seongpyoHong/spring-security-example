package com.sphong.demo.security;

import com.sphong.demo.domain.Account;
import com.sphong.demo.domain.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class AccountContextService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Account account =  accountRepository.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("UserID Not Found!"));
        return getAccountContext(account);
    }

    private AccountContext getAccountContext(Account account) {
        return AccountContext.fromAccountModel(account);
    }
}
