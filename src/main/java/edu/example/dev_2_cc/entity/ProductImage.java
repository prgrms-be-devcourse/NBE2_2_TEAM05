package edu.example.dev_2_cc.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ProductImage implements Comparable<ProductImage> {

    private int ino;
    private String filename;

    @Override
    public int compareTo(ProductImage o) {
        return this.ino - o.ino;
    }
}
