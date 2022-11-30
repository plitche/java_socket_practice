package com.example.Thymeleaf.controller;

import com.example.Thymeleaf.dto.MemberDto;
import com.example.Thymeleaf.service.MemberService;
import com.example.Thymeleaf.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    final MemberService memberService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@RequestParam String id,
                     @RequestParam String password,
                     HttpServletRequest request,
                     Model model) throws Exception {
//        System.out.println("id = " + id);
//        System.out.println("password = " + password);

        Map<String, Object> returnMap = new HashMap<>();
        try {
            MemberDto memberDto = memberService.login(id, password);

            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("memberId", memberDto.getId());

            returnMap.put("result", true);
            returnMap.put("message", "로그인에 성공하셨습니다.");
        } catch (Exception e) {
            returnMap.put("result", false);
            returnMap.put("message", "로그인에 실패하셨습니다. 다시 시도해주세요.");
        }

        return returnMap;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public void logout(HttpServletRequest request) throws Exception {
        try {
            HttpSession httpSession = request.getSession();
            httpSession.removeAttribute("memberId");

        } catch (Exception e) {
        }
    }
}
