package com.example.Thymeleaf.controller;

import com.example.Thymeleaf.dto.MemberDto;
import com.example.Thymeleaf.dto.ProductDto;
import com.example.Thymeleaf.service.MemberService;
import com.example.Thymeleaf.service.ProductService;
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
    final ProductService productService;

    @RequestMapping("/")
    public String getMain(@RequestParam(required = false) Boolean result,
                          Model model) throws Exception {
        model.addAttribute("productList", productService.getProductList());
        model.addAttribute("result", result);
        return "main";
    }

    @RequestMapping("/product/add/page")
    public String addProductPage(Model model, ProductDto productDto) {
        model.addAttribute("productDto", productDto);
        return "product/addProduct";
    }

    @RequestMapping(value = "/product/add", method = RequestMethod.POST)
    public String addProduct(ProductDto productDto, RedirectAttributes re) throws Exception {
        // Parameter productDto를 받을때 @RequestParam annotation을 사용하면 에러 발생
        // @ModelAttribute를 사용해야 한다. // 생략 가능

        String name = productDto.getProductName();
        int price = productDto.getProductPrice();
        String desc = productDto.getDescription();

        ProductDto addProduct = productService.addProduct(name, price, desc);

        // 아래 코드는 URL에 노출이 됨 GET 방식
        // re.addAttribute("addProduct", addProduct);
         re.addAttribute("result", true);

        // addFlashAttribute는 POST 방식이어야 함.
        // re.addFlashAttribute("addProduct", addProduct);
        // re.addFlashAttribute("result", true);
        return "redirect:/";
    }

    @RequestMapping(value = "/product/detail/{seq}")
    public String productDetail(@PathVariable int seq, Model model) throws Exception {
        ProductDto product = productService.getProduct(seq);
        model.addAttribute("product", product);
        return "/product/productDetail";
    }

    @RequestMapping(value = "/product/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteProduct(@RequestParam String itemSeqDatas) throws Exception {
        String[] arrItemSeq = itemSeqDatas.split(",");
        int deletedSize = productService.deleteProduct(arrItemSeq);

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("deletedSize", deletedSize);
        return returnMap;
    }

}
