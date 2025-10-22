package com.xiesx.fastboot.core.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import com.xiesx.fastboot.base.config.Configed;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class JwtHelperTest {
  private static final String ID = "testId";
  private static final String SUBJECT = "testSubject";
  private static final String ISSUER = "testIssuer";
  private static final String AUDIENCE = "testAudience";
  private static final String SECRET = "";
  private static final long TIMEOUT = JwtHelper.JWT_EXPIRE_D_1;

  private static String token;

  JwtHelper cls;

  @BeforeEach
  void setup() {
    cls = new JwtHelper();
    token = JwtHelper.build(TIMEOUT, AUDIENCE, SECRET);
  }

  @Test
  void testConstructor() {
    assertNotNull(cls);
  }

  @Test
  @Order(1)
  void testBuild_timeoutAudienceSecret() {
    token = JwtHelper.build(TIMEOUT, AUDIENCE, SECRET);
    assertNotNull(token);
  }

  @Test
  @Order(2)
  void testBuild_withHeaderAndClaim() {
    Map<String, Object> header = new HashMap<>();
    header.put("alg", "HS256");
    Map<String, Object> claim = new HashMap<>();
    claim.put("role", "admin");
    String result = JwtHelper.build(TIMEOUT, header, claim, AUDIENCE, SECRET);
    assertNotNull(result);
  }

  @Test
  @Order(3)
  void testBuild_fullArguments() {
    Map<String, Object> header = Collections.singletonMap("typ", "JWT");
    Map<String, Object> claim = Collections.singletonMap("scope", "read");
    String result = JwtHelper.build(ID, SUBJECT, ISSUER, TIMEOUT, header, claim, AUDIENCE, SECRET);
    assertNotNull(result);
  }

  @Test
  @Order(4)
  void testCreate_withDates() {
    Date start = new Date();
    Date end = new Date(start.getTime() + TIMEOUT * 1000);
    Map<String, Object> header = Collections.singletonMap("alg", "HS256");
    Map<String, Object> claim = Collections.singletonMap("data", "value");
    String result =
        JwtHelper.create(ID, SUBJECT, ISSUER, start, end, header, claim, AUDIENCE, SECRET);
    assertNotNull(result);
  }

  @Test
  @Order(5)
  void testParser_withSecret() {
    JWT jwt = JwtHelper.parser(SECRET, token);
    JSONObject claims = jwt.getPayloads();
    assertNotNull(jwt);
    assertEquals(Configed.FASTBOOT, claims.getStr("iss"));
  }

  @Test
  @Order(6)
  void testValidate_withSecret_validToken() {
    boolean result = JwtHelper.validate(SECRET, token);
    assertTrue(result);
  }

  @Test
  @Order(7)
  void testValidate_withSecret_invalidToken() {
    boolean result = JwtHelper.validate(SECRET, "invalid.token");
    assertFalse(result);
  }
}
