package com.xiesx.fastboot;

import com.xiesx.fastboot.db.jpa.annotation.EnableJpaPlusRepositories;

import lombok.extern.log4j.Log4j2;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.UnsupportedEncodingException;

@Log4j2
@EnableJpaAuditing
@EnableJpaPlusRepositories
@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) throws UnsupportedEncodingException {
        SpringApplication app = new SpringApplication(ExampleApplication.class);
        app.setBannerMode(Banner.Mode.CONSOLE);
        app.run(args);
        log.info("Started ExampleApplication Successfully");
    }
}
