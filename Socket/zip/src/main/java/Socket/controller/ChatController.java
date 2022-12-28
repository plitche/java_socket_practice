package Socket.controller;

import Socket.service.ChatService;
import ch.qos.logback.classic.net.server.ServerSocketReceiver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.management.ServiceNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    final ChatService chatService;
    private Socket socket;

    @RequestMapping()
    public String goChatPage(HttpServletRequest request,
                             Model model) throws ServiceNotFoundException, IOException {
        HttpSession httpSession = request.getSession();
        String memberId = (String) httpSession.getAttribute("memberId");

        if (memberId == null) {
            model.addAttribute("isLogin", false);
            /*throw new ServiceNotFoundException("Not login Exception - ChatController");*/
        } else {
            int applyCount = chatService.applyChat(memberId);

            model.addAttribute("isLogin", true);
            model.addAttribute("applyCount", applyCount);

            socket = new Socket("localhost", 9092);

        }
        return "/chat/main";
    }

    @RequestMapping("/send")
    @ResponseBody
    public Map<String, Object> sendChat(@RequestParam String sendText) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        PrintWriter pw = null;
        BufferedReader br = null;

        try {
            // 서버에 요청 보내기
            if (this.socket == null || this.socket.isClosed()) {
                socket = new Socket("localhost", 9092);
            }
            System.out.println(socket.getInetAddress().getHostAddress() + "에 연결됨");
            
            // 메시지 전달
            pw = new PrintWriter(socket.getOutputStream());
            pw.println(sendText);
            pw.flush();

            // 메시지 받기
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String returnMsg = br.readLine();
            if (returnMsg != null) resultMap.put("message", returnMsg);
        } catch (IOException e) {
            resultMap.put("message", "채팅 목록 조회 중 오류가 발생하였습니다.");
            System.out.println(e.getMessage());
        } finally {
            // 소켓 닫기 (연결 끊기)
            try {
                if(socket != null) { socket.close(); }
                if(br != null) { br.close(); }
                if(pw != null) { pw.close(); }
            } catch (IOException e) {
                resultMap.put("message", "채팅 목록 조회 중 오류가 발생하였습니다.");
                System.out.println(e.getMessage());
            }
        }

        return resultMap;
    }

    public static byte[] toByteArray (Object obj)
    {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            bos.close();
            bytes = bos.toByteArray();
        }
        catch (IOException ex) {
            //TODO: Handle the exception
        }
        return bytes;
    }
}
