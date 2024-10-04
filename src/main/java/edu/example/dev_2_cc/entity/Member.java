package edu.example.dev_2_cc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString // 테스트에서 log로 찍을 때, 보기 쉽게 출력하기 위해서 추가
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Member {

    @Id
    private String memberId;

    @Email
    private String email;
    private String phoneNumber;
    private String name;
    private String password;
    private String sex;
    private String address;
    private String role;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Embedded // MeberImage의 필드들을 Member 테이블에 컬럼으로 포함시킴
    // 추후 MeberImage의 확장성을 고려하여 따로 엔티티 클래스로 생성(썸네일, 업로드 시간 등)
    @AttributeOverrides({
            @AttributeOverride(name = "filename", column = @Column(name = "member_image"))
    })       // Member 테이블의 Embedded 컬럼의 이름을 변경하기 위한 어노테이션
    private MemberImage image;


    public void addImage(String filename){
        this.image =MemberImage.builder().filename(filename).build();
    }

    public void clearImage(){ //
        this.image = null;
    }


    public void changeEmail(String email) {
        this.email = email;
    }

    public void changePhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeAddress(String address){
        this.address=address;
    }

    public void changeRole(String role){
        this.role = role;
    }

}
