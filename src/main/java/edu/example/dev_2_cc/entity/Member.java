package edu.example.dev_2_cc.entity;

import jakarta.validation.constraints.Email;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
//@ToString
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Member {
    @Id
    private String memberId;

    @Email
    private String email;
    private String name;
    private String password;
    private String sex;
    private String address;
    private String profilePic;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeSex(String sex){
        this.sex=sex;
    }

    public void changeAddress(String address){
        this.address=address;
    }

    public void changeProfilePic(String profilePic){
        this.profilePic=profilePic;
    }

}
