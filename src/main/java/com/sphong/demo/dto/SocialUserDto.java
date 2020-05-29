package com.sphong.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sphong.demo.domain.SocialProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialUserDto {
    @JsonProperty("provider")
    private SocialProvider provider;

    @JsonProperty("token")
    private String token;
}
