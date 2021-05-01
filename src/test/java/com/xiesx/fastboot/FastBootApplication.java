package com.xiesx.fastboot;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.xiesx.fastboot.db.jpa.annotation.EnableJpaPlusRepositories;

import lombok.extern.log4j.Log4j2;

/**
 * @title FastBootApplication.java
 * @description
 * @author xiesx
 * @date 2021-04-03 16:10:20
 */
@Log4j2
@EnableJpaAuditing
@EnableJpaPlusRepositories
@SpringBootApplication
public class FastBootApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FastBootApplication.class);
        app.setBannerMode(Banner.Mode.CONSOLE);
        app.run(args);
        log.info("Started FastBootApplication 启动成功");
    }
}
