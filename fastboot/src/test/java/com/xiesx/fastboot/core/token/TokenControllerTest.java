package com.xiesx.fastboot.core.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTPayload;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Maps;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.base.config.Configed;
import com.xiesx.fastboot.core.token.configuration.TokenCfg;
import com.xiesx.fastboot.core.token.configuration.TokenProperties;
import com.xiesx.fastboot.test.base.BaseApi;
import com.xiesx.fastboot.test.base.BaseResult;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class TokenControllerTest extends BaseApi {

  @Autowired TokenProperties properties;

  @Value("${fastboot.token.secret}")
  String secret;

  Map<String, Object> param, header;

  public static String generate() {
    //
    String secret =
        Opt.ofNullable(SpringUtil.getProperty("fastboot.token.secret"))
            .orElse(TokenProperties.SECRET);
    //
    Map<String, Object> headers = Maps.newHashMap();
    headers.put("subscribe", "free");
    //
    Map<String, Object> payload = Maps.newHashMap();
    payload.put(TokenCfg.UID, "1");
    //
    String token =
        JwtHelper.build(
            JwtHelper.JWT_EXPIRE_M_1, headers, payload, Configed.FASTBOOT, Configed.FASTBOOT);
    Console.log(token);
    //
    JWT jwt = JwtHelper.parser(secret, token);
    Console.log("签名算法：" + jwt.getSigner().getAlgorithm());
    //
    JWTHeader jh = jwt.getHeader();
    Console.log("头部信息：" + jh.getClaimsJson());
    //
    JWTPayload jp = jwt.getPayload();
    Console.log("负载信息：" + jp.getClaimsJson());

    return token;
  }

  @BeforeEach
  public void setup() {
    super.setup();
    // 参数
    param = Maps.newHashMap();
    param.put("name", "fasotboot");
    // 头部
    header = Maps.newHashMap();
    header.put(properties.getHeader(), generate());
    header.put("h1", 1);
  }

  @Test
  @Order(1)
  public void token() {
    Response response = get("token/header", header, param);
    BaseResult<List<Object>> result = gtbl.parseObject(response.asString());
    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertEquals(result.getData().get(0), "fasotboot");
    assertEquals(result.getData().get(1), "1");
    assertEquals(result.getData().get(2), "free");
    TokenVo hp = JSON.parseObject(result.getData().get(3).toString(), TokenVo.class);
    assertEquals(hp.getH1(), "1");
  }

  @Test
  @Order(2)
  public void token_empty() {
    Response response = get("token/header", Maps.newHashMap(), param);
    BaseResult<List<Object>> result = gtbl.parseObject(response.asString());
    assertNotNull(result);
    assertFalse(result.isSuccess());
    assertTrue(result.getMsg().contains("token is empty"));
  }

  @Test
  @Order(3)
  public void token_expired() {
    String token =
        JwtHelper.build(
            TimeUnit.MILLISECONDS.toSeconds(1), param, param, Configed.FASTBOOT, Configed.FASTBOOT);
    header.put(properties.getHeader(), token);
    ThreadUtil.sleep(100);
    Response response = get("token/header", header, param);
    BaseResult<List<Object>> result = gtbl.parseObject(response.asString());
    assertNotNull(result);
    assertFalse(result.isSuccess());
    assertTrue(result.getMsg().contains("token is expired"));
  }

  @Test
  @Order(4)
  public void token_error() {
    header.put(properties.getHeader(), "xxx");
    Response response = get("token/header", header, param);
    BaseResult<List<Object>> result = gtbl.parseObject(response.asString());
    assertNotNull(result);
    assertFalse(result.isSuccess());
    assertTrue(result.getMsg().contains("token was expected"));
  }

  public static void main(String[] args) {
    generate();
  }
}
