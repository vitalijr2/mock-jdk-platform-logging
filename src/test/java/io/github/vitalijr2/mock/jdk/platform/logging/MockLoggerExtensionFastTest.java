package io.github.vitalijr2.mock.jdk.platform.logging;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Tag("fast")
class MockLoggerExtensionFastTest {

  @Captor
  private ArgumentCaptor<Supplier<String>> messageCaptor;
  @Mock
  private ExtensionContext extensionContext;
  @Mock
  private org.junit.platform.commons.logging.Logger extensionLogger;

  private MockLoggerExtension extension;
  private Logger firstLogger;
  private Logger secondLogger;

  @BeforeEach
  void setUp() {
    var loggerFinder = new MockLoggerFinder();
    extension = new MockLoggerExtension(loggerFinder, extensionLogger);
    firstLogger = loggerFinder.getLogger("first", getClass().getModule());
    secondLogger = loggerFinder.getLogger("second", getClass().getModule());
  }

  @DisplayName("Clean and reset loggers after each test")
  @Test
  void resetLoggersAfterEachTest() {
    // given
    when(firstLogger.isLoggable(Level.INFO)).thenReturn(true);
    when(firstLogger.isLoggable(Level.INFO)).thenReturn(false);

    // when
    firstLogger.log(Level.INFO, "test message");
    secondLogger.log(Level.INFO, "another test message");

    assertDoesNotThrow(() -> extension.afterEach(extensionContext));

    // then
    verifyNoInteractions(extensionContext);
    verifyNoInteractions(firstLogger);
    verifyNoInteractions(secondLogger);
    verify(extensionLogger).debug(messageCaptor.capture());

    assertAll("All clean and reset",
        () -> assertEquals("Clean and reset the loggers: first, second", messageCaptor.getValue().get(),
            "logging message"), () -> assertFalse(firstLogger.isLoggable(Level.INFO)),
        () -> assertFalse(secondLogger.isLoggable(Level.INFO)));
    ;
  }

  @DisplayName("Clean and reset loggers before each test")
  @Test
  void resetLoggersBeforeEachTest() {
    // given
    when(firstLogger.isLoggable(Level.INFO)).thenReturn(true);
    when(firstLogger.isLoggable(Level.INFO)).thenReturn(false);

    // when
    firstLogger.log(Level.INFO, "test message");
    secondLogger.log(Level.INFO, "another test message");

    assertDoesNotThrow(() -> extension.beforeEach(extensionContext));

    // then
    verifyNoInteractions(extensionContext);
    verifyNoInteractions(firstLogger);
    verifyNoInteractions(secondLogger);
    verify(extensionLogger).debug(messageCaptor.capture());

    assertAll("All clean and reset",
        () -> assertEquals("Clean and reset the loggers: first, second", messageCaptor.getValue().get(),
            "logging message"), () -> assertFalse(firstLogger.isLoggable(Level.INFO)),
        () -> assertFalse(secondLogger.isLoggable(Level.INFO)));
  }

}
