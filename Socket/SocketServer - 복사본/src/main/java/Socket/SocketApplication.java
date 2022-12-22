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
