package edu.example.dev_2_cc.repository.search;

import edu.example.dev_2_cc.dto.product.ProductListDTO;
import edu.example.dev_2_cc.dto.product.ProductRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductSearch {
    Page<ProductListDTO> list(Pageable pageable);
    Page<ProductRequestDTO> listWithAllImages(Pageable pageable);
}
