package com.xiesx.fastboot.support.validate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import java.util.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class ValidatorHelperTest {

  @Mock Validator validatorMock;

  // 辅助方法：创建模拟的 ConstraintViolation
  private static <T> ConstraintViolation<T> mockViolation(String property, String message) {
    ConstraintViolation<T> violation = mock(ConstraintViolation.class);
    Path path = mock(Path.class);
    when(path.toString()).thenReturn(property);
    when(violation.getPropertyPath()).thenReturn(path);
    when(violation.getMessage()).thenReturn(message);
    return violation;
  }

  // ==================== get() ====================
  @Test
  @Order(1)
  void testGetValidatorSuccess() {
    try (MockedStatic<SpringHelper> mocked = mockStatic(SpringHelper.class)) {
      mocked.when(() -> SpringHelper.getBean(Validator.class)).thenReturn(validatorMock);
      Validator v = ValidatorHelper.get();
      assertNotNull(v);
      assertEquals(validatorMock, v);
    }
  }

  @Test
  @Order(2)
  void testGetValidatorMissing() {
    try (MockedStatic<SpringHelper> mocked = mockStatic(SpringHelper.class)) {
      mocked.when(() -> SpringHelper.getBean(Validator.class)).thenReturn(null);
      RunException ex = assertThrows(RunException.class, ValidatorHelper::get);
      assertEquals(RunExc.DBASE.getCode(), ex.getStatus());
    }
  }

  // ==================== validate() ====================
  @Test
  @Order(3)
  void testValidateNoViolations() {
    Object obj = new Object();
    try (MockedStatic<SpringHelper> mocked = mockStatic(SpringHelper.class)) {
      mocked.when(() -> SpringHelper.getBean(Validator.class)).thenReturn(validatorMock);
      when(validatorMock.validate(obj, Default.class)).thenReturn(Collections.emptySet());
      assertDoesNotThrow(() -> ValidatorHelper.validate(obj));
    }
  }

  @Test
  @Order(4)
  void testValidateWithViolations() {
    Object obj = new Object();
    ConstraintViolation<Object> v = mockViolation("field", "must not be null");
    Set<ConstraintViolation<Object>> violations = Collections.singleton(v);

    try (MockedStatic<SpringHelper> mocked = mockStatic(SpringHelper.class)) {
      mocked.when(() -> SpringHelper.getBean(Validator.class)).thenReturn(validatorMock);
      when(validatorMock.validate(obj, Default.class)).thenReturn(violations);

      ConstraintViolationException e =
          assertThrows(ConstraintViolationException.class, () -> ValidatorHelper.validate(obj));
      assertEquals(violations, e.getConstraintViolations());
    }
  }

  @Test
  @Order(5)
  void testValidateWithGroups() {
    Object obj = new Object();
    Class<?>[] groups = new Class[] {String.class};

    try (MockedStatic<SpringHelper> mocked = mockStatic(SpringHelper.class)) {
      mocked.when(() -> SpringHelper.getBean(Validator.class)).thenReturn(validatorMock);
      when(validatorMock.validate(obj, groups)).thenReturn(Collections.emptySet());

      assertDoesNotThrow(() -> ValidatorHelper.validate(obj, groups));
    }
  }

  // ==================== extractMessage() ====================
  @Test
  @Order(6)
  void testExtractMessage() {
    ConstraintViolation<Object> v1 = mockViolation("field1", "must not be null");
    ConstraintViolation<Object> v2 = mockViolation("field2", "size must be > 0");

    Set<ConstraintViolation<?>> set = new LinkedHashSet<>(Arrays.asList(v1, v2));

    List<String> messages1 = ValidatorHelper.extractMessage(new ConstraintViolationException(set));
    List<String> messages2 = ValidatorHelper.extractMessage(set);

    List<String> expected1 = Arrays.asList("size must be > 0", "must not be null");
    assertEquals(expected1, messages1);

    List<String> expected2 = Arrays.asList("must not be null", "size must be > 0");
    assertEquals(expected2, messages2);
  }

  // ==================== extractPropertyAndMessage() ====================
  @Test
  @Order(7)
  void testExtractPropertyAndMessage() {
    ConstraintViolation<Object> v = mockViolation("name", "cannot be blank");
    Set<ConstraintViolation<?>> set = Collections.singleton(v);
    ConstraintViolationException e = new ConstraintViolationException(set);

    Map<String, String> map1 = ValidatorHelper.extractPropertyAndMessage(e);
    Map<String, String> map2 = ValidatorHelper.extractPropertyAndMessage(set);

    assertEquals("cannot be blank", map1.get("name"));
    assertEquals("cannot be blank", map2.get("name"));
    assertEquals(1, map1.size());
    assertEquals(1, map2.size());
  }

  // ==================== extractPropertyAndMessageAsList() ====================
  @Test
  @Order(8)
  void testExtractPropertyAndMessageAsList() {
    ConstraintViolation<Object> v = mockViolation("age", "must be positive");
    Set<ConstraintViolation<?>> set = Collections.singleton(v);
    ConstraintViolationException e = new ConstraintViolationException(set);

    // 默认 separator
    List<String> list1 = ValidatorHelper.extractPropertyAndMessageAsList(e);
    List<String> list2 = ValidatorHelper.extractPropertyAndMessageAsList(set);
    assertEquals(Collections.singletonList("age must be positive"), list1);
    assertEquals(Collections.singletonList("age must be positive"), list2);

    // 自定义 separator
    List<String> list3 = ValidatorHelper.extractPropertyAndMessageAsList(e, ": ");
    List<String> list4 = ValidatorHelper.extractPropertyAndMessageAsList(set, " -> ");
    assertEquals(Collections.singletonList("age: must be positive"), list3);
    assertEquals(Collections.singletonList("age -> must be positive"), list4);
  }

  // ==================== ValidatorHelper 与真实 Validator 集成测试 ====================
  @Test
  @Order(9)
  void testRealValidatorIntegration() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator realValidator = factory.getValidator();

    class TestVo {
      @javax.validation.constraints.NotNull(message = "cannot be null")
      String name;
    }

    TestVo vo = new TestVo();
    Set<ConstraintViolation<TestVo>> violations = realValidator.validate(vo);

    List<String> messages = ValidatorHelper.extractMessage(violations);
    Map<String, String> propMap = ValidatorHelper.extractPropertyAndMessage(violations);
    List<String> listMsg = ValidatorHelper.extractPropertyAndMessageAsList(violations);

    assertEquals(Collections.singletonList("cannot be null"), messages);
    assertEquals(Collections.singletonMap("name", "cannot be null"), propMap);
    assertEquals(Collections.singletonList("name cannot be null"), listMsg);
  }
}
