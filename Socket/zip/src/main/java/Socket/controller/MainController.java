package Socket.controller;

import Socket.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {

    final MemberService memberService;

    @RequestMapping("/")
    public String getMain(@RequestParam(required = false) Boolean result,
                          Model model) throws Exception {
        return "main";
    }

}
