# 事件总线

轻量灵活的组件间通信机制，无需 Spring 依赖，使事件驱动逻辑更简洁，更易测试与复用

## 注解

`@GoEnableBody`

## 示例

### 定义

定义事件类似、事件处理

::: code-group

```java [SimpleEvent.java]
@Data
@AllArgsConstructor
public class SimpleEvent {

    @NonNull
    private String name;
}

```

```java [SimpleEventHandler.java]
@Log4j2
@Component
public class SimpleEventHandler extends EventAdapter<SimpleEvent> {

    @Override
    public boolean process(SimpleEvent e) throws InterruptedException {
        log.debug("==================== 收到【{}】事件 ===================", e.getName());
        TimeUnit.SECONDS.sleep(2);
        log.debug("==================== 结束【{}】事件 ===================", e.getName());
        return true;
    }
}
```

:::

### 发布

发布事件

```java
EventBusHelper.post(new SimpleEvent("测试"));
```

```log
2025-10-11 13:15:32 INFO pool-2-thread-1:7130 SimpleEventHandler.java:15 - ==================== 收到【测试】事件 ===================
2025-10-11 13:15:32 INFO pool-2-thread-1:7130 SimpleEventHandler.java:17 - sleep
2025-10-11 13:15:34 INFO pool-2-thread-1:9131 SimpleEventHandler.java:20 - ==================== 结束【测试】事件 ===================
```
