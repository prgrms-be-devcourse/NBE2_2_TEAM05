package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.dto.product.ProductRequestDTO;
import edu.example.dev_2_cc.entity.Product;
import edu.example.dev_2_cc.repository.search.ProductSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> , ProductSearch {

    @Query("SELECT p FROM Product p JOIN FETCH p.images pi WHERE p.productId = :productId")
    Optional<Product> getProduct(@Param("productId") Long productId);

    //1. tbl_product와 tbl_product_image를 조인하여
    //   지정된 상품번호의 ProductDTO를 반환하는 getProductDTO 메서드
    @Query("SELECT p FROM Product p JOIN FETCH p.images pi WHERE p.productId = :productId")
    Optional<ProductRequestDTO> getProductDTO(@Param("productId") Long productId);

    @Query("SELECT p FROM Product p JOIN FETCH p.images pi")
    Page<ProductRequestDTO> getProductDTOFetch(Pageable pageable);

//    @Query("SELECT p FROM Product p JOIN FETCH p.images pi WHERE p.pName = :pName")
    List<Product> findBypNameContaining(String pName);

}
