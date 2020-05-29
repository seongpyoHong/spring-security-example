package com.sphong.demo.security.provider;

import com.sphong.demo.domain.Account;
import com.sphong.demo.domain.AccountRepository;
import com.sphong.demo.domain.SocialProvider;
import com.sphong.demo.domain.UserRole;
import com.sphong.demo.dto.SocialUserDto;
import com.sphong.demo.security.AccountContext;
import com.sphong.demo.security.service.specification.SocialFetchService;
import com.sphong.demo.security.social.SocialUserProperty;
import com.sphong.demo.security.token.PostAuthorizationToken;
import com.sphong.demo.security.token.SocialPreAuthorizationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SocialLoginAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    @Qualifier("socialFetchServiceImpl")
    private SocialFetchService socialFetchService;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SocialPreAuthorizationToken token = (SocialPreAuthorizationToken)authentication;
        SocialUserDto dto = token.getDto();

        return PostAuthorizationToken.getTokenFromAccountContext(AccountContext.fromAccountModel(getAccountFromDB(dto)));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SocialPreAuthorizationToken.class.isAssignableFrom(authentication);
    }

    private Account getAccountFromDB(SocialUserDto dto) {
        SocialUserProperty property = socialFetchService.getSocialUserInfo(dto);
        String userId = property.getUserId();
        SocialProvider provider = dto.getProvider();
        return accountRepository.findBySocialIdAndSocialProvider(Long.valueOf(userId), provider)
                                .orElseGet( () -> {
                                    return accountRepository.save(Account.builder()
                                                                        .id(Long.valueOf(property.getUserId()))
                                                                        .password(String.valueOf(UUID.randomUUID().getMostSignificantBits()))
                                                                        .userId(property.getUserId())
                                                                        .username(property.getUserNickname())
                                                                        .userRole(UserRole.USER)
                                                                        .socialProvider(provider)
                                                                        .socialId(Long.valueOf(property.getUserId()))
                                                                        .profileHref(property.getProfileHref())
                                                                        .build());
                                });
    }

}
