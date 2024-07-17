package example.hello;

import static java.util.Objects.requireNonNull;

import java.lang.System.Logger;

public class HelloService {

  private final Logger logger = System.getLogger("HelloService");

  public String sayHelloWorld() {
    return sayHello("World");
  }

  public String sayHello(String name) {
    if (requireNonNull(name, "Name is missed").isBlank()) {
      throw new IllegalArgumentException("Name is empty");
    }

    var greeting = "Hello " + name + "!";

    logger.log(System.Logger.Level.INFO, greeting);

    return greeting;
  }

}
