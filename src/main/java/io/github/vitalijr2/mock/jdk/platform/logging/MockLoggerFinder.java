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

import static org.mockito.Mockito.mock;

import java.lang.System.Logger;
import java.lang.System.LoggerFinder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.VisibleForTesting;

/**
 * Uses {@link org.mockito.Mockito#mock(Class, String)} to get a mock that is adapted for {@link Logger}.
 *
 * @since 1.0.0
 */
public class MockLoggerFinder extends LoggerFinder {

  private final Map<String, Logger> loggers;

  /**
   * Create a map-based logger finder. The finder uses a concurrent map: a logger name is a key.
   */
  public MockLoggerFinder() {
    this(new ConcurrentHashMap<>());
  }

  @VisibleForTesting
  MockLoggerFinder(Map<String, Logger> loggers) {
    this.loggers = loggers;
  }

  /**
   * Returns an instance of Logger for the given name, module is ignored.
   *
   * @param name   logging name
   * @param module logging module
   * @return mock logger
   */
  @Override
  public Logger getLogger(String name, Module module) {
    return loggers.computeIfAbsent(name, key -> mock(Logger.class, "Mock for logger " + key));
  }

  /**
   * Returns mock loggers for {@link MockLoggerExtension}.
   *
   * @return mock loggers
   */
  Map<String, Logger> getLoggers() {
    return loggers;
  }

}
