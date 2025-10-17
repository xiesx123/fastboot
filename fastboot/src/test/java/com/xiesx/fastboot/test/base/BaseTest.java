package com.xiesx.fastboot.test.base;

import static io.restassured.RestAssured.given;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Maps;
import com.xiesx.fastboot.core.json.reference.GenericType;
import com.xiesx.fastboot.test.LogRecordRepository;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

public abstract class BaseTest {

    protected @Autowired LogRecordRepository mLogRecordRepository;

    //
    protected static GenericType<Object> gto = new GenericType<Object>() {};

    protected static GenericType<Map<String, Object>> gtm =
            new GenericType<Map<String, Object>>() {};

    protected static GenericType<List<Object>> gtl = new GenericType<List<Object>>() {};

    //
    protected static GenericType<BaseResult<Object>> gtbo =
            new GenericType<BaseResult<Object>>() {};

    protected static GenericType<BaseResult<Map<String, Object>>> gtbm =
            new GenericType<BaseResult<Map<String, Object>>>() {};

    protected static GenericType<BaseResult<List<Object>>> gtbl =
            new GenericType<BaseResult<List<Object>>>() {};

    //
    protected static GenericType<BaseResult<JSON>> gtbj = new GenericType<BaseResult<JSON>>() {};

    @Value("${local.server.port}")
    int port;

    @BeforeEach
    public void init() {
        RestAssured.port = port;
    }

    public static Response get(String url) {
        return given().when().get(url);
    }

    public static Response get(String url, Map<String, ?> headers, Map<String, ?> param) {
        return given().headers(headers).formParams(param).when().get(url);
    }

    public static Response post(String url, Map<String, ?> param) {
        return post(url, Maps.newConcurrentMap(), param);
    }

    public static Response post(String url, Map<String, ?> headers, Map<String, ?> param) {
        return given().headers(headers).formParams(param).when().post(url);
    }
}
