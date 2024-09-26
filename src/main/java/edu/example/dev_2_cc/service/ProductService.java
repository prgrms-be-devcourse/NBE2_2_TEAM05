package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.product.ProductRequestDTO;
import edu.example.dev_2_cc.dto.product.ProductResponseDTO;
import edu.example.dev_2_cc.dto.product.ProductUpdateDTO;
import edu.example.dev_2_cc.entity.Product;
import edu.example.dev_2_cc.exception.ProductException;
import edu.example.dev_2_cc.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public ProductResponseDTO read(Long productId) {
        try{
            Optional<Product> foundProduct = productRepository.findById(productId);
            Product product = foundProduct.get();
            return new ProductResponseDTO(product);
        }catch (Exception e){
            log.error(e.getMessage());
            throw ProductException.NOT_FOUND.get();
        }

    }

    public ProductResponseDTO update(ProductUpdateDTO productUpdateDTO) {
        Optional<Product> foundProduct = productRepository.findById(productUpdateDTO.getProductId());
        Product product = foundProduct.orElseThrow(ProductException.NOT_FOUND::get);

        try{
            product.changePName(productUpdateDTO.getPName());
            product.changePrice(productUpdateDTO.getPrice());
            product.changeDescription(productUpdateDTO.getDescription());
            product.changeStock(productUpdateDTO.getStock());
            product.changeFilename(productUpdateDTO.getFilename());

            return new ProductResponseDTO(productRepository.save(product));
        }catch (Exception e){
            log.error(e.getMessage());
            throw ProductException.NOT_UPDATED.get();
        }
    }

    public void delete(Long productId) {
        Optional<Product> foundProduct = productRepository.findById(productId);
        Product product = foundProduct.orElseThrow(ProductException.NOT_FOUND::get);

        try{
            productRepository.delete(product);
        }catch (Exception e){
            log.error("--- " + e.getMessage());
            throw ProductException.NOT_DELETED.get();
        }

    }


}
