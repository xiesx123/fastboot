package com.xiesx.fastboot.support.actuator;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import com.xiesx.fastboot.core.json.reference.GenericType;
import com.xiesx.fastboot.support.actuator.ActuatorContext.Envar;
import com.xiesx.fastboot.test.base.BaseMock;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class ActuatorTest extends BaseMock {

  GenericType<List<Object>> gtl = new GenericType<List<Object>>() {};

  @Test
  @Order(1)
  public void executor() {

    // 数据 1->2->[31,32]
    String json =
        "[{\"name\":\"node1\",\"type\":\"HTTP\",\"method\":\"GET\",\"url\":\"http://rest.apizza.net/mock/46b8b8197618d143b5a76eeae002abbd/test\",\"params\":{},\"timeout\":5000,\"ret\":\"node1\",\"rule\":\"\",\"ignoreFailure\":false},{\"name\":\"node2\",\"type\":\"HTTP\",\"method\":\"GET\",\"url\":\"http://rest.apizza.net/mock/46b8b8197618d143b5a76eeae002abbd/test\",\"params\":{\"a\":\"$.node1.x\",\"b\":\"$.node1.y\"},\"timeout\":5000,\"ret\":\"node2\",\"rule\":\"\",\"ignoreFailure\":false},[{\"name\":\"node3-1\",\"type\":\"HTTP\",\"method\":\"GET\",\"url\":\"http://rest.apizza.net/mock/46b8b8197618d143b5a76eeae002abbd/test\",\"params\":{\"p1\":311,\"p2\":312},\"timeout\":5000,\"ret\":\"node31\",\"rule\":\"compare($.env.a,2)\",\"ignoreFailure\":false},{\"name\":\"node3-2\",\"type\":\"HTTP\",\"method\":\"GET\",\"url\":\"http://rest.apizza.net/mock/46b8b8197618d143b5a76eeae002abbd/test\",\"params\":{\"p1\":321,\"p2\":322},\"timeout\":5000,\"ret\":\"node32\",\"rule\":\"compare(sum($.env.b,1),2)\",\"ignoreFailure\":false}],{\"name\":\"node4\",\"type\":\"HTTP\",\"method\":\"GET\",\"url\":\"http://rest.apizza.net/mock/46b8b8197618d143b5a76eeae002abbd/test\",\"params\":{\"c\":\"$.node32.x\",\"d\":\"$.node32.y\"},\"timeout\":5000,\"ret\":\"node4\",\"rule\":\"\",\"ignoreFailure\":false}]";

    // 上下文
    ActuatorContext context =
        ActuatorContext.builder()
            .trace(IdUtil.fastSimpleUUID())
            .title("测试")
            .env(
                Envar.builder()
                    .custom(Dict.create().set("a", 1).set("b", 2))
                    .saveDir("D:")
                    .debug(true)
                    .build())
            .build();

    // 执行器
    ActuatorDispatch dispatch =
        new ActuatorDispatch(
            context,
            gtl.parseObject(json),
            new ActuatorFutureCallback() {

              @Override
              public void onSuccess(ActuatorContext ctx, Dict result) {
                super.onSuccess(ctx, result);
                Console.log(ctx);
                Console.log(result);
              }

              @Override
              public void onFailure(ActuatorContext ctx, Throwable t) {
                super.onFailure(ctx, t);
                Console.log(ctx);
              }
            });
    dispatch.execute();
  }
}
