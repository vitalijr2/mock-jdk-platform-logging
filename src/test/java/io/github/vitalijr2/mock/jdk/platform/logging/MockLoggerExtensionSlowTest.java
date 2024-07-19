package io.github.vitalijr2.mock.jdk.platform.logging;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.lang.System.LoggerFinder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Tag("slow")
class MockLoggerExtensionSlowTest {

  @DisplayName("Unknown logger finder")
  @Test
  void unknownLoggerFinder() {
    try (var loggerFinder = mockStatic(LoggerFinder.class)) {
      // given
      loggerFinder.when(LoggerFinder::getLoggerFinder).thenReturn(mock(LoggerFinder.class));

      // when
      var exception = assertThrows(ExtensionConfigurationException.class,
          MockLoggerExtension::getMockLoggerFinder);

      // then
      assertEquals("The logger finder is not a MockLoggerFinder", exception.getMessage());
    }
  }

  @DisplayName("Initialize a logger finder on \"before all\" step")
  @Test
  void initLoggerFinderOnBeforeAll() {
    // when and then
    assertDoesNotThrow(() -> new MockLoggerExtension());
  }

}
