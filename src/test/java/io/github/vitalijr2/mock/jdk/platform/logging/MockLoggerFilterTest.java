package io.github.vitalijr2.mock.jdk.platform.logging;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("fast")
class MockLoggerFilterTest {

  @DisplayName("Default acceptance filter: empty string")
  @ParameterizedTest
  @ValueSource(strings = {"qwerty", "abc.xyz"})
  void acceptanceFilter(String loggerName) {
    // given
    var defaultFilter = new MockLoggerFilter("");

    // when and then
    assertTrue(defaultFilter.test(loggerName.split("\\.")));
  }

  @DisplayName("Default rejection filter: null string")
  @ParameterizedTest
  @ValueSource(strings = {"qwerty", "abc.xyz"})
  void rejectionFilter(String loggerName) {
    // given
    var defaultFilter = new MockLoggerFilter(null);

    // when and then
    assertFalse(defaultFilter.test(loggerName.split("\\.")));
  }

  @DisplayName("Accept logger names which start with similar prefix")
  @ParameterizedTest
  @ValueSource(strings = {"qwerty.abc", "qwerty.abc.xyz"})
  void acceptWithSimilarPrefix(String loggerName) {
    // given
    var qwertyAbcFilter = new MockLoggerFilter("qwerty.abc");

    // when and then
    assertTrue(qwertyAbcFilter.test(loggerName.split("\\.")));
  }

  @DisplayName("Prefix is trimmed")
  @ParameterizedTest
  @ValueSource(strings = {"qwerty.abc", "qwerty.abc.xyz"})
  void prefixIsTrimmed(String loggerName) {
    // given
    var qwertyAbcFilter = new MockLoggerFilter("  qwerty.abc ");

    // when and then
    assertTrue(qwertyAbcFilter.test(loggerName.split("\\.")));
  }

  @DisplayName("Reject logger names which do not start with similar prefix")
  @ParameterizedTest
  @ValueSource(strings = {"qwerty", "qwerty.abc123", "qwerty.xyz.xyz", "qwerty.ab.xyz",})
  void rejectWithUnrelatedPrefix(String loggerName) {
    // given
    var qwertyAbcFilter = new MockLoggerFilter("qwerty.abc");

    // when and then
    assertFalse(qwertyAbcFilter.test(loggerName.split("\\.")));
  }

}