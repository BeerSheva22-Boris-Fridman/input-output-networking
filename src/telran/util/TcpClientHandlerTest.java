package telran.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.PrintStream;


import org.junit.jupiter.api.Test;

import telran.util.TcpLoggerClientHandler;

class TcpClientHandlerTest {
	    private static final String HOSTNAME = "localhost";
	    private static final int PORT = 4001;
	//    static String loggerName = TcpLoggerClientHandler.class.getName();
	   

	    public static void main(String[] args) throws IOException {
	     
	        System.out.println("good");
	    	Handler networkSender = new TcpLoggerClientHandler(HOSTNAME,PORT);
	    	 System.out.println("good1");
	         Logger logger = new Logger(networkSender, "TcpLogger", Level.TRACE);
	         System.out.println("good2");
	    	  
	    	logger.trace("level TRACE message is:");
	    	  System.out.println("good3");
			logger.debug("level DEBUG message is:");
			logger.trace("level TRACE message is:");
			logger.trace("level TRACE message is:");
			logger.info("level INFO message is:");
			logger.warn("level WARN message is:");
			logger.warn("level WARN message is:");
			logger.error("level ERROR message is:");
			networkSender.close();
			  LevelCounter counter = new LevelCounter(); 
			counter.Counter("counter#error");
			  System.out.println("good4");
			counter.Counter("counter#warn");
			counter.Counter("counter#info");
			counter.Counter("counter#debug");
			counter.Counter("counter#trace");
	    }
}
