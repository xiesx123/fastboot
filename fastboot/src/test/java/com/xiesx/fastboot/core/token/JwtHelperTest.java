package com.xiesx.fastboot.core.token;

import static org.junit.jupiter.api.Assertions.*;

import cn.hutool.jwt.JWT;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JwtHelperTest {

    @Test
    void testSimpleTokenGenerationAndValidation() {
        String subject = "testSubject";
        String audience = "testAudience";

        String token = JwtHelper.simple(subject, audience);
        assertNotNull(token, "Token should not be null");

        boolean isValid = JwtHelper.validate(token);
        assertTrue(isValid, "Token should be valid");

        JWT parsedToken = JwtHelper.parser(token);
        assertEquals(subject, parsedToken.getPayload("sub"), "Subject should match");
        assertEquals(
                audience, ((List) parsedToken.getPayload("aud")).get(0), "Audience should match");
    }

    @Test
    void testSimpleTokenWithTimeout() {
        String subject = "testSubject";
        String audience = "testAudience";
        long timeout = JwtHelper.JWT_EXPIRE_M_1;

        String token = JwtHelper.simple(subject, audience, timeout);
        assertNotNull(token, "Token should not be null");

        boolean isValid = JwtHelper.validate(token);
        assertTrue(isValid, "Token should be valid");
    }

    @Test
    void testSimpleTokenWithClaims() {
        String subject = "testSubject";
        String audience = "testAudience";
        long timeout = JwtHelper.JWT_EXPIRE_M_5;

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "admin");

        String token = JwtHelper.simple(subject, audience, claims, timeout);
        assertNotNull(token, "Token should not be null");

        JWT parsedToken = JwtHelper.parser(token);
        assertEquals("admin", parsedToken.getPayload("role"), "Claim 'role' should match");
    }

    @Test
    void testTokenCreationWithCustomHeaderAndClaims() {
        String subject = "testSubject";
        String audience = "testAudience";
        long timeout = JwtHelper.JWT_EXPIRE_H_1;

        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "user");

        String token = JwtHelper.simple(subject, audience, header, claims, timeout);
        assertNotNull(token, "Token should not be null");

        JWT parsedToken = JwtHelper.parser(token);
        assertEquals("user", parsedToken.getPayload("role"), "Claim 'role' should match");
        assertEquals("HS256", parsedToken.getHeader("alg"), "Header 'alg' should match");
    }

    @Test
    void testTokenValidationWithCustomSecret() {
        String subject = "testSubject";
        String audience = "testAudience";
        String customSecret = "customSecretKey";

        String token =
                JwtHelper.create(
                        "testId",
                        subject,
                        "testIssuer",
                        audience,
                        null,
                        null,
                        JwtHelper.JWT_EXPIRE_H_12,
                        customSecret);

        boolean isValid = JwtHelper.validate(customSecret, token);
        assertTrue(isValid, "Token should be valid with custom secret");
    }

    @Test
    void testInvalidTokenValidation() {
        String invalidToken = "invalidToken";

        boolean isValid = JwtHelper.validate(invalidToken);
        assertFalse(isValid, "Invalid token should not be valid");
    }
}
