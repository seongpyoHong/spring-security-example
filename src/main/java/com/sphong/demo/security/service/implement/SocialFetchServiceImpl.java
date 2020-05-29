package com.sphong.demo.security.service.implement;

import com.sphong.demo.domain.SocialProvider;
import com.sphong.demo.dto.SocialUserDto;
import com.sphong.demo.security.service.specification.SocialFetchService;
import com.sphong.demo.security.social.SocialUserProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SocialFetchServiceImpl implements SocialFetchService {
    private static final String HEADER_PREFIX = "Bearer ";
    @Override
    public SocialUserProperty getSocialUserInfo(SocialUserDto socialUserDto) {
        SocialProvider provider = socialUserDto.getProvider();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>("parameter", generateHeader(socialUserDto.getToken()));

        return restTemplate.exchange(provider.getEndpoint(),
                                    HttpMethod.GET,
                                    entity,
                                    provider.getPropertyMetaClass()).getBody();
    }

    private HttpHeaders generateHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", generateHeaderContent(token));
        return headers;
    }

    private String generateHeaderContent(String token) {
        StringBuilder sb = new StringBuilder();
        sb.append(HEADER_PREFIX);
        sb.append(token);
        return sb.toString();
    }
}
