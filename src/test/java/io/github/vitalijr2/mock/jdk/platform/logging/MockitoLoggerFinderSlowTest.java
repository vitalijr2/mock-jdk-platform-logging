package io.github.vitalijr2.mock.jdk.platform.logging;

import static org.mockito.Mockito.verify;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Tag("slow")
class MockitoLoggerFinderSlowTest {

  @Mock
  private Logger logger;

  @BeforeEach
  void setUp() {
    logger = System.getLogger("test");
  }

  @DisplayName("Test")
  @Test
  void test() {
    // when
    System.getLogger("test").log(Level.INFO, "test message");

    // then
    verify(logger).log(Level.INFO, "test message");
  }

}