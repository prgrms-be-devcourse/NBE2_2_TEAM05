package edu.example.dev_2_cc.api_controller;

import edu.example.dev_2_cc.dto.product.*;
import edu.example.dev_2_cc.exception.ProductException;
import edu.example.dev_2_cc.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cc/product")
@Log4j2
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.ok(productService.create(productRequestDTO));

    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.read(productId));

    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable("productId") Long productId, @RequestBody ProductUpdateDTO productUpdateDTO) {

        if(!productId.equals(productUpdateDTO.getProductId())) {
            throw ProductException.NOT_FOUND.get();
        }

        return ResponseEntity.ok(productService.update(productUpdateDTO));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable("productId") Long productId) {
        // ADMIN role인지 아닌지 확인하는 코드? 여기다가 넣는 것 같습니다

        productService.delete(productId);

        return ResponseEntity.ok(Map.of("message", "Product deleted"));
    }

    @GetMapping                         //목록
    public ResponseEntity<Page<ProductListDTO>> getProductList(@Validated PageRequestDTO pageRequestDTO){
        log.info("getList() ----- " + pageRequestDTO);         //로그로 출력
        return ResponseEntity.ok(productService.getProductList(pageRequestDTO));
    }
}
