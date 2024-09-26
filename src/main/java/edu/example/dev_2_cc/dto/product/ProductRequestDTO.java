package edu.example.dev_2_cc.dto.product;

import edu.example.dev_2_cc.entity.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    private String pName;

    @Min(0)
    private Long price;

    private String description;
    private String filename;

    @Min(0)
    private int stock;

    public Product toEntity(){
        Product product = Product.builder().pName(pName)
                .price(price).description(description).stock(stock).filename(filename).build();

        return product;
    }


}
