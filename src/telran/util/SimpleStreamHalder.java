package telran.util;

import java.io.PrintStream;

public class SimpleStreamHalder implements Handler {
private PrintStream stream;

	public SimpleStreamHalder(PrintStream stream) {
		super();
		this.stream = stream;
	}

	@Override
	public void  publish(LoggerRecord loggerRecord) {
		stream.println();
		stream.println(loggerRecord.message);
		stream.println(loggerRecord.timestamp);
		stream.println(loggerRecord.zoneId);
		stream.println(loggerRecord.level);
		stream.println(loggerRecord.loggerName);
		stream.println();
	}





}
