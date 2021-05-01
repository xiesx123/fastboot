package com.xiesx.fastboot.app.base;

import static io.restassured.RestAssured.given;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.web.server.LocalServerPort;

import com.alibaba.fastjson.TypeReference;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * @title BaseTest.java
 * @description
 * @author xiesx
 * @date 2020-12-21 6:16:27
 */
public abstract class BaseTest {

    //
    protected static TypeReference<Object> tr_Obj = new TypeReference<Object>() {};

    protected static TypeReference<Map<String, Object>> tr_Map = new TypeReference<Map<String, Object>>() {};

    //
    protected static TypeReference<BaseResult<Object>> tr_B_Obj = new TypeReference<BaseResult<Object>>() {};

    protected static TypeReference<BaseResult<Map<String, Object>>> tr_B_Map = new TypeReference<BaseResult<Map<String, Object>>>() {};

    //
    protected static TypeReference<BaseResult<List<Object>>> tr_B_List = new TypeReference<BaseResult<List<Object>>>() {};

    @LocalServerPort
    int port;

    @BeforeEach
    public void init() {
        RestAssured.port = port;
    }

    public static Response get(String url) {
        return given().request().when().get(url).then().extract().response();
    }

    public static Response post(String url, Map<String, Object> param) {
        return post(url, null, param);
    }

    public static Response post(String url, Map<String, Object> headers, Map<String, Object> param) {
        return given().contentType(ContentType.JSON).request().headers(headers).body(param).when().post(url).then().extract().response();
    }
}
