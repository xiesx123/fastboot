package com.xiesx.fastboot.app.mock;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.xiesx.fastboot.app.enums.StatusEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;

/**
 * @title MockData.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:19:27
 */
public class MockData {

    public static Map<String, Object> map() {
        Dict dict = Dict.create()//
                .set("k1", "1")//
                .set("k2", 2)//
                .set("k3", 3L)//
                .set("k4", 4.1f)//
                .set("k5", 5.2d)//
                .set("k6", true)//
                .set("k7", '7')//
                .set("k8", DateTime.now());
        return dict;
    }

    public static List<String> list() {
        return Lists.newArrayList(map().keySet());
    }

    public static JSON fastjson() {
        JSONObject jo = new JSONObject(map());
        jo.put("list", list());
        return jo;
    }

    public static MockUser user() {

        // ============================== get/set构造

        // // @Date
        // MockUser user = new MockUser();
        // user.setName("段正淳");
        // user.setBirthDay(DateTime.now());
        // user.setRegisterDay(DateTime.now());
        // user.setIdCard("51343620000320711X");
        // user.setPhone("09127518479");
        // user.setTel("13800138000");
        // user.setAddress("xx市xx区xxxx街道xxx号");
        // user.setEmail("123456789@qq.com");
        // user.setPassword(RandomUtil.randomString(8));
        // user.setCarnumber("京A88888");
        // return user;

        // ============================== builder构造

        // //@Data
        // //@Builder
        // MockUserBuilder builder = MockUser.builder();
        // builder.name("段正淳");
        // builder.birthDay(DateTime.now());
        // builder.registerDay(DateTime.now());
        // builder.idCard("51343620000320711X");
        // builder.phone("09127518479");
        // builder.tel("13800138000");
        // builder.address("xx市xx区xxxx街道xxx号");
        // builder.email("123456789@qq.com");
        // builder.password(RandomUtil.randomString(8));
        // builder.carnumber("京A88888");
        // return builder.build();

        // ============================== chain构造

        // @Date
        // @Accessors(chain = true)
        MockUser user = new MockUser()//
                .setName("段正淳")//
                .setBirthDay(DateTime.now())//
                .setRegisterDay(DateTime.now())//
                .setIdCard("51343620000320711X")//
                .setPhone("09127518479")//
                .setTel("13800138000")//
                .setAddress("xx市xx区xxxx街道xxx号")//
                .setEmail("123456789@qq.com")//
                .setPassword(RandomUtil.randomString(8))//
                .setCarnumber("京A88888")//
                .setStatus(StatusEnum.A);//
        return user;

        // ============================== fluent构造

        // // @Date
        // // @Accessors(fluent = true)
        // MockUser user = new MockUser()//
        // .name("段正淳")//
        // .birthDay(DateTime.now())//
        // .registerDay(DateTime.now())//
        // .idCard("51343620000320711X")//
        // .phone("09127518479")//
        // .tel("13800138000")//
        // .address("xx市xx区xxxx街道xxx号")//
        // .email("123456789@qq.com")//
        // .password(RandomUtil.randomString(8))//
        // .carnumber("京A88888");//
        // return user;
    }

    public static MockDesensitized user2() {
        MockUser user = user();
        MockDesensitized user2 = new MockDesensitized();
        BeanUtil.copyProperties(user, user2, true);
        return user2;
    }
}
