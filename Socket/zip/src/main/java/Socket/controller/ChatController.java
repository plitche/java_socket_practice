package Socket.controller;

import Socket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.management.ServiceNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    final ChatService chatService;

    @RequestMapping()
    public String goChatPage(HttpServletRequest request,
                             Model model) throws ServiceNotFoundException {
        HttpSession httpSession = request.getSession();
        String memberId = (String) httpSession.getAttribute("memberId");

        if (memberId == null) {
            model.addAttribute("isLogin", false);
            /*throw new ServiceNotFoundException("Not login Exception - ChatController");*/
        } else {
            int applyCount = chatService.applyChat(memberId);

            model.addAttribute("isLogin", true);
            model.addAttribute("applyCount", applyCount);
        }

        return "/chat/main";
    }
}
