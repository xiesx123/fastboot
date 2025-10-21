package com.xiesx.fastboot.db.jpa.identifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Member;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.EventType;
import org.hibernate.id.factory.spi.CustomIdGeneratorCreationContext;
import org.junit.jupiter.api.Test;

class GeneratorIdWorkerTest {

  @Test
  void testGenerateWithPrefix() {
    // 模拟注解配置
    GeneratedIdId config = mock(GeneratedIdId.class);
    when(config.prefix()).thenReturn("TEST_");
    when(config.workerId()).thenReturn(1);
    when(config.centerId()).thenReturn(2);

    Member member = mock(Member.class);
    CustomIdGeneratorCreationContext context = mock(CustomIdGeneratorCreationContext.class);

    GeneratorIdWorker generator = new GeneratorIdWorker(config, member, context);
    generator.getEventTypes();

    SharedSessionContractImplementor session = mock(SharedSessionContractImplementor.class);
    Object result = generator.generate(session, new Object(), null, EventType.INSERT);

    assertTrue(result instanceof String);
    assertTrue(((String) result).startsWith("TEST_"));
  }

  @Test
  void testGenerateWithoutPrefix() {
    GeneratedIdId config = mock(GeneratedIdId.class);
    when(config.prefix()).thenReturn("");
    when(config.workerId()).thenReturn(1);
    when(config.centerId()).thenReturn(2);

    Member member = mock(Member.class);
    CustomIdGeneratorCreationContext context = mock(CustomIdGeneratorCreationContext.class);

    GeneratorIdWorker generator = new GeneratorIdWorker(config, member, context);

    SharedSessionContractImplementor session = mock(SharedSessionContractImplementor.class);
    Object result = generator.generate(session, new Object(), null, EventType.INSERT);

    assertTrue(result instanceof Long);
  }

  @Test
  void testStaticNextId() {
    Long id = GeneratorIdWorker.nextId(3, 4);
    assertNotNull(id);
  }

  @Test
  void testStaticNextIdDefault() {
    Long id = GeneratorIdWorker.nextId();
    assertNotNull(id);
  }
}
