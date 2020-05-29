package com.sphong.demo.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sphong.demo.security.social.KakaoUserProperty;
import com.sphong.demo.security.social.SocialUserProperty;
import lombok.Getter;

/*
    - Endpoint
    - Deserializer
 */
@Getter
public enum SocialProvider {
    KAKAO("https://kapi.kakao.com/v2/user/me", KakaoUserProperty.class);
    private String endpoint;
    private Class<? extends SocialUserProperty> propertyMetaClass;

    SocialProvider(String endpoint, Class<? extends SocialUserProperty> propertyMetaClass) {
        this.endpoint = endpoint;
        this.propertyMetaClass = propertyMetaClass;
    }

    @JsonValue
    public String getProviderName() {
        return this.name();
    }
}
