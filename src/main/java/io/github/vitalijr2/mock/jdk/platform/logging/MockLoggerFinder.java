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

import static java.util.Objects.requireNonNullElse;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import java.lang.System.LoggerFinder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

/**
 * Uses {@link org.mockito.Mockito#mock(Class, String)} to get a mock that is adapted for {@link Logger}.
 * <p>
 * Example:
 * <pre><code class="language-java">
 *   {@literal @}Test
 *   void helloWorld() {
 *     var helloService = new HelloService();
 *
 *     assertDoesNotThrow(helloService::sayHelloWorld);
 *
 *     verify(System.getLogger("HelloService")).log(Level.INFO, "Hello World!");
 *   }
 * </code></pre>
 *
 * @since 1.0.0
 */
public class MockLoggerFinder extends LoggerFinder {

  private final Map<String, Logger> loggers;

  private final Predicate<String[]> loggerFilters;

  /**
   * Create a map-based logger finder. The finder uses a concurrent map: a logger name is a key.
   */
  public MockLoggerFinder() {
    this(new ConcurrentHashMap<>());
  }

  @VisibleForTesting
  MockLoggerFinder(Map<String, Logger> loggers) {
    this.loggers = loggers;

    var configurationProperties = loadProperties("mock-loggers.properties");

    loggerFilters = craftLoggerFilters(configurationProperties.getProperty("excludes"),
        configurationProperties.getProperty("includes"));
  }

  /**
   * Prepares sets of acceptance and rejection filters.
   *
   * @param excludes rejection comma-separated list
   * @param includes acceptance comma-separated list
   * @return filter predicate
   */
  @VisibleForTesting
  static Predicate<String[]> craftLoggerFilters(@Nullable String excludes, @Nullable String includes) {
    Set<MockLoggerFilter> excludeFilters;
    Set<MockLoggerFilter> includeFilters;

    if (excludes == null) {
      // default rejection filter
      excludeFilters = Collections.singleton(new MockLoggerFilter(null));
    } else {
      excludeFilters = Arrays.stream(excludes.split(",")).map(MockLoggerFilter::new).collect(Collectors.toSet());
    }
    if (includes == null) {
      // default acceptance filter
      includeFilters = Collections.singleton(new MockLoggerFilter(""));
    } else {
      includeFilters = Arrays.stream(includes.split(",")).map(MockLoggerFilter::new).collect(Collectors.toSet());
    }

    return splitLoggerName -> excludeFilters.stream().noneMatch(excludeFilter -> excludeFilter.test(splitLoggerName))
        && includeFilters.stream().anyMatch(includeFilter -> includeFilter.test(splitLoggerName));
  }

  @VisibleForTesting
  static InputStream fallbackInputStream() {
    return new ByteArrayInputStream("includes=".getBytes());
  }

  /**
   * Loads configuration properties.
   * <p>
   * If a property file is not found that default acceptance filter is used.
   *
   * @param configurationFile filename
   * @return configuration properties
   */
  @VisibleForTesting
  static @NotNull Properties loadProperties(@NotNull String configurationFile) {
    try (InputStream configurationInputStream = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream(configurationFile)) {
      var properties = new Properties();

      properties.load(requireNonNullElse(configurationInputStream, fallbackInputStream()));

      return properties;
    } catch (IllegalArgumentException | IOException exception) {
      throw new MockLoggerConfigurationException("Could not load configuration properties", exception);
    }
  }

  /**
   * Returns an instance of Logger for the given name, module is ignored.
   *
   * @param name   logging name
   * @param module logging module
   * @return logger, mock or real
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
