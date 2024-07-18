/*
 * Copyright 2024 Vitalij Berdinskih
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	   http://www.apache.org/licenses/LICENSE-2.0
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
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

public class CleanLoggersExtension implements AfterEachCallback, BeforeAllCallback, BeforeEachCallback {

  private final Logger logger = LoggerFactory.getLogger(CleanLoggersExtension.class);
  private MockLoggerFinder loggerFinder;

  @VisibleForTesting
  static MockLoggerFinder getMockLoggerFinder() {
    var loggerFinder = LoggerFinder.getLoggerFinder();
    if (loggerFinder instanceof MockLoggerFinder) {
      return (MockLoggerFinder) loggerFinder;
    } else {
      throw new ExtensionConfigurationException("The logger finder is not a MockLoggerFinder");
    }
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    cleanAndResetLoggers();
  }

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    loggerFinder = getMockLoggerFinder();
    logger.trace(() -> "Initialize the logger finder");
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    cleanAndResetLoggers();
  }

  private void cleanAndResetLoggers() {
    loggerFinder.getLoggers().forEach((loggerName, logger) -> {
      clearInvocations(logger);
      reset(logger);
    });
    logger.debug(() -> "Clean and reset the loggers: " + String.join(", ", loggerFinder.getLoggers().keySet()));
  }

}
