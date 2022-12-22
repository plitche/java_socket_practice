package Socket;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketThreadServer extends Thread {

    private Socket socket;

    public SocketThreadServer(Socket socket){
        this.socket=socket;
    }

    //단순 문자열 Thread server
    public void run(){
        BufferedReader br = null;
        PrintWriter pw = null;
        try{
            String connIp = socket.getInetAddress().getHostAddress();
            System.out.println(connIp + "에서 연결 시도.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream());

            // 클라이언트에서 보낸 문자열 출력
            System.out.println(br.readLine());

            // 클라이언트에 문자열 전송
            pw.println("수신되었다. 오버");
            pw.flush();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(pw != null) { pw.close();}
                if(br != null) { br.close();}
                if(socket != null){socket.close();}
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}