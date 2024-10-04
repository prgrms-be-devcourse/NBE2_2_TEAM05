package edu.example.dev_2_cc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @JsonProperty("pName")
    @Column(name = "p_name")
    private String pName;
    private Long price;
    private String description;

    @ElementCollection(fetch = FetchType.LAZY)  // 지연 로딩
    @CollectionTable(name="product_image", joinColumns = @JoinColumn(name="productId"))
    @Builder.Default
    @BatchSize(size = 100)
    private SortedSet<ProductImage> images = new TreeSet<>();

    private int stock;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    //Product 이미지 추가
    public void addImage(String filename){
        ProductImage productImage = ProductImage.builder().filename(filename).ino(images.size()).build();
        images.add(productImage);
    }

    //Product 이미지 제거
    public void clearImages(){
        images.clear();
    }


    public void changePName(String pName) {
        this.pName = pName;
    }

    public void changePrice(Long price){
        this.price = price;
    }

    public void changeDescription(String description){
        this.description = description;
    }

    public void changeStock(int stock){
        this.stock = stock;
    }

}
