package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Product;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Log4j2
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testCreate(){
        Product product = Product.builder()
                .pName("test2")
                .price(9000L)
                .description("test2 설명")
                .stock(6)
                .build();

        product.addImage("test.png");
        product.addImage("s_test.png");

        Product savedProduct = productRepository.save(product);
        assertNotNull(savedProduct);
        assertNotNull(savedProduct.getImages());
        assertTrue(savedProduct.getImages().contains("test.png"));
        assertTrue(savedProduct.getImages().contains("s_test.png"));
    }

    @Test
    @Transactional(readOnly = true)
    public void testRead(){
        Long productId = 1L;

        Optional<Product> foundProduct = productRepository.findById(productId);
        assertTrue(foundProduct.isPresent(), "Product should be present");
        assertEquals(productId, foundProduct.get().getProductId());
    }

    @Test
    @Commit
    @Transactional
    public void testUpdate(){
        Long productId = 1L;
        String pName = "test";
        Long price = 10000L;
        String description = "수정설명테스트";
        int stock = 15;

        Optional<Product> foundProduct = productRepository.findById(productId);
        assertTrue(foundProduct.isPresent(), "Product should be present");

        Product product = foundProduct.get(); // Optional 벗기기

        product.clearImages(); // ProductImage 초기화
        product.addImage("test2.png");
        product.addImage("s_test2.png");

        product.changePName(pName);
        product.changePrice(price);
        product.changeDescription(description);
        product.changeStock(stock);

        assertEquals(pName, product.getPName());
        assertEquals(price, product.getPrice());
        assertEquals(description, product.getDescription());
        assertEquals(stock, product.getStock());

        assertTrue(product.getImages().stream()
                .anyMatch(image -> "test2.png".equals(image.getFilename())), "Image 'test2.png' should be present");
        assertTrue(product.getImages().stream()
                .anyMatch(image -> "s_test2.png".equals(image.getFilename())), "Image 's_test2.png' should be present");
    }

    @Test
    @Transactional
    @Commit
    public void testDelete(){
        Long productId = 2L;
        assertTrue(productRepository.findById(productId).isPresent(), "Product should be present");

        productRepository.deleteById(productId);
        assertFalse(productRepository.findById(productId).isPresent(), "Product should not be present");
    }

    @Test
    @Transactional(readOnly = true)
    public void testList(){
        List<Product> foundAllProduct = productRepository.findAll();
        assertFalse(foundAllProduct.isEmpty(), "Product should be present");
        log.info("---foundAllProduct : " + foundAllProduct);
    }

}
