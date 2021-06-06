package com.xiesx.fastboot.db.jpa;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.xiesx.fastboot.FastBootApplication;

/**
 * @title JpaPlusTest.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:21:06
 */
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FastBootApplication.class)
public class JpaPlusTest {

    @Test
    @Order(1)
    public void map() {}
}
