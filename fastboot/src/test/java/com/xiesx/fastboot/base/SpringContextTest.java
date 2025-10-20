package com.xiesx.fastboot.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.system.SystemUtil;
import cn.hutool.system.UserInfo;
import com.xiesx.fastboot.SpringContext;
import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.support.scheduler.ScheduleHelper;
import com.xiesx.fastboot.test.base.BaseMock;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.Scheduler;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class SpringContextTest extends BaseMock {

  @Mock UserInfo mockUserInfo;

  @Mock Scheduler mockScheduler;

  // 1. getServerName()
  @Test
  @Order(1)
  void testGetServerNameInitiallyNull() {
    assertNull(SpringContext.getServerName());
  }

  // 2. getServerPath()
  @Test
  @Order(2)
  void testGetServerPathInitiallyNull() {
    assertNull(SpringContext.getServerPath());
  }

  // 3. getApplicationContext()
  @Test
  @Order(3)
  void testGetApplicationContextInitiallyNull() {
    assertNull(SpringContext.getApplicationContext());
  }

  // 4. setApplicationContext()
  @Test
  @Order(4)
  void testSetApplicationContext() {
    SpringContext springContext = new SpringContext();
    try (MockedStatic<ObjectUtil> mocked = mockStatic(ObjectUtil.class)) {
      mocked.when(() -> ObjectUtil.isNotNull(mockContext)).thenReturn(true);
      springContext.setApplicationContext(mockContext);
      assertEquals(mockContext, SpringContext.getApplicationContext());
    }
  }

  // 5. onApplicationEvent() with scheduler present
  @Test
  @Order(5)
  void testSetApplicationContextNull() {
    try (MockedStatic<ObjectUtil> mocked = mockStatic(ObjectUtil.class)) {
      mocked.when(() -> ObjectUtil.isNotNull(any())).thenReturn(false);
      SpringContext context = new SpringContext();
      context.setApplicationContext(mockContext);
      mocked.verify(() -> ObjectUtil.isNotNull(SpringContext.getApplicationContext()), times(1));
      assertSame(mockContext, SpringContext.getApplicationContext());
    }
  }

  @Test
  @Order(6)
  void testOnApplicationEventWithScheduler() {
    // 模拟路径
    String serverPath = "/home/server/app";

    // 模拟事件上下文
    when(mockEvent.getApplicationContext()).thenReturn(mockContext);
    when(mockContext.getParent()).thenReturn(null);

    // 模拟 UserInfo 返回路径
    when(mockUserInfo.getCurrentDir()).thenReturn(serverPath);

    // 创建 SpringContext 实例
    SpringContext springContext = new SpringContext();

    try (MockedStatic<FileUtil> fileMock = mockStatic(FileUtil.class);
        MockedStatic<SystemUtil> systemMock = mockStatic(SystemUtil.class);
        MockedStatic<SpringHelper> springHelperMock = mockStatic(SpringHelper.class);
        MockedStatic<ScheduleHelper> scheduleMock = mockStatic(ScheduleHelper.class)) {

      // 模拟 FileUtil.getName(serverPath) 返回 servername
      fileMock.when(() -> FileUtil.getName(serverPath)).thenReturn("app");

      // 模拟 SystemUtil.getUserInfo() 返回 mockUserInfo
      systemMock.when(SystemUtil::getUserInfo).thenReturn(mockUserInfo);

      // 模拟 SpringHelper.hasBean(Scheduler.class) 返回 mockScheduler
      springHelperMock
          .when(() -> SpringHelper.hasBean(Scheduler.class))
          .thenReturn(Optional.of(mockScheduler));

      // 模拟 ScheduleHelper.queryAllJob() 返回一个任务列表
      List<Map<String, Object>> mockJobs =
          Lists.newArrayList(Collections.singletonMap("job", "test"));
      scheduleMock.when(ScheduleHelper::queryAllJob).thenReturn(mockJobs);

      // 执行事件处理
      springContext.onApplicationEvent(mockEvent);

      // 断言 servername 和 serverpath 被正确设置
      assertEquals("app", SpringContext.getServerName());
      assertEquals(serverPath, SpringContext.getServerPath());
    }
  }

  // 6. checkSchedulerClassExists() returns true
  @Test
  @Order(7)
  void testCheckSchedulerClassExistsTrue() throws Exception {
    SpringContext context = new SpringContext();
    Method method = SpringContext.class.getDeclaredMethod("checkSchedulerClassExists");
    method.setAccessible(true);
    boolean result = (boolean) method.invoke(context);
    assertTrue(result); // 如果 quartz 包存在，应该返回 true
  }
}
