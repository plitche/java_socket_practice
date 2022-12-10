package Socket.socketUtil;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;

class MultichatServer {

    HashMap<String, DataOutputStream> clients;

    // 해쉬맵 생성, 여러개의 스레드가 동시에 접근 가능
    //synchronizedMap(원래는 asynchronized)
    MultichatServer() {
        clients = new HashMap<>();
        Collections.synchronizedMap(clients);
    }

    public void start() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            //7777 port 열어놓기
            serverSocket = new ServerSocket(7777);
            System.out.println("server has started.");
            while (true) {
//                socket = serverSocket.accept();
//                System.out.println("a new connection from [" + socket.getInetAddress() + ":" + socket.getPort() + "]");
//
//                //새로운 Client가 올때마다 스레드를 새로 생성한다.
//                ServerReceiver thread = new ServerReceiver(socket);
//                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}