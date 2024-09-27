package edu.example.dev_2_cc.controller;

import edu.example.dev_2_cc.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
@RequiredArgsConstructor
@Log4j2
public class ProductViewController {
    private final ProductService productService;

//    @GetMapping("/products")
//    public String productList() {
//        return "";
//    }

}
