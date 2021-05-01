package com.xiesx.fastboot.support.validate;

import javax.validation.constraints.NotNull;

import com.xiesx.fastboot.app.base.BaseVo;
import com.xiesx.fastboot.support.validate.ValidatorVo.TestVoValid.B;
import com.xiesx.fastboot.support.validate.ValidatorVo.TestVoValid.C;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ValidatorVo extends BaseVo {

    @NotNull(message = "昵称不能为空", groups = B.class)
    private String nickname;

    @NotNull(message = "验证码不能为空", groups = C.class)
    private String code;

    @NotNull(message = "手机不能为空", groups = C.class)
    private String phone;

    public interface TestVoValid {

        public interface A {
        }

        public interface B {
        }

        public interface C {
        }
    }
}
