package Socket;

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

	public static void main(String[] args) throws IOException {
		SpringApplication.run(SocketApplication.class, args);

		ServerSocket serverSocket = new ServerSocket(9092);
		Socket s = serverSocket.accept();

		InputStreamReader in = new InputStreamReader(s.getInputStream());
		BufferedReader bf = new BufferedReader(in);

		String str = bf.readLine();
		System.out.println("str = " + str);
//		serverSocket.close();
	}

}
