package com.eci.edu.ieti.tastemap.user.config;

import com.eci.edu.ieti.tastemap.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppConfigTest {

    @Test
    void testBCryptPasswordEncoder() {
        AppConfig appConfig = new AppConfig();
        BCryptPasswordEncoder encoder = appConfig.bCryptPasswordEncoder();
        assertNotNull(encoder);
    }
}
