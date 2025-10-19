package com.xiesx.fastboot.core.signature;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class SignerAspectTest {

    SignerAspect cls;

    SignerHelper helper;

    @BeforeEach
    void setup() {
        cls = new SignerAspect();
        helper = new SignerHelper();
    }

    @Test
    @Order(1)
    void testConstructor() {
        cls.signerPointcut();
        assertNotNull(cls);
    }
}
