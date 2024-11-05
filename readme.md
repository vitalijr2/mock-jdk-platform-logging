# Mock of JDK Platform Logging

JDK Platform Logging Service with mock loggers backed by [Mockito][].

[![Java Version][java-version]][jdk-download]
![jUnit Version][junit-version]
![Mockito Version][mockito-version]
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0.html)  
![Maven Central Last Update][maven-central-last-update]
[![Maven Central][maven-central]][maven-central-link]
[![Javadoc][javadoc]][javadoc-link]  
[![GitHub master check runs][github-master-check-runs]][github-master-check-runs-link]
[![Codacy Badge][codacy-badge]][codacy-badge-link]
[![Codacy Coverage][codacy-coverage]][codacy-coverage-link]
![GitHub commit activity][github-commit-activity]
[![Today's hits][today-hits]][today-hits-link]

## How to use

Just put a test dependency to your POM:
```xml
<dependency>
    <artifactId>mock-jdk-platform-logging</artifactId>
    <groupId>io.github.vitalijr2.logging</groupId>
    <scope>test</scope>
    <version>1.1.2</version>
</dependency>
```

The most basic usage example looks like this:
```java
@Test
void helloWorld() {
    var helloService = new HelloService();

    assertDoesNotThrow(helloService::sayHelloWorld);

    verify(System.getLogger("HelloService")).log(Level.INFO, "Hello World!");
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

    verify(logger).log(Level.INFO, "Hello " + name + "!");
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

        verify(logger).log(Level.INFO, "Hello " + name + "!");
        verifyNoMoreInteractions(logger);
    }

}
```
See more details at [HelloServiceExtensionTest.java](src/it/hello-world/src/test/java/example/hello/HelloServiceExtensionTest.java)

Since the version **1.2.0** you can use the annotation for automation.
```java
@MockLoggers
class HelloServiceAnnotationTest {

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

        verify(logger).log(Level.INFO, "Hello " + name + "!");
        verifyNoMoreInteractions(logger);
    }

}
```
See more details at [HelloServiceAnnotationTest.java](src/it/hello-world/src/test/java/example/hello/HelloServiceAnnotationTest.java)

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

[Mockito]: https://site.mockito.org

[github-master-check-runs]: https://img.shields.io/github/check-runs/vitalijr2/mock-jdk-platform-logging/master

[github-master-check-runs-link]: https://github.com/vitalijr2/mock-jdk-platform-logging/actions?query=branch%3Amaster

[maven-central-last-update]: https://img.shields.io/maven-central/last-update/io.github.vitalijr2.logging/mock-jdk-platform-logging

[maven-central]: https://img.shields.io/maven-central/v/io.github.vitalijr2.logging/mock-jdk-platform-logging

[maven-central-link]: https://central.sonatype.com/artifact/io.github.vitalijr2.logging/mock-jdk-platform-logging?smo=true

[javadoc]: https://javadoc.io/badge2/io.github.vitalijr2.logging/mock-jdk-platform-logging/javadoc.svg

[javadoc-link]: https://javadoc.io/doc/io.github.vitalijr2.logging/mock-jdk-platform-logging

[codacy-badge]: https://app.codacy.com/project/badge/Grade/9be380deaf3e40138ad306a40532289c

[codacy-badge-link]: https://app.codacy.com/gh/vitalijr2/mock-jdk-platform-logging/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade

[codacy-coverage]: https://app.codacy.com/project/badge/Coverage/9be380deaf3e40138ad306a40532289c

[codacy-coverage-link]: https://app.codacy.com/gh/vitalijr2/mock-jdk-platform-logging/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage

[today-hits]: https://hits.sh/github.com/vitalijr2/mock-jdk-platform-logging.svg?view=today-total&label=today's%20hits

[today-hits-link]: https://hits.sh/github.com/vitalijr2/mock-jdk-platform-logging/

[github-commit-activity]: https://img.shields.io/github/commit-activity/y/vitalijr2/mock-jdk-platform-logging

[java-version]: https://img.shields.io/static/v1?label=Java&message=11&color=blue&logoColor=E23D28

[jdk-download]: https://www.oracle.com/java/technologies/downloads/#java11

[junit-version]: https://img.shields.io/static/v1?label=jUnit&message=5.11.3&color=blue&logo=junit5&logoColor=E23D28

[mockito-version]: https://img.shields.io/static/v1?label=Mockito&message=5.14.2&color=blue&logoColor=E23D28

[slf4j-mock]: https://github.com/s4u/slf4j-mock

[mock-slf4j-impl]: https://github.com/ocarlsen/mock-slf4j-impl
