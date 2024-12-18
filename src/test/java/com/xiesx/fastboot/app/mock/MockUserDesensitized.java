package com.xiesx.fastboot.app.mock;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson2.annotation.JSONField;
import com.xiesx.fastboot.app.enums.StatusEnum;
import com.xiesx.fastboot.core.json.annotation.GoDesensitized;

import cn.hutool.core.util.DesensitizedUtil.DesensitizedType;
import lombok.Data;

/**
 * @title MockDesensitized.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:19:31
 */
@Data
public class MockUserDesensitized {

    @GoDesensitized(type = DesensitizedType.CHINESE_NAME)
    @JSONField(ordinal = 1)
    private String name;

    @JSONField(ordinal = 2)
    private Date birthDay;

    @JSONField(ordinal = 3, format = "yyyy-MM-dd")
    private Date registerDay;

    @JSONField(ordinal = 4)
    @GoDesensitized(type = DesensitizedType.ID_CARD)
    private String idCard;

    @JSONField(ordinal = 5)
    @GoDesensitized(type = DesensitizedType.FIXED_PHONE)
    private String phone;

    @JSONField(ordinal = 6)
    @GoDesensitized(type = DesensitizedType.MOBILE_PHONE)
    private String tel;

    @JSONField(ordinal = 7)
    @GoDesensitized(type = DesensitizedType.ADDRESS)
    private String address;

    @JSONField(ordinal = 8)
    @GoDesensitized(type = DesensitizedType.EMAIL)
    private String email;

    @JSONField(ordinal = 9)
    @GoDesensitized(type = DesensitizedType.PASSWORD)
    private String password;

    @JSONField(ordinal = 10)
    @GoDesensitized(type = DesensitizedType.CAR_LICENSE)
    private String carnumber;

    @JSONField(ordinal = 11)
    private StatusEnum status;

    @JSONField(ordinal = 12, format = "0.00")
    private BigDecimal balance;

    @JSONField(ordinal = 13)
    private boolean enable;
}
