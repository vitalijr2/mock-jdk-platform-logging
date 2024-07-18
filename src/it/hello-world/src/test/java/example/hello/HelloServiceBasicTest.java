package example.hello;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HelloServiceBasicTest {

  @DisplayName("Hello world")
  @Test
  void helloWorld() {
    var helloService = new HelloService();

    assertDoesNotThrow(helloService::sayHelloWorld);

    verify(System.getLogger("HelloService")).log(System.Logger.Level.INFO, "Hello World!");
  }

}
