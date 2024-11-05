package io.github.vitalijr2.mock.jdk.platform.logging;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("fast")
class MockLoggerFilterTest {

  @DisplayName("Default filter: empty string")
  @ParameterizedTest
  @ValueSource(strings = {"qwerty", "abc.xyz"})
  void defaultFilter(String loggerName) {
    // given
    var defaultFilter = new MockLoggerFilter("");

    // when and then
    assertTrue(defaultFilter.test(loggerName.split("\\.")));
  }

  @DisplayName("Accept logger names which start with similar prefix")
  @ParameterizedTest
  @ValueSource(strings = {"qwerty.abc", "qwerty.abc.xyz"})
  void accept(String loggerName) {
    // given
    var qwertyAbcFilter = new MockLoggerFilter("qwerty.abc");

    // when and then
    assertTrue(qwertyAbcFilter.test(loggerName.split("\\.")));
  }

  @DisplayName("Reject logger names which do not start with similar prefix")
  @ParameterizedTest
  @ValueSource(strings = {"qwerty", "qwerty.abc123", "qwerty.xyz.xyz", "qwerty.ab.xyz",})
  void reject(String loggerName) {
    // given
    var qwertyAbcFilter = new MockLoggerFilter("qwerty.abc");

    // when and then
    assertFalse(qwertyAbcFilter.test(loggerName.split("\\.")));
  }

}