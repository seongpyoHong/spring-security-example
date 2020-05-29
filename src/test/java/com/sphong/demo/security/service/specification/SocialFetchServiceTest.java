package com.sphong.demo.security.service.specification;
import com.sphong.demo.domain.SocialProvider;
import com.sphong.demo.dto.SocialUserDto;
import com.sphong.demo.security.service.implement.SocialFetchServiceImpl;
import com.sphong.demo.security.social.SocialUserProperty;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SocialFetchServiceTest {
    // 내부에 autowired (di) 없고 통합테스트가 아닐 경우 그냥 객체생성해서 테스트하는 것도 방법이다.
    private SocialFetchServiceImpl service = new SocialFetchServiceImpl();
    private SocialUserDto dto;

    @BeforeEach
    public void setUp(){
        this.dto = new SocialUserDto(SocialProvider.KAKAO, "token-value");
    }

    @Test
    public void service_fetchSocialInfo(){
        SocialUserProperty property = service.getSocialUserInfo(this.dto);
        assertThat(property.getEmail(), is("email"));
    }
}
