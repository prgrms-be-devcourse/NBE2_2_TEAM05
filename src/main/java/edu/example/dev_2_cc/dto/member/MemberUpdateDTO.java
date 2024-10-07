package edu.example.dev_2_cc.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateDTO {

//    @NotBlank(message = "회원 ID는 필수 항목입니다.")
//    Body에 값으로 전달하지 않기 때문에 검증 어노테이션 주석처리
    private String memberId;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    //@NotEmpty(message = "이메일은 빈 문자열일 수 없습니다.")
    private String email;

    //@NotEmpty(message = "전화번호는 빈 문자열일 수 없습니다.")
    private String phoneNumber;

    //@NotEmpty(message = "이름은 빈 문자열일 수 없습니다.")
    private String name;

    //@NotEmpty(message = "비밀번호는 빈 문자열일 수 없습니다.")
    private String password;

    //@NotEmpty(message = "성별은 빈 문자열일 수 없습니다.")
    private String sex;

    //@NotEmpty(message = "주소는 빈 문자열일 수 없습니다.")
    private String address;

    //@NotEmpty(message = "역할은 빈 문자열일 수 없습니다.")
    private String role;

    private String image;
}

// 클라이언트가 Update 할 때 필요한 바꾸고 싶은 부분만 입력받고,
// 따로 기입하지 않은 부분은 원래 값을 그대로 유지하기 위해서 검증 어노테이션 붙이지 않음

