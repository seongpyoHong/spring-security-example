package com.sphong.demo.security.social;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class KakaoUserProperty implements SocialUserProperty {
    @JsonProperty("kakao_account.id")
    private Long id;

    @JsonProperty("kakao_account.email")
    private String email;

    @JsonProperty("kakao_account.profile.profile_image_url")
    private String profileHref;

    @JsonProperty("kakao_account.profile.nickname")
    private String nickname;

    @Override
    public String getUserId() {
        return this.id.toString();
    }

    @Override
    public String getUserNickname() {
        return this.nickname;
    }

    @Override
    public String getProfileHref() {
        return this.profileHref;
    }

    @Override
    public String getEmail() {
        return this.email;
    }
}
