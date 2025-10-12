package com.xiesx.fastboot.app.mock;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.xiesx.fastboot.app.enums.StatusEnum;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class MockData {

    public static Map<String, Object> map() {
        return Dict.create()
                .set("k1", "1")
                .set("k2", 2)
                .set("k3", 3L)
                .set("k4", 4.1f)
                .set("k5", 5.2d)
                .set("k6", true)
                .set("k7", '7')
                .set("k8", DateTime.now());
    }

    public static List<String> list() {
        return Lists.newArrayList(map().keySet());
    }

    public static JSONObject fastjson() {
        JSONObject jo = new JSONObject(map());
        jo.put("list", list());
        return jo;
    }

    /** 用户信息 */
    public static MockUser user() {

        // ============================== get/set构造

        // // @Date
        MockUser user = new MockUser();
        user.setName("张三");
        user.setBirthDay(DateTime.now());
        user.setRegisterDay(DateTime.now());
        user.setIdCard("51343620000320711X");
        user.setPhone("09127518479");
        user.setTel("13800138000");
        user.setAddress("xx市xx区xxxx街道xxx号");
        user.setEmail("123456789@qq.com");
        user.setPassword(RandomUtil.randomString(8));
        user.setCarnumber("京A88888");
        user.setStatus(StatusEnum.A);
        user.setBalance(new BigDecimal(100.000));
        user.setEnable(false);
        return user;

        // ============================== builder 构造

        //		MockUserBuilder builder = MockUser.builder();
        //		builder.name("张三");
        //		builder.birthDay(DateTime.now());
        //		builder.registerDay(DateTime.now());
        //		builder.idCard("51343620000320711X");
        //		builder.phone("09127518479");
        //		builder.tel("13800138000");
        //		builder.address("xx市xx区xxxx街道xxx号");
        //		builder.email("123456789@qq.com");
        //		builder.password(RandomUtil.randomString(8));
        //		builder.carnumber("京A88888");
        //		builder.balance(new BigDecimal(100.000));
        //		return builder.build();

        // ============================== chain 构造

        // return new MockUser()//
        //     .setName("张三")//
        //     .setBirthDay(DateTime.now())//
        //     .setRegisterDay(DateTime.now())//
        //     .setIdCard("51343620000320711X")//
        //     .setPhone("09127518479")//
        //     .setTel("13800138000")//
        //     .setAddress("xx市xx区xxxx街道xxx号")//
        //     .setEmail("123456789@qq.com")//
        //     .setPassword(RandomUtil.randomString(8))//
        //     .setCarnumber("京A88888")//
        //     .setStatus(StatusEnum.A)//
        //     .setBalance(new BigDecimal(100.000));

        // ============================== fluent 构造

        // MockUser user = new MockUser()//
        // .name("张三")//
        // .birthDay(DateTime.now())//
        // .registerDay(DateTime.now())//
        // .idCard("51343620000320711X")//
        // .phone("09127518479")//
        // .tel("13800138000")//
        // .address("xx市xx区xxxx街道xxx号")//
        // .email("123456789@qq.com")//
        // .password(RandomUtil.randomString(8))//
        // .carnumber("京A88888")//
        // .balance(new BigDecimal(100.000));//
        // return user;
    }
}
