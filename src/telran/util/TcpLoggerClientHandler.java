package telran.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class TcpLoggerClientHandler implements Handler {
	private Socket socket;
	static BufferedReader reader;
	static PrintStream writer;
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 4001;

	public TcpLoggerClientHandler() throws IOException {
		this.socket = new Socket(HOSTNAME, PORT);
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintStream(socket.getOutputStream());
	}

	@Override
	public void publish(LoggerRecord loggerRecord) {
		writer.println("log#" + loggerRecord.message + " " + loggerRecord.zoneId + " " + loggerRecord.level + " "
				+ loggerRecord.timestamp + " " + loggerRecord.loggerName);
		try {
			String response = reader.readLine();
			System.out.println(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static public BufferedReader getReader() {
		return reader;
	}

	static public PrintStream getWriter() {
		return writer;
	}

	@Override
	public void close() throws SecurityException {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
