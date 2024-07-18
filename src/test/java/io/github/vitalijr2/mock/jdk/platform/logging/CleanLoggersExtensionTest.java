package io.github.vitalijr2.mock.jdk.platform.logging;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verifyNoInteractions;

import java.lang.System.Logger.Level;
import java.lang.System.LoggerFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Tag("slow")
class CleanLoggersExtensionTest {

  @Mock
  private ExtensionContext extensionContext;

  private CleanLoggersExtension extension;

  @BeforeEach
  void setUp() {
    extension = new CleanLoggersExtension();
  }

  @DisplayName("Unknown logger finder")
  @Test
  void unknownLoggerFinder() {
    try (var loggerFinder = mockStatic(LoggerFinder.class)) {
      // given
      loggerFinder.when(LoggerFinder::getLoggerFinder).thenReturn(mock(LoggerFinder.class));

      // when
      var exception = assertThrows(ExtensionConfigurationException.class,
          CleanLoggersExtension::getMockitoLoggerFinder);

      // then
      assertEquals("The logger finder is not a MockitoLoggerFinder", exception.getMessage());
    }
  }

  @DisplayName("Initialize a logger finder on \"before all\" step")
  @Test
  void initLoggerFinderOnBeforeAll() {
    // when
    assertDoesNotThrow(() -> extension.beforeAll(extensionContext));

    // then
    verifyNoInteractions(extensionContext);
  }

  @DisplayName("Clean and reset loggers after each test")
  @Test
  void resetLoggersAfterEachTest() {
    // given
    extension.beforeAll(extensionContext);
    var firstLogger = System.getLogger("first");
    var secondLogger = System.getLogger("second");
    firstLogger.log(Level.INFO, "test message");
    secondLogger.log(Level.INFO, "another test message");

    // when
    assertDoesNotThrow(() -> extension.afterEach(extensionContext));

    // then
    verifyNoInteractions(firstLogger);
    verifyNoInteractions(secondLogger);
  }

  @DisplayName("Clean and reset loggers before each test")
  @Test
  void resetLoggersBeforeEachTest() {
    // given
    extension.beforeAll(extensionContext);
    var firstLogger = System.getLogger("first");
    var secondLogger = System.getLogger("second");
    firstLogger.log(Level.INFO, "test message");
    secondLogger.log(Level.INFO, "another test message");

    // when
    assertDoesNotThrow(() -> extension.beforeEach(extensionContext));

    // then
    verifyNoInteractions(firstLogger);
    verifyNoInteractions(secondLogger);
  }

}
