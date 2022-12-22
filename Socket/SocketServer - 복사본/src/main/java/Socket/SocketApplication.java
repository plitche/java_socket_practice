package Socket;

import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootApplication
public class SocketApplication {

	private static final int PORT_NUMBER = 9092;

	public static void main(String[] args) throws IOException {
		SpringApplication.run(SocketApplication.class, args);

//		ServerSocket serverSocket = new ServerSocket(9092);
//		Socket s = serverSocket.accept();
//
//		InputStreamReader in = new InputStreamReader(s.getInputStream());
//		BufferedReader bf = new BufferedReader(in);
//
//		String str = bf.readLine();
////		int str2 = bf.read();
//		System.out.println("str = " + str);
////		System.out.println("str2 = " + str2);
////		serverSocket.close();

		System.out.println(":::                                                :::");
		System.out.println(":::       Socket Application  Process Start        :::");
		System.out.println(":::                                                :::");

		try(ServerSocket server = new ServerSocket(PORT_NUMBER)){
			while(true){
				Socket connection = server.accept();
				Thread task = new SocketThreadServer(connection);
				task.start();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
