package com.xiesx.fastboot.support.actuator;

import com.alibaba.fastjson2.JSON;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.support.actuator.model.ActuatorEnv;
import com.xiesx.fastboot.support.actuator.model.ActuatorPlan;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ActuatorTest {

    // @Test
    @Order(1)
    public void executor() {
        //
        ActuatorEnv env = ActuatorEnv.builder().trace("1").debug(true).build();
        //
        String json =
                "{\"id\":\"1\",\"title\":\"测试\",\"env\":{\"a1\":1,\"a2\":2},\"plans\":[{\"name\":\"gitee1-1\",\"method\":\"GET\",\"url\":\"http://rest.apizza.net/mock/46b8b8197618d143b5a76eeae002abbd/test\",\"params\":{\"p1\":\"$.env.a1\",\"p2\":\"$.env.a2\"},\"timeout\":5000,\"ret\":\"key11\",\"rule\":\"\",\"ignoreFailure\":false},[{\"name\":\"gitee3-1\",\"method\":\"GET\",\"url\":\"http://rest.apizza.net/mock/46b8b8197618d143b5a76eeae002abbd/test\",\"params\":{\"p1\":1,\"p2\":2},\"timeout\":5000,\"ret\":\"k31\",\"rule\":\"compare(sum($.env.a1,$.env.a2),2)\",\"ignoreFailure\":false},{\"name\":\"gitee3-2\",\"method\":\"GET\",\"url\":\"http://rest.apizza.net/mock/46b8b8197618d143b5a76eeae002abbd/test\",\"params\":{\"p1\":1,\"p2\":2},\"timeout\":5000,\"ret\":\"k32\",\"rule\":\"\",\"ignoreFailure\":false}],{\"name\":\"gitee1-2\",\"method\":\"GET\",\"url\":\"http://rest.apizza.net/mock/46b8b8197618d143b5a76eeae002abbd/test\",\"params\":{\"p1\":1,\"p2\":2},\"timeout\":5000,\"ret\":\"key12\",\"rule\":\"\",\"ignoreFailure\":false}]}";

        json =
                "{\"id\":\"1\",\"title\":\"测试\",\"env\":{\"a1\":1,\"a2\":2},\"plans\":[{\"name\":\"gitee1-1\",\"type\":\"HTTP\",\"method\":\"GET\",\"url\":\"http://rest.apizza.net/mock/46b8b8197618d143b5a76eeae002abbd/test\",\"params\":{\"p1\":\"$.env.a1\",\"p2\":\"$.env.a2\"},\"timeout\":5000,\"ret\":\"key11\",\"rule\":\"\",\"ignoreFailure\":false},[{\"name\":\"gitee3-1\",\"type\":\"HTTP\",\"method\":\"GET\",\"url\":\"http://rest.apizza.net/mock/46b8b8197618d143b5a76eeae002abbd/test\",\"params\":{\"p1\":1,\"p2\":2},\"timeout\":5000,\"ret\":\"k31\",\"rule\":\"compare($.env.a2,2)\",\"ignoreFailure\":false},{\"name\":\"gitee3-2\",\"type\":\"HTTP\",\"method\":\"GET\",\"url\":\"http://rest.apizza.net/mock/46b8b8197618d143b5a76eeae002abbd/test\",\"params\":{\"p1\":1,\"p2\":2},\"timeout\":5000,\"ret\":\"k32\",\"rule\":\"\",\"ignoreFailure\":false}],{\"name\":\"gitee1-2\",\"type\":\"HTTP\",\"method\":\"GET\",\"url\":\"http://rest.apizza.net/mock/46b8b8197618d143b5a76eeae002abbd/test\",\"params\":{\"p1\":1,\"p2\":2},\"timeout\":5000,\"ret\":\"key12\",\"rule\":\"\",\"ignoreFailure\":false}]}";
        ActuatorPlan plan = JSON.parseObject(json, ActuatorPlan.class);
        env.setCustom(plan.getEnv());
        //
        ActuatorDispatch dispatch = new ActuatorDispatch(env, plan);
        dispatch.execute();
    }
}
