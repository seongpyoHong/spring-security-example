package com.sphong.demo.security.service.specification;

import com.sphong.demo.dto.SocialUserDto;
import com.sphong.demo.security.social.SocialUserProperty;


/*
* 실제 Social Server에 인증 정보 확인
* */
public interface SocialFetchService {
    SocialUserProperty getSocialUserInfo(SocialUserDto socialUserDto);
}
