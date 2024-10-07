package edu.example.dev_2_cc.api_controller;

import edu.example.dev_2_cc.dto.member.MemberResponseDTO;
import edu.example.dev_2_cc.dto.member.MemberUpdateDTO;
import edu.example.dev_2_cc.service.MemberService;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import edu.example.dev_2_cc.dto.product.ProductRequestDTO;
import edu.example.dev_2_cc.dto.product.ProductResponseDTO;
import edu.example.dev_2_cc.dto.product.ProductUpdateDTO;
import edu.example.dev_2_cc.exception.ProductException;
import edu.example.dev_2_cc.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/cc/admin")
public class AdminController {
    private final ProductService productService;
    private final MemberService memberService;
  
    // 회원 리관리
    // 관리자의 회원 전체 조회
    @GetMapping("/memberlist")
    public ResponseEntity<List<MemberResponseDTO>> getAllMembers() {
        List<MemberResponseDTO> memberList = memberService.list();
        return ResponseEntity.ok(memberList);
    }

    // 관리자의 단일 회원 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> getMember(
            @PathVariable String memberId
    ) {
        return ResponseEntity.ok(memberService.readMember(memberId));
    }

    // 관리자의 회원 정보 수정(권한 수정 포함)
    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> updateMember(
            @PathVariable String memberId,
            @Validated @RequestBody MemberUpdateDTO memberUpdateDTO
    ) {
//        //updateDTO에 memberId 설정
//        memberUpdateDTO.setMemberId(memberId);

        //회원 정보 수정
        MemberResponseDTO response = memberService.modifyAdmin(memberId, memberUpdateDTO);

        return ResponseEntity.ok(response);
    }


    //상품 관리
    @PostMapping("/product")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.ok(productService.create(productRequestDTO));
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long productId, @RequestBody ProductUpdateDTO productUpdateDTO) {
        if(!productId.equals(productUpdateDTO.getProductId())) {
            throw ProductException.NOT_FOUND.get();
        }
        return ResponseEntity.ok(productService.update(productUpdateDTO));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable("productId") Long productId) {
        productService.delete(productId);
        return ResponseEntity.ok(Map.of("message", "Product deleted"));
    }
    

    
}
