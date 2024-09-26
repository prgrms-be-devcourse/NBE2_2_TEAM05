package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.product.ProductRequestDTO;
import edu.example.dev_2_cc.dto.product.ProductResponseDTO;
import edu.example.dev_2_cc.entity.Product;
import edu.example.dev_2_cc.exception.ProductException;
import edu.example.dev_2_cc.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponseDTO create(ProductRequestDTO productRequestDTO) {
        try{
            Product product = productRequestDTO.toEntity();
            Product savedProduct = productRepository.save(product);

            return new ProductResponseDTO(savedProduct);
        }catch (Exception e){
            log.error(e.getMessage());
            throw ProductException.NOT_CREATED.get();
        }
    }


}
