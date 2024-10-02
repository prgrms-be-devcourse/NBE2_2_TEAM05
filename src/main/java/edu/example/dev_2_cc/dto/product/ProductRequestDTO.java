package edu.example.dev_2_cc.dto.product;

import edu.example.dev_2_cc.entity.Product;
import edu.example.dev_2_cc.entity.ProductImage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ProductRequestDTO {

    private String pName;

    @Min(0)
    private Long price;

    private String description;

    @Min(0)
    private int stock;

    private List<String> images;

    public ProductRequestDTO(Product product) {
        this.pName = product.getPName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.stock = product.getStock();
        this.images = product.getImages()
                .stream().map(ProductImage::getFilename)
                .collect(Collectors.toList());
    }

    public Product toEntity(){
        Product product = Product.builder().pName(pName)
                .price(price).description(description).stock(stock).build();

        if(images != null && !images.isEmpty()){
            images.forEach(product::addImage);
        }

        product.addImage("default.jpg");

        return product;
    }


}
