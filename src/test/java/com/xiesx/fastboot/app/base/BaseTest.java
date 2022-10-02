package com.xiesx.fastboot.app.base;

import static io.restassured.RestAssured.given;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.xiesx.fastboot.core.json.reference.GenericType;

import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * @title BaseTest.java
 * @description
 * @author xiesx
 * @date 2020-12-21 6:16:27
 */
public abstract class BaseTest {

    //
    protected static GenericType<Object> gtObj = new GenericType<Object>() {};

    protected static GenericType<Map<String, Object>> gtMap = new GenericType<Map<String, Object>>() {};

    //
    protected static GenericType<BaseResult<Object>> gtBaseObj = new GenericType<BaseResult<Object>>() {};

    protected static GenericType<BaseResult<Map<String, Object>>> gtBaseMap = new GenericType<BaseResult<Map<String, Object>>>() {};

    protected static GenericType<BaseResult<List<Object>>> gtBaseList = new GenericType<BaseResult<List<Object>>>() {};

    //
    protected static GenericType<BaseResult<JSON>> gtBaseJson = new GenericType<BaseResult<JSON>>() {};

    @Value("${local.server.port}")
    int port;

    @BeforeEach
    public void init() {
        RestAssured.port = port;
    }

    public static Response get(String url) {
        return given().when().get(url);
    }

    public static Response post(String url, Map<String, String> param) {
        return post(url, Maps.newConcurrentMap(), param);
    }

    public static Response post(String url, Map<String, String> headers, Map<String, String> param) {
        return given().headers(headers).formParams(param).when().post(url);
    }
}
