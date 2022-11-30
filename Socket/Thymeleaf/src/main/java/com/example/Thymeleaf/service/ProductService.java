package com.example.Thymeleaf.service;

import com.example.Thymeleaf.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private Integer seq = 0;
    private List<ProductDto> productDtoList = new ArrayList<>();

    public ProductService() {
        this.productDtoList.add(new ProductDto(++seq, "java", 10000, "자바 책 입니다."));
        this.productDtoList.add(new ProductDto(++seq, "spring", 20000, "스프링 책 입니다."));
        this.productDtoList.add(new ProductDto(++seq, "node", 15000, "노드 책 입니다."));
        this.productDtoList.add(new ProductDto(++seq, "thymeleaf", 30000, "타임리프 책 입니다."));
    }

    public List<ProductDto> getProductList() {
        return this.productDtoList;
    }

    public ProductDto getProduct(int seq) throws Exception {
//        for (ProductDto productDto : this.productDtoList) {
//            System.out.println("productDto = " + productDto.getProductSeq());
//        }

        return this.productDtoList.stream()
                .filter(productDto -> productDto.getProductSeq() == seq)
                .findFirst()
                .orElseThrow(() -> new Exception("bad product seq parameter ERROR"));
    }

    public ProductDto addProduct(String productName, int price, String desc) throws Exception {
        int seq = this.seq + 1;
        ProductDto productDto = new ProductDto(seq, productName, price, desc);
        this.productDtoList.add(productDto);
        return getProduct(seq);
    }

    public int deleteProduct(String[] arrItemSeq) {
        List<ProductDto> tempList = this.productDtoList;

        for (String itemSeq : arrItemSeq) {
            tempList = tempList.stream()
                    .filter(item -> item.getProductSeq() != Integer.parseInt(itemSeq.trim())).collect(Collectors.toList());
        }

        int deletedSize = this.productDtoList.size() - tempList.size();
        this.productDtoList = tempList;
        return deletedSize;
    }
}
