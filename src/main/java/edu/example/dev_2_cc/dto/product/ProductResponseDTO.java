package edu.example.dev_2_cc.dto.product;

import edu.example.dev_2_cc.entity.Product;
import edu.example.dev_2_cc.entity.ProductImage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    @NotEmpty
    private Long productId;

    @NotEmpty
    private String pName;

    @Min(0)
    private Long price;
    private String description;

    private List<String> images;

    @Min(0)
    private int stock;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponseDTO(Product product) {
        this.productId = product.getProductId();
        this.pName = product.getPName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.stock = product.getStock();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
        this.images = product.getImages()
                .stream().map(ProductImage::getFilename)
                .collect(Collectors.toList());
    }


}
