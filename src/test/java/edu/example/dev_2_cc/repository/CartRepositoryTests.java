package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Cart;
import edu.example.dev_2_cc.entity.CartItem;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CartRepositoryTests {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;

    //등록 테스트
    @Test
    public void testCreate(){
        Member member = Member.builder()
                .memberId("testid")
                .email("test@email.com")
                .build();
        Member savedMember = memberRepository.save(member);

        Product product = Product.builder()
                .pName("테스트상품")
                .price(1000L)
                .description("상품설명ㅁㅁㅁ")
                .stock(10)
                .build();
        Product savedProduct = productRepository.save(product);


        Cart testCart = Cart.builder()
                .member(savedMember)
                .build();
        Cart savedCart = cartRepository.save(testCart);

        CartItem cartItem = CartItem.builder()
                .cart(savedCart)
                .product(savedProduct)
                .quantity(5)
                .build();
        CartItem savedCartItem = cartItemRepository.save(cartItem);

    }

    //조회 테스트(memberId 검색)
    @Test
    public void testRead(){
        Cart testCart = cartRepository.findByMemberId("testid").orElse(null);

    }

    //수정 테스트 - Cart 를 수정할 일은 없고 CartItem 의 수량 수정
    @Test
    public void testUpdate(){
        CartItem foundCartItem = cartItemRepository.findById(1L).orElse(null);
        foundCartItem.changeQuantity(25);
        cartItemRepository.save(foundCartItem);

    }

    //삭제 테스트
    @Test
    public void testDelete(){
        cartItemRepository.deleteById(2L);
    }



}
