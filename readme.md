# Mock of JDK Platform Logging

JDK Platform Logging Service with mocked loggers backed by [Mockito][].

[![Codacy Badge][codacy-badge]][codacy-badge-link]
[![Codacy Coverage][codacy-coverage]][codacy-coverage-link]  
[![Libraries.io dependency status for GitHub repo][dependency-status]][dependencies]
[![Java Version][java-version]][jdk-download]  
[![Maven Central](https://img.shields.io/maven-central/v/io.github.vitalijr2.logging/mock-jdk-platform-logging)](https://search.maven.org/artifact/io.github.vitalijr2.logging/mock-jdk-platform-logging)
[![Javadoc](https://javadoc.io/badge2/io.github.vitalijr2.logging/mock-jdk-platform-logging/javadoc.svg)](https://javadoc.io/doc/io.github.vitalijr2.logging/mock-jdk-platform-logging)

## How to use

Just put a test dependency to your POM:
```xml
<dependency>
    <artifactId>mock-jdk-platform-logging</artifactId>
    <groupId>io.github.vitalijr2.logging</groupId>
    <scope>test</scope>
    <version>1.0.0</version>
</dependency>
```

The most basic usage example looks like this:
```java
@Test
void helloWorld() {
    var helloService = new HelloService();

    assertDoesNotThrow(helloService::sayHelloWorld);

    verify(System.getLogger("HelloService")).log(System.Logger.Level.INFO, "Hello World!");
}
```
See more details at [HelloServiceBasicTest.java](src/it/hello-world/src/test/java/example/hello/HelloServiceBasicTest.java)

It should be taken into account that all loggers are initialized only once during the run of tests.
Therefore, a more complex example cleans the loggers before (or after) each test:
```java
// the static logger instance
private static Logger logger;

// initialize the mock logger once
@BeforeAll
static void setUpClass() {
    logger = System.getLogger("HelloService");
}

// clean the mock logger after each test
@AfterEach
void tearDown() {
    clearInvocations(logger);
}

// use the mock logger in a test
@DisplayName("Names")
@ParameterizedTest(name = "<{0}>")
@ValueSource(strings = {"John", "Jane"})
void names(String name) {
    var helloService = new HelloService();

    assertDoesNotThrow(() -> helloService.sayHello(name));

    var logger = System.getLogger("HelloService");

    verify(logger).log(System.Logger.Level.INFO, "Hello " + name + "!");
    verifyNoMoreInteractions(logger);
}
```
See more details at [HelloServiceFullTest.java](src/it/hello-world/src/test/java/example/hello/HelloServiceFullTest.java)

Since the version **1.1.0** you can use the jUnit extension for automation.
```java
@ExtendWith(MockLoggerExtension.class)
class HelloServiceExtensionTest {

    private static Logger logger;

    @BeforeAll
    static void setUpClass() {
        logger = System.getLogger("HelloService");
    }

    @DisplayName("Names")
    @ParameterizedTest(name = "<{0}>")
    @ValueSource(strings = {"John", "Jane"})
    void names(String name) {
        var helloService = new HelloService();

        assertDoesNotThrow(() -> helloService.sayHello(name));

        var logger = System.getLogger("HelloService");

        verify(logger).log(System.Logger.Level.INFO, "Hello " + name + "!");
        verifyNoMoreInteractions(logger);
    }

}
```
See more details at [HelloServiceExtensionTest.java](src/it/hello-world/src/test/java/example/hello/HelloServiceExtensionTest.java)

## Credits

There are two projects which inspired me to make this library:

- [s4u/slf4j-mock][slf4j-mock]
- [ocarlsen/mock-slf4j-impl][mock-slf4j-impl]

## Contributing

Please read [Contributing](contributing.md).

## History

See [Changelog](changelog.md)

## License

Copyright 2024 Vitalij Berdinskih

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
