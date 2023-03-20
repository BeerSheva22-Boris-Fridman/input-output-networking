package telran.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


public class TcpLoggerClientHandler implements Handler {
        private Socket socket;
        BufferedReader reader;
        PrintStream writer;


        public TcpLoggerClientHandler(String host, int port) throws IOException {
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintStream(socket.getOutputStream());
        }

        @Override
        public void publish(LoggerRecord loggerRecord) {
            writer.println("log#" + loggerRecord.message + " " + loggerRecord.zoneId + 
            		" " + loggerRecord.level + " " + loggerRecord.timestamp+ " " + loggerRecord.loggerName);
            try {
                String response = reader.readLine();
                System.out.println(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

