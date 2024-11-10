package io.github.vitalijr2.mock.jdk.platform.logging;

import static org.mockito.Mockito.mock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

public class MockLoggerFactory extends LogFactory {

  private final Map<String, Log> loggers;

  public MockLoggerFactory() {
    this(new ConcurrentHashMap<>());
  }

  public MockLoggerFactory(Map<String, Log> loggers) {
    this.loggers = loggers;
  }

  @Override
  public Object getAttribute(String name) {
    return null;
  }

  @Override
  public String[] getAttributeNames() {
    return new String[0];
  }

  @Override
  public Log getInstance(Class<?> clazz) throws LogConfigurationException {
    return getInstance(clazz.getName());
  }

  @Override
  public Log getInstance(String name) throws LogConfigurationException {
    return loggers.computeIfAbsent(name, key -> mock(Log.class, "Mock for logger " + key));
  }

  @Override
  public void release() {
    loggers.clear();
  }

  @Override
  public void removeAttribute(String s) {
    ; // do nothing
  }

  @Override
  public void setAttribute(String s, Object o) {
    ; // do nothing
  }

}
