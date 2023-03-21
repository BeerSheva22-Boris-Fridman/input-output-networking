package telran.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.*;


class TcpClientHandlerTest {

		@Test
		void tcpTest() throws IOException {
		Handler networkSender = new TcpLoggerClientHandler();
		Logger logger = new Logger(networkSender, "TcpLogger");
		logger.setLevel(Level.TRACE);
		
		logger.trace("level TRACE message is:");
		logger.debug("level DEBUG message is:");
		logger.trace("level TRACE message is:");
		logger.trace("level TRACE message is:");
		logger.info("level INFO message is:");
		logger.warn("level WARN message is:");
		logger.warn("level WARN message is:");
		logger.error("level ERROR message is:");
		
		PrintStream writer = TcpLoggerClientHandler.getWriter();
		BufferedReader reader = TcpLoggerClientHandler.getReader();
		
		writer.println("counter#trace");
		String res = reader.readLine();
		assertEquals(res, "3");
		
		writer.println("counter#warn");
		String res1 = reader.readLine();
		assertEquals(res1, "2");
	}
}
