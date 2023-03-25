package telran.net.application;
import java.io.*;
import java.net.*;

import telran.net.TcpServer;
import telran.net.UdpServer;
public class ServerTcpExampleAppl {

	private static final int PORT = 4000;

	public static void main(String[] args) throws Exception{
		UdpServer server = new UdpServer(PORT, new ExampleProtocol());
		server.run();
}
}
