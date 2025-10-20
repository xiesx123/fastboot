package com.xiesx.fastboot;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import java.util.Optional;
import org.springframework.context.ApplicationContext;

public class SpringHelper {

  /** 获取项目地址 */
  public static String getUrl() {
    String host = Opt.ofNullable(NetUtil.getLocalhostStr()).orElse("127.0.0.1");
    String port = Opt.ofNullable(SpringUtil.getProperty("server.port")).orElse("8080");
    String path = Opt.ofNullable(SpringUtil.getProperty("server.servlet.context-path")).orElse("/");
    return StrUtil.format("http://{}:{}{}", host, port, path);
  }

  /** 获取上下文 */
  public static ApplicationContext getContext() {
    return SpringUtil.getApplicationContext();
  }

  /** 通过class获取Bean */
  public static <T> T getBean(Class<T> clazz) {
    return SpringUtil.getBean(clazz);
  }

  /** 通过name获取Bean */
  public static <T> T getBean(String name) {
    return SpringUtil.getBean(name);
  }

  /** 通过class、name获取Bean */
  public static <T> T getBean(String name, Class<T> clazz) {
    return SpringUtil.getBean(name, clazz);
  }

  /** 检查是否存在Bean */
  public static <T> Optional<T> hasBean(Class<T> clazz) {
    return Optional.ofNullable(getBean(clazz));
  }
}
