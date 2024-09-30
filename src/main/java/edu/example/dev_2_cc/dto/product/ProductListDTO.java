package edu.example.dev_2_cc.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListDTO {
    private Long productId;
    private String pName;
    private Long price;
    //private String description; //리스트니까 제외
    private String pimage;
    private int stock;
}
