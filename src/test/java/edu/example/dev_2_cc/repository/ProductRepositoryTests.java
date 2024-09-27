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
                .pName("테스트 상품5")
                .price(9000L)
                .description("테스트 상품5 설명")
                .filename("testProduct5.png")
                .stock(6).build();

        Product savedProduct = productRepository.save(product);
        assertNotNull(savedProduct);

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
        Long productId = 2L;
        String pName = "수정테스트상품";
        Long price = 10000L;
        String description = "수정설명테스트";
        String filename = "modifiedProduct.png";
        int stock = 15;

        Optional<Product> foundProduct = productRepository.findById(productId);
        assertTrue(foundProduct.isPresent(), "Product should be present");

        Product product = foundProduct.get();
        product.changePName(pName);
        product.changePrice(price);
        product.changeDescription(description);
        product.changeStock(stock);
        product.changeFilename(filename);

        foundProduct = productRepository.findById(productId);
        assertEquals(pName, foundProduct.get().getPName());
        assertEquals(price, foundProduct.get().getPrice());
        assertEquals(description, foundProduct.get().getDescription());
        assertEquals(stock, foundProduct.get().getStock());
        assertEquals(filename, foundProduct.get().getFilename());

    }

    @Test
    @Transactional
    @Commit
    public void testDelete(){
        Long productId = 5L;
        assertTrue(productRepository.findById(productId).isPresent(), "Product should be present");

        productRepository.deleteById(productId);
        assertFalse(productRepository.findById(productId).isPresent(), "Product should not be present");
    }

    @Test
    @Transactional(readOnly = true)
    public void testList(){

    }


}
