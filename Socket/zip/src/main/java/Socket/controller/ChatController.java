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
import java.util.*;
import java.util.stream.Collectors;

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
    public Map<String, Object> sendChat(@RequestParam String sendText, HttpServletRequest request) throws IOException {
        HttpSession httpSession = request.getSession();
        String memberId = (String) httpSession.getAttribute("memberId");

        Map<String, Object> resultMap = new HashMap<>();
        PrintWriter pw = null;
        BufferedReader br = null;

        ArrayList<String> memberList = new ArrayList<>();
        ArrayList<String> msgList = new ArrayList<>();

        try {
            // ????????? ?????? ?????????
            if (this.socket == null || this.socket.isClosed()) {
                socket = new Socket("localhost", 9092);
            }
            System.out.println(socket.getInetAddress().getHostAddress() + "??? ?????????");
            
            // ????????? ??????
            Map<String, Object> dataMap = new HashMap();
            dataMap.put("memberId", memberId);
            dataMap.put("sendText", sendText);

            pw = new PrintWriter(socket.getOutputStream());
            pw.println(dataMap);
            pw.flush();

            // ????????? ??????
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String returnSocket = br.readLine();
            String[] split = returnSocket.split("/");

            for (String s : split) {
                if (!s.equals("")) {
                    String[] mapSplit = s.split(",");

                    String[] idSplit = mapSplit[0].split("=");
                    String[] msgSplit = mapSplit[1].split("=");

                    memberList.add(idSplit[1]);
                    msgList.add(msgSplit[1]);
                }
            }

            if (memberList.size() != msgList.size()) throw new IOException("????????? ?????? ??????");

            if (msgList.size() != 0) {
                resultMap.put("memberList", memberList);
                resultMap.put("msgList", msgList);
            } else {
                resultMap.put("error", "????????? ?????????????????????.");
            }
        } catch (IOException e) {
            resultMap.put("message", "?????? ?????? ?????? ??? ????????? ?????????????????????.");
            System.out.println(e.getMessage());
        } finally {
            // ?????? ?????? (?????? ??????)
            try {
                if(socket != null) { socket.close(); }
                if(br != null) { br.close(); }
                if(pw != null) { pw.close(); }
            } catch (IOException e) {
                resultMap.put("message", "?????? ?????? ?????? ??? ????????? ?????????????????????.");
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

