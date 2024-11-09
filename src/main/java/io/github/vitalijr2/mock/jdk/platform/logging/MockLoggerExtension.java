/*
 * Copyright 2024 Vitalij Berdinskih
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.vitalijr2.mock.jdk.platform.logging;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.reset;

import java.lang.System.LoggerFinder;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.VisibleForTesting;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

/**
 * A jUnit extension to clean and reset mock loggers.
 * <p>
 * Clean and reset mock loggers before and after tests.
 * <p>
 * Example:
 * <pre><code class="language-java">
 *   {@literal @}ExtendWith(MockLoggerExtension.class)
 *   class HelloServiceExtensionTest {
 *
 *     private static Logger logger;
 *
 *     {@literal @}BeforeAll
 *     static void setUpClass() {
 *         logger = System.getLogger("HelloService");
 *     }
 *
 *     {@literal @}DisplayName("Names")
 *     {@literal @}ParameterizedTest(name = "&lt;{0}&gt;")
 *     {@literal @}ValueSource(strings = {"John", "Jane"})
 *     void names(String name) {
 *         var helloService = new HelloService();
 *
 *         assertDoesNotThrow(() -&lt; helloService.sayHello(name));
 *
 *         var logger = System.getLogger("HelloService");
 *
 *         verify(logger).log(Level.INFO, "Hello " + name + "!");
 *         verifyNoMoreInteractions(logger);
 *     }
 *
 *   }
 * </code></pre>
 *
 * @since 1.1.0
 */
public class MockLoggerExtension implements AfterEachCallback, BeforeEachCallback {

  private final MockLoggerFinder loggerFinder;
  private final Logger extensionLogger;

  /**
   * Create an extension.
   */
  public MockLoggerExtension() {
    this(getMockLoggerFinder(), LoggerFactory.getLogger(MockLoggerExtension.class));
  }

  @VisibleForTesting
  MockLoggerExtension(MockLoggerFinder loggerFinder, Logger extensionLogger) {
    this.loggerFinder = loggerFinder;
    this.extensionLogger = extensionLogger;
  }

  @VisibleForTesting
  static MockLoggerFinder getMockLoggerFinder() {
    var loggerFinder = LoggerFinder.getLoggerFinder();

    if (loggerFinder instanceof MockLoggerFinder) {
      return (MockLoggerFinder) loggerFinder;
    } else {
      throw new ExtensionConfigurationException("The logger finder is not a MockLoggerFinder");
    }
  }

  /**
   * Clean and reset mock loggers after tests. You are still able to run tasks in
   * {@link org.junit.jupiter.api.AfterEach}.
   *
   * @param context the current extension context; never {@code null}
   */
  @Override
  public void afterEach(ExtensionContext context) {
    var processedLoggers = cleanAndResetLoggers();

    extensionLogger.debug(() -> "Clean and reset the loggers: " + String.join(", ", processedLoggers));
  }

  /**
   * Clean and reset mock loggers before tests. You are still able to run tasks in
   * {@link org.junit.jupiter.api.BeforeEach}.
   *
   * @param context the current extension context; never {@code null}
   */
  @Override
  public void beforeEach(ExtensionContext context) {
    var processedLoggers = cleanAndResetLoggers();

    extensionLogger.debug(() -> "Clean and reset the loggers: " + String.join(", ", processedLoggers));
  }

  private List<String> cleanAndResetLoggers() {
    var processedLoggers = new ArrayList<String>();

    loggerFinder.getLoggers().forEach((loggerName, logger) -> {
      clearInvocations(logger);
      reset(logger);
      processedLoggers.add(loggerName);
    });

    return processedLoggers;
  }

}
