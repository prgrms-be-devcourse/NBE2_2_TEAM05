package edu.example.dev_2_cc.dto.product;

import edu.example.dev_2_cc.entity.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String filename;

    @Min(0)
    private int stock;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponseDTO(Product product) {
        this.productId = product.getProductId();
        this.pName = product.getPName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.filename = product.getFilename();
        this.stock = product.getStock();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
    }


}
