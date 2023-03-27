package Socket;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootApplication
@RequiredArgsConstructor
public class SocketApplication {

	private static final int PORT_NUMBER = 9092;
	static SocketRepository socketRepository = null;

	public static void main(String[] args) throws IOException {
		SpringApplication.run(SocketApplication.class, args);
		socketRepository = new SocketRepository();

		System.out.println(":::                                                :::");
		System.out.println(":::       Socket Application  Process Start        :::");
		System.out.println(":::                                                :::");

		try(ServerSocket server = new ServerSocket(PORT_NUMBER)){
			while(true){
				Socket connection = server.accept();
				Thread task = new SocketThreadServer(connection, socketRepository);
				task.start();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
