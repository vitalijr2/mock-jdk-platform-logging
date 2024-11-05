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

import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implements a simple match filter.
 * <p>
 * If a logger name starts with a prefix, that registrar is accepted. The logger name is interpreted as the full Java
 * class name, i.e. separated by dots.
 * <p>
 * An empty string is the default acceptance filter. A zero string is the default rejection filter.
 */
class MockLoggerFilter implements Predicate<String[]> {

  private final String[] prefix;

  MockLoggerFilter(@Nullable String prefix) {
    if (prefix == null) {
      this.prefix = new String[0];
    } else {
      this.prefix = prefix.split("\\.");
    }
  }

  @Override
  public boolean test(@NotNull String[] loggerName) {
    if (prefix.length == 0 || loggerName.length < prefix.length) {
      return false;
    }
    if (prefix.length == 1 && prefix[0].isEmpty()) {
      return true;
    }

    for (int i = 0; i < prefix.length; i++) {
      if (!loggerName[i].equals(prefix[i])) {
        return false;
      }
    }

    return true;
  }

}
