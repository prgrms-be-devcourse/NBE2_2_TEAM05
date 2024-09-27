package edu.example.dev_2_cc.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateDTO {

    @NotBlank(message = "회원 ID는 필수 항목입니다.")
    private String memberId;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    private String name;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;

    private String sex;

    private String address;

    private String profilePic;

    private String role;


}

// 클라이언트가 Update 할 때 필요한 바꾸고 싶은 부분만 입력받고,
// 따로 기입하지 않은 부분은 원래 값을 그대로 유지하기 위해서 검증 어노테이션 붙이지 않음

