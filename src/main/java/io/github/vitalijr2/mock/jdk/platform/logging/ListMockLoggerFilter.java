package io.github.vitalijr2.mock.jdk.platform.logging;

import java.util.List;

/*
 * TODO: Will change to a record when the project JDK is 17.
 */
class ListMockLoggerFilter {

  final List<String> excludes;
  final List<String> includes;

  ListMockLoggerFilter(List<String> excludes, List<String> includes) {
    this.excludes = excludes;
    this.includes = includes;
  }

  boolean accept(String loggerName) {
    return true;
  }
}
