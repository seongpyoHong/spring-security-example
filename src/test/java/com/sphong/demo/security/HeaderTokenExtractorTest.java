package com.sphong.demo.security;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

class HeaderTokenExtractorTest {
    private HeaderTokenExtractor headerTokenExtractor = new HeaderTokenExtractor();
    private String header;

    @BeforeEach
    public void setUp() {
        this.header = "Bearer asdasdasdasdasdasdasdasd";
    }

    @Test
    public void JwtExtractorTest() {
        assertEquals("asdasdasdasdasdasdasdasd",headerTokenExtractor.extract(header));
    }
}