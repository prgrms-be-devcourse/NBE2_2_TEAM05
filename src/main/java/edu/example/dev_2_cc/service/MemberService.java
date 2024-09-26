package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.member.MemberRequestDTO;
import edu.example.dev_2_cc.dto.member.MemberResponseDTO;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.exception.MemberException;
import edu.example.dev_2_cc.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 생성
    public MemberResponseDTO create(MemberRequestDTO memberRequestDTO) {
        // 프로필 사진이 제공되지 않았을 경우 기본값 설정
        String profilePic = memberRequestDTO.getProfilePic();
        if (profilePic == null || profilePic.isEmpty()) {
            profilePic = "default_avatar.png";
        }

        // MemberId 중복 시 Duplicate 예외 발생 -> APIControllerAdvice가 예외 처리
        Optional<Member> existingMember = memberRepository.findById(memberRequestDTO.getMemberId());
        if (existingMember.isPresent()) {
            throw MemberException.DUPLICATE.get();
        }

        // DTO를 엔티티로 변환하는 .build()
        Member member = Member.builder()
                .memberId(memberRequestDTO.getMemberId())
                .email(memberRequestDTO.getEmail())
                .name(memberRequestDTO.getName())
                .password(memberRequestDTO.getPassword())
                .sex(memberRequestDTO.getSex())
                .address(memberRequestDTO.getAddress())
                .profilePic(profilePic)
                .role("USER") // 디폴트 role -> USER 설정
                .build();

        // 회원 정보 저장
        Member savedMember = memberRepository.save(member);

        // 엔티티를 ResponseDTO로 변환하는 메서드 사용
        return toResponseDTO(savedMember);
        }

    // 회원 조회


    // 회원 전체 조회


    // 엔티티를 ResponseDTO로 변환하는 메서드
    private MemberResponseDTO toResponseDTO(Member member) {
        MemberResponseDTO responseDTO = new MemberResponseDTO();
        responseDTO.setMemberId(member.getMemberId());
        responseDTO.setEmail(member.getEmail());
        responseDTO.setName(member.getName());
        responseDTO.setSex(member.getSex());
        responseDTO.setAddress(member.getAddress());
        responseDTO.setProfilePic(member.getProfilePic());
        responseDTO.setRole(member.getRole());
        responseDTO.setCreatedAt(member.getCreatedAt());
        responseDTO.setUpdatedAt(member.getUpdatedAt());
        return responseDTO;
    }

}

// Service의 create메서드에(회원 가입) .build로 toEntity 구현
// Service에 toResponseDTO 구현
// 이미지 업로드 기능구현 시, 이름 "default_avatar.png"의 사진을 디텍토리에 추가

// 1. 이미지 업로드 설정 완료
// 2. 회원 조회, 전체 조회 구현
// 3. Mapper 구현 및 이해