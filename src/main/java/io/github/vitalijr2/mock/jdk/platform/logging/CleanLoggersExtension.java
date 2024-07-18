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
import org.jetbrains.annotations.VisibleForTesting;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

/**
 * jUnit extension to clean and reset mock loggers.
 */
public class CleanLoggersExtension implements AfterEachCallback, BeforeEachCallback {

  private final MockLoggerFinder loggerFinder;
  private final Logger extensionLogger;

  /**
   * Create an extension, initialize the MockLoggerFinder.
   */
  public CleanLoggersExtension() {
    this(getMockLoggerFinder(), LoggerFactory.getLogger(CleanLoggersExtension.class));
  }

  @VisibleForTesting
  CleanLoggersExtension(MockLoggerFinder loggerFinder, Logger extensionLogger) {
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
   * Clean and reset mock loggers after tests.
   *
   * @param context the current extension context; never {@code null}
   */
  @Override
  public void afterEach(ExtensionContext context) {
    cleanAndResetLoggers();
  }

  /**
   * Clean and reset mock loggers before tests.
   *
   * @param context the current extension context; never {@code null}
   */
  @Override
  public void beforeEach(ExtensionContext context) {
    cleanAndResetLoggers();
  }

  private void cleanAndResetLoggers() {
    loggerFinder.getLoggers().forEach((loggerName, logger) -> {
      clearInvocations(logger);
      reset(logger);
    });
    extensionLogger.debug(
        () -> "Clean and reset the loggers: " + String.join(", ", loggerFinder.getLoggers().keySet()));
  }

}
