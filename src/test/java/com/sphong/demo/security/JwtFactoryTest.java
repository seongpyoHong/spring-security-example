package com.sphong.demo.security;

import com.sphong.demo.domain.Account;
import com.sphong.demo.domain.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
class JwtFactoryTest {

    private static final Logger logger = LoggerFactory.getLogger(JwtFactoryTest.class);
    private AccountContext context;

    @Autowired
    private JwtFactory jwtFactory;

    @BeforeEach
    public void setUp() {
        Account account = new Account();
        account.setUserRole(UserRole.USER);
        account.setUserId("userid");
        account.setPassword("1234");
        this.context = AccountContext.fromAccountModel(account);
    }

    @Test
    public void TEST_JWT_GENERATE() {
        logger.error(() -> jwtFactory.generateToken(this.context));
    }
}