package com.xiesx.fastboot.app.mock;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.xiesx.fastboot.app.enums.StatusEnum;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MockUser {

    @JSONField(ordinal = 1)
    private String name;

    @JSONField(ordinal = 2)
    private Date birthDay;

    @JSONField(ordinal = 3)
    private Date registerDay;

    @JSONField(ordinal = 4)
    private String idCard;

    @JSONField(ordinal = 5)
    private String phone;

    @JSONField(ordinal = 6)
    private String tel;

    @JSONField(ordinal = 7)
    private String address;

    @JSONField(ordinal = 8)
    private String email;

    @JSONField(ordinal = 9)
    private String password;

    @JSONField(ordinal = 10)
    private String carnumber;

    @JSONField(ordinal = 11)
    private StatusEnum status;

    @JSONField(ordinal = 12)
    private Boolean enable;
}
