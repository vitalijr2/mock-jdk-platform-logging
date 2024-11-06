package io.github.vitalijr2.mock.jdk.platform.logging;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("slow")
class MockLoggerFinderSlowTest {

  private static Logger logger;

  @BeforeAll
  static void setUpClass() {
    logger = System.getLogger("test");
  }

  @AfterEach
  void tearDown() {
    clearInvocations(logger);
  }

  @DisplayName("Test")
  @ParameterizedTest
  @ValueSource(strings = {"TRACE", "DEBUG", "INFO", "WARNING", "ERROR"})
  void test(Level level) {
    // given
    verifyNoInteractions(logger);

    // when
    System.getLogger("test").log(level, "test message");

    // then
    verify(logger).log(level, "test message");
  }

  @DisplayName("Property file does not exists, use defaults")
  @Test
  void propertyFileDoesNotExist() {
    // when
    var properties = assertDoesNotThrow(() -> MockLoggerFinder.loadProperties("i-do-not-exist.properties"));

    // then
    assertAll("Properties from fallback", () -> assertEquals(1, properties.size()),
        () -> assertEquals("", properties.getProperty("includes")));
  }

  @DisplayName("Property file exists")
  @Test
  void propertyFileExists() {
    // when
    var properties = assertDoesNotThrow(() -> MockLoggerFinder.loadProperties("i-am-a-test.properties"));

    // then
    assertAll("Properties from fallback", () -> assertEquals(2, properties.size()),
        () -> assertEquals("test-include", properties.getProperty("includes")),
        () -> assertEquals("test-exclude", properties.getProperty("excludes")));
  }

  @DisplayName("Input/output exception")
  @Test
  void inputOutputException() throws IOException {
    try (var loggerFinder = mockStatic(MockLoggerFinder.class)) {
      // given
      var fallbackInputStream = mock(InputStream.class);

      loggerFinder.when(MockLoggerFinder::fallbackInputStream).thenReturn(fallbackInputStream);
      loggerFinder.when(() -> MockLoggerFinder.loadProperties(anyString())).thenCallRealMethod();
      when(fallbackInputStream.read(isA(byte[].class))).thenThrow(new IOException("test exception"));

      // when
      var exception = assertThrows(RuntimeException.class, () -> MockLoggerFinder.loadProperties("qwerty.properties"));

      // then
      assertAll("IOException is thrown",
          () -> assertEquals("Could not load configuration properties", exception.getMessage()),
          () -> assertEquals("test exception", exception.getCause().getMessage()));
    }

  }

  @DisplayName("Logger filter")
  @Test
  void loggerFilter() {
    // when
    var loggerFilter = MockLoggerFinder.craftLoggerFilters("abc.qwerty.xyz", "abc.qwerty");

    // then
    assertTrue(loggerFilter.test("abc.qwerty.uv".split("\\.")));
    assertFalse(loggerFilter.test("abc.qwerty.xyz.st".split("\\.")));
    assertFalse(loggerFilter.test("abc.ij".split("\\.")));
  }

  @DisplayName("Logger filter with defaults")
  @Test
  void loggerFilterWithDefaults() {
    // when
    var loggerFilter = MockLoggerFinder.craftLoggerFilters(null, null);

    // then
    assertTrue(loggerFilter.test("abc.qwerty.uv".split("\\.")));
    assertTrue(loggerFilter.test("abc.qwerty.xyz.st".split("\\.")));
    assertTrue(loggerFilter.test("abc.ij".split("\\.")));
  }

}
