package com.sphong.demo.security.social;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sphong.demo.config.KakaoPropertyDeserializer;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonDeserialize(using = KakaoPropertyDeserializer.class)
public class KakaoUserProperty implements SocialUserProperty {
    private Long id;

    private String email;

    private String profileHref;

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

    @Builder
    public KakaoUserProperty(Long id, String email, String profileHref, String nickname) {
        this.id = id;
        this.email = email;
        this.profileHref = profileHref;
        this.nickname = nickname;
    }
}
