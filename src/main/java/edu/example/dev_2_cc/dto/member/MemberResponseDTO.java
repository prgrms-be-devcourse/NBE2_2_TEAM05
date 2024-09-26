package edu.example.dev_2_cc.dto.member;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberResponseDTO {

    private String memberId;
    private String email;
    private String name;
    private String sex;
    private String address;
    private String profilePic;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
