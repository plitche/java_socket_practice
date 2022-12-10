package Socket.controller;

import Socket.service.ChatService;
import ch.qos.logback.classic.net.server.ServerSocketReceiver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.management.ServiceNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    final ChatService chatService;
    private static Socket socket;

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

            if (applyCount <= 1) {
                socket = new Socket("localhost", 9091);
            }

            ServerSocket serverSocket = new ServerSocket(9992);
            Socket s = serverSocket.accept();

            System.out.println("??");

        }
        return "/chat/main";
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
