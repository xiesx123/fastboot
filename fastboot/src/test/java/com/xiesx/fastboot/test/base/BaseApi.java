package com.xiesx.fastboot.test.base;

import static io.restassured.RestAssured.given;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Maps;
import com.xiesx.fastboot.core.json.reference.GenericType;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.server.LocalServerPort;

public abstract class BaseApi {

  //
  protected GenericType<Object> gto = new GenericType<Object>() {};

  protected GenericType<Map<String, Object>> gtm = new GenericType<Map<String, Object>>() {};

  protected GenericType<List<Object>> gtl = new GenericType<List<Object>>() {};

  //
  protected GenericType<BaseResult<Object>> gtbo = new GenericType<BaseResult<Object>>() {};

  protected GenericType<BaseResult<Map<String, Object>>> gtbm =
      new GenericType<BaseResult<Map<String, Object>>>() {};

  protected GenericType<BaseResult<List<Object>>> gtbl =
      new GenericType<BaseResult<List<Object>>>() {};

  //
  protected GenericType<BaseResult<JSON>> gtbj = new GenericType<BaseResult<JSON>>() {};

  @LocalServerPort int port;

  @BeforeEach
  public void setup() {
    RestAssured.port = port;
  }

  //
  public static Response get(String url) {
    return given().when().get(url);
  }

  public static Response get(String url, Map<String, ?> headers, Map<String, ?> param) {
    return given().headers(headers).formParams(param).when().get(url);
  }

  public static Response post(String url, Map<String, ?> param) {
    return post(url, Maps.newHashMap(), param);
  }

  public static Response post(String url, Map<String, ?> headers, Map<String, ?> param) {
    return given().headers(headers).formParams(param).when().post(url);
  }
}
