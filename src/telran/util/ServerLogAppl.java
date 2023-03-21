package telran.util;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class ServerLogAppl {
	private static final int PORT = 4001;
	static	int traceCount = 0;
	static		int warnCount = 0;
	static		int infoCount = 0;
	static		int debugCount = 0;
	static		int errorCount = 0;
	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("server listening on port " + PORT);
		while (true) {
			Socket socket = serverSocket.accept();
			try {
				runServerClient(socket);
			} catch (IOException e) {
				System.out.println("abnormal closing connection");
			}
		}

	}

	private static void runServerClient(Socket socket) throws IOException {
		Scanner s = new Scanner(socket.getInputStream());
		PrintStream writer = new PrintStream(socket.getOutputStream());
		while (s.hasNext()) {

			String request = s.nextLine();
			String response = getResponse(request);
			writer.println(response);

		}
		System.out.println("client closed connection");
	}

	private static String getResponse(String request) {
		String[] tokens = request.split("#");
	//	HashMap<String, Integer> levelCounter = new HashMap();

		if (tokens.length != 2) {
			return "Wrong Request";

		} else if (tokens[0].equals("log")) {
			if (tokens[1].contains("TRACE")) {
				traceCount++;
						}
			if (tokens[1].contains("WARN")) {
				warnCount++;
			}
			if (tokens[1].contains("INFO")) {
				infoCount++;
			}
			if (tokens[1].contains("ERROR")) {
				errorCount++;
			}
			if (tokens[1].contains("DEBUG")) {
				debugCount++;
			}
			System.out.println(tokens[1]);
			return "OK";
		} else if (tokens[0].equals("counter")) {
			if (tokens[1].equals("error")) {
				
				return Integer.toString(errorCount);
			}
			if (tokens[1].equals("warn")) {
				return Integer.toString(warnCount);
			}
			if (tokens[1].equals("info")) {
				return Integer.toString(infoCount);
			}
			if (tokens[1].equals("debug")) {
				return Integer.toString(debugCount);
			}
			if (tokens[1].equals("trace")) {
				
				return Integer.toString(traceCount);
			} else {
				return "Wrong type " + tokens[0];
			}

		} else {
			return "Wrong type of command";

		}
	}
}
