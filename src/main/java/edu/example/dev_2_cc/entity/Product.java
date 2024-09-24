package edu.example.dev_2_cc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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
    private String pName;
    private Long price;
    private String description;
    private String filename;
    private int stock;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void changePName(String pName) {
        this.pName = pName;
    }

    public void changePrice(Long price){
        this.price = price;
    }

    public void changeDescription(String description){
        this.description = description;
    }

    public void changeFilename(String filename){
        this.filename = filename;
    }

    public void changeStock(int stock){
        this.stock = stock;
    }

}
