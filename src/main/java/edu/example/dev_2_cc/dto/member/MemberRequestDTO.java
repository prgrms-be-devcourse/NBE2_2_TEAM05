package edu.example.dev_2_cc.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRequestDTO {

    @NotBlank(message = "회원 ID는 필수 항목입니다.")
    private String memberId;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;

    @NotBlank(message = "이름은 필수 항목입니다.")
    private String name;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;

    @NotBlank(message = "성별은 필수 항목입니다.")
    private String sex;

    @NotBlank(message = "주소는 필수 항목입니다.")
    private String address;

    // 아바타 프로필 입력하지 않을 시, 디폴트 프로필 기능 구현 필요
    private String profilePic;

}
