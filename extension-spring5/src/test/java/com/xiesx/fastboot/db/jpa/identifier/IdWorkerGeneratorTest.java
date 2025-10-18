package com.xiesx.fastboot.db.jpa.identifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Properties;

class IdWorkerGeneratorTest {

    private IdWorkerGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new IdWorkerGenerator();
    }

    @Test
    void testConfigureWithPrefixAndIds() {
        Properties props = new Properties();
        props.setProperty("prefix", "TEST_");
        props.setProperty("workerId", "1");
        props.setProperty("centerId", "2");

        Type mockType = mock(Type.class);
        ServiceRegistry mockRegistry = mock(ServiceRegistry.class);

        generator.configure(mockType, props, mockRegistry);

        assertEquals("TEST_", generator.prefix);
        assertEquals(1L, generator.workerId);
        assertEquals(2L, generator.centerId);
        assertNotNull(
                generator.generate(mock(SharedSessionContractImplementor.class), new Object()));
    }

    @Test
    void testGenerateWithPrefix() {
        Properties props = new Properties();
        props.setProperty("prefix", "PRE_");
        props.setProperty("workerId", "1");
        props.setProperty("centerId", "1");

        generator.configure(null, props, null); // 初始化 prefix 和 snowflake

        SharedSessionContractImplementor session = mock(SharedSessionContractImplementor.class);
        Serializable id = generator.generate(session, new Object());

        assertTrue(id instanceof String);
        assertTrue(((String) id).startsWith("PRE_"));
    }

    @Test
    void testGenerateWithoutPrefix() {
        generator.prefix = null;
        generator.workerId = 0L;
        generator.centerId = 0L;
        generator.configure(null, new Properties(), null);

        SharedSessionContractImplementor session = mock(SharedSessionContractImplementor.class);
        Serializable id = generator.generate(session, new Object());

        assertTrue(id instanceof Long);
    }

    @Test
    void testStaticNextId() {
        Long id = IdWorkerGenerator.nextId(1, 1);
        assertNotNull(id);
    }

    @Test
    void testStaticNextIdDefault() {
        Long id = IdWorkerGenerator.nextId();
        assertNotNull(id);
    }
}
