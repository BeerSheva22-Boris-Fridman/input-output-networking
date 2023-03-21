package telran.util;

import java.time.Instant;
import java.time.ZoneId;

public class Logger  {
private Handler handler;
private String name;
private Level level;

public Logger(Handler handler, String name) {
	
	this.handler = handler;
	this.name = name;
	this.level = level;
	}

public void setLevel(Level level) {
	this.level = level;
}

public LoggerRecord loggerRecord(String message, Level level) {
	return new LoggerRecord(Instant.now(), ZoneId.systemDefault().toString(), level, name, message);
}

public void trace(String message) {
	if (level.compareTo(level.TRACE) <= 0) {
		handler.publish(loggerRecord(message, Level.TRACE));
	}
}
	
public void debug(String message) {
	if (level.compareTo(level.DEBUG) <= 0) {
		handler.publish(loggerRecord(message, Level.DEBUG));
	}	
}

public void info(String message) {
	if (level.compareTo(level.INFO) <= 0) {
		handler.publish(loggerRecord(message, Level.INFO));
	}	
}	

public void warn(String message) {
	if (level.compareTo(level.WARN) <= 0) {
		handler.publish(loggerRecord(message, Level.WARN));
	}	
}

public void error(String message) {
	if (level.compareTo(level.ERROR) <= 0) {
		handler.publish(loggerRecord(message, Level.ERROR));
	}	
}
}
