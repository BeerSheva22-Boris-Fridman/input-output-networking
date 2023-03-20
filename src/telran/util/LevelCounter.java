package telran.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class LevelCounter {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 4001;
	PrintStream writer;
	BufferedReader reader;
	Socket socket;
	
		public LevelCounter() throws UnknownHostException, IOException {
		super();
		this.socket = new Socket(HOSTNAME, PORT);
		this.writer = new PrintStream(socket.getOutputStream());
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));;
		
	}
		
		
		
		

	
 public String Counter (String command) throws IOException {
	 writer.println(command);
	 String response = reader.readLine();
	
	return response;
}
	private static void runClient(Socket socket, PrintStream writer, BufferedReader reader) throws Exception {
		Scanner scanner = new Scanner(System.in);

		System.out.println("enter request: <type>#<string> or exit");
		String line = scanner.nextLine();

		writer.println(line);
		String response = reader.readLine();
		System.out.println(response);
		socket.close();
	}

}
