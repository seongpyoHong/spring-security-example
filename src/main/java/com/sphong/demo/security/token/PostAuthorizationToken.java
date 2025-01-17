package com.sphong.demo.security.token;

import com.sphong.demo.security.AccountContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PostAuthorizationToken extends UsernamePasswordAuthenticationToken {
    private PostAuthorizationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static PostAuthorizationToken getTokenFromAccountContext(AccountContext context) {
        return new PostAuthorizationToken(context, context.getPassword(), context.getAuthorities());
    }

    public AccountContext getAccountContext() {
        return (AccountContext) this.getPrincipal();
    }
}
