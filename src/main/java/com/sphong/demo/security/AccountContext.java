package com.sphong.demo.security;

import com.sphong.demo.domain.Account;
import com.sphong.demo.domain.UserRole;
import com.sphong.demo.security.token.JwtPostProcessingToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.management.relation.RelationNotification;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountContext extends User {

    private Account account;
    private AccountContext(Account account,String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.account = account;
    }

    public static AccountContext fromAccountModel(Account account) {
        return new AccountContext(account, account.getUserId(), account.getPassword(), parseAuthorities(account.getUserRole()));
    }

    public static AccountContext frommJwtPostToken(JwtPostProcessingToken token) {
        return new AccountContext(null, token.getUserId(), token.getPassword(), token.getAuthorities());
    }

    public AccountContext(String username, String password, String role) {
        super(username, password,parseAuthorities(role));
    }

    private static List<SimpleGrantedAuthority> parseAuthorities(UserRole role) {
        return Stream.of(role).map(r -> new SimpleGrantedAuthority(r.getRoleName())).collect(Collectors.toList());
    }

    private static List<SimpleGrantedAuthority> parseAuthorities(String role) {
        return parseAuthorities(UserRole.getRoleName(role));
    }
    public Account getAccount() {
        return this.account;
    }
}
