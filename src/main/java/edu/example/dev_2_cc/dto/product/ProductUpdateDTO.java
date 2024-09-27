package edu.example.dev_2_cc.dto.product;

import edu.example.dev_2_cc.entity.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDTO {

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

    public Product toEntity(){
        Product product = Product.builder().productId(productId).pName(pName)
                .price(price).description(description).stock(stock).build();

        if(images != null || !images.isEmpty()){
            images.forEach(product::addImage);
        }

        return product;
    }
}
