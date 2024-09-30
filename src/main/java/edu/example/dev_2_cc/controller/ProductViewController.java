package edu.example.dev_2_cc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/product")
@Log4j2
public class ProductViewController {
    @GetMapping()
    public String productView() {
        return "product-list";
    }

    @GetMapping("/{productId}")
    public String productView(@PathVariable String productId, Model model) {
        model.addAttribute("productId", productId);
        return "product";
    }

}
