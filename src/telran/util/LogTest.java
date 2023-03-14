package telran.util;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

public class LogTest {
	

	@Test
	void consoleTest() {
		SimpleStreamHalder handler = new SimpleStreamHalder (new PrintStream(System.out));
		Logger logger = new Logger (handler, "loger",Level.INFO);
		logger.trace("level TRACE message is:");
		logger.debug("level DEBUG message is:");
		logger.info("level INFO message is:");
		logger.warn("level WARN message is:");
		logger.error("level ERROR message is:");
	}
	
}
