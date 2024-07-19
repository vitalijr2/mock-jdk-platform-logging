package example.hello;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import io.github.vitalijr2.mock.jdk.platform.logging.MockLoggerExtension;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@ExtendWith(MockLoggerExtension.class)
@Tag("fast")
class HelloServiceExtensionTest {

  private static Logger logger;

  @BeforeAll
  static void setUpClass() {
    logger = System.getLogger("HelloService");
  }

  @BeforeEach
  void setUp() {
    when(logger.isLoggable(Level.INFO)).thenReturn(true);
  }

  @DisplayName("Names")
  @ParameterizedTest(name = "<{0}>")
  @ValueSource(strings = {"John", "Jane"})
  void names(String name) {
    var helloService = new HelloService();

    assertDoesNotThrow(() -> helloService.sayHello(name));

    var logger = System.getLogger("HelloService");

    verify(logger).isLoggable(Level.INFO);
    verify(logger).log(System.Logger.Level.INFO, "Hello " + name + "!");
    verifyNoMoreInteractions(logger);
  }

}
