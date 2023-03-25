package util;

public interface Handler {
void publish (LoggerRecord loggerRecord);
default void close() {}
}
