package edu.example.dev_2_cc.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.example.dev_2_cc.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListDTO {
    private Long productId;
    @JsonProperty("pName")
    private String pName;
    private Long price;
    //private String description; //리스트니까 제외
    private String pimage;
    private int stock;

    public ProductListDTO(Product product) {
        this.productId = product.getProductId();
        this.pName = product.getPName();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.pimage = product.getImages().first().getFilename();
    }
}
