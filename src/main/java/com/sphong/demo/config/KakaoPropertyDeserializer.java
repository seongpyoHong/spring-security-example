package com.sphong.demo.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.sphong.demo.security.social.KakaoUserProperty;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class KakaoPropertyDeserializer extends StdDeserializer<KakaoUserProperty> {
    public KakaoPropertyDeserializer() {
        this(null);
    }
    public KakaoPropertyDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public KakaoUserProperty deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode jsonNode = p.getCodec().readTree(p);
        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.path("kakao_account").get("email").asText();
        String profileHref = jsonNode.path("kakao_account").path("profile").get("profile_image_url").asText();
        String nickname = jsonNode.path("kakao_account").path("profile").get("nickname").asText();
        return KakaoUserProperty.builder().id(id).email(email).profileHref(profileHref).nickname(nickname).build();
    }
}
