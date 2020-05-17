package com.sphong.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FormLoginDto {
    @JsonProperty(value = "userId")
    private String id;

    @JsonProperty(value = "password")
    private String password;
}
