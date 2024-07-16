# Mock of JDK Platform Logging

JDK Platform Logging Service with mocked loggers backed by [Mockito][].

[![Codacy Badge][codacy-badge]][codacy-badge-link]
[![Codacy Coverage][codacy-coverage]][codacy-coverage-link]  
[![Libraries.io dependency status for GitHub repo][dependency-status]][dependencies]
[![Java Version][java-version]][jdk-download]

## How to use

Just put to your POM:
```xml
  <dependencies>
    ...
    <dependency>
      <artifactId>mock-jdk-platform-logging</artifactId>
      <groupId>io.github.vitalijr2.logging</groupId>
      <scope>test</scope>
      <version>1.0.0</version>
    </dependency>
    ...
  </dependencies>

```

Example:
```java
  // the static logger instance
  private static Logger logger;

  // initialize it once
  @BeforeAll
  static void setUpClass() {
    logger = System.getLogger("test");
  }

  // clean it after each test
  @AfterEach
  void tearDown() {
    clearInvocations(logger);
  }

  // use the mock in a test
  @DisplayName("Test")
  @ParameterizedTest
  @ValueSource(strings = {"TRACE", "DEBUG", "INFO", "WARNING", "ERROR"})
  void test(Level level) {
    // given
    var logger = System.getLogger("test")

    // when
    logger.log(level, "test message");

    // then
    verify(logger).log(level, "test message");
  }
```

## Credits

There are two projects which inspired me to make this library:

- [s4u/slf4j-mock][slf4j-mock]
- [ocarlsen/mock-slf4j-impl][mock-slf4j-impl]

## Contributing

Please read [Contributing](contributing.md).

## History

See [Changelog](changelog.md)

## License

Copyright 2023-2024 Vitalij Berdinskih

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<https://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

[Apache License v2.0](LICENSE)  
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0.html)

[Mockito]: https://site.mockito.org

[codacy-badge]: https://app.codacy.com/project/badge/Grade/9be380deaf3e40138ad306a40532289c

[codacy-badge-link]: https://app.codacy.com/gh/vitalijr2/mock-jdk-platform-logging/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade

[codacy-coverage]: https://app.codacy.com/project/badge/Coverage/9be380deaf3e40138ad306a40532289c

[codacy-coverage-link]: https://app.codacy.com/gh/vitalijr2/mock-jdk-platform-logging/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage

[dependency-status]: https://img.shields.io/librariesio/github/vitalijr2/mock-jdk-platform-logging

[dependencies]: https://libraries.io/github/vitalijr2/mock-jdk-platform-logging

[java-version]: https://img.shields.io/static/v1?label=java&message=11&color=blue&logo=java&logoColor=E23D28

[jdk-download]: https://www.oracle.com/java/technologies/downloads/#java11

[slf4j-mock]: https://github.com/s4u/slf4j-mock

[mock-slf4j-impl]: https://github.com/ocarlsen/mock-slf4j-impl