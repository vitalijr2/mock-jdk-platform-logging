package example.hello;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class HelloServiceFullTest {

  private static Logger logger;

  @BeforeAll
  static void setUpClass() {
    logger = System.getLogger("HelloService");
  }

  @BeforeEach
  void setUp() throws Exception {
    clearInvocations(logger);
    reset(logger);
  }

  @DisplayName("Names")
  @ParameterizedTest(name = "<{0}>")
  @ValueSource(strings = {"John", "Jane"})
  void names(String name) {
    var helloService = new HelloService();

    when(logger.isLoggable(Level.INFO)).thenReturn(true);

    assertDoesNotThrow(() -> helloService.sayHello(name));

    var logger = System.getLogger("HelloService");

    verify(logger).isLoggable(Level.INFO);
    verify(logger).log(Level.INFO, "Hello " + name + "!");
    verifyNoMoreInteractions(logger);
  }

  @DisplayName("Null or empty name")
  @ParameterizedTest(name = "<{0}>")
  @NullAndEmptySource
  @ValueSource(strings = "   ")
  void nullOrEmptyName(String name) {
    var helloService = new HelloService();

    var exception = assertThrows(RuntimeException.class, () -> helloService.sayHello(name));

    verifyNoInteractions(System.getLogger("HelloService"));

    assertThat(exception.getMessage(), startsWith("Name is"));
  }


}
