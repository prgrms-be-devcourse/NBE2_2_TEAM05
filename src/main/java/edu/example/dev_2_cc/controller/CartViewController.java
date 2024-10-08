package edu.example.dev_2_cc.controller;

import edu.example.dev_2_cc.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/cart")
@RequiredArgsConstructor
@Log4j2
public class CartViewController {
    private final CartService cartService;

    @GetMapping()
    public String getCart() {
        return "cart";
    }


}
