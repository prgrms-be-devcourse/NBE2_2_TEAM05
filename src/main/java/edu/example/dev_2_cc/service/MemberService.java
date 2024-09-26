package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.member.MemberRequestDTO;
import edu.example.dev_2_cc.dto.member.MemberResponseDTO;
import edu.example.dev_2_cc.dto.member.MemberUpdateDTO;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.exception.MemberException;
import edu.example.dev_2_cc.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

        // DTO를 엔티티로 변환
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

        // 엔티티를 ResponseDTO로 변환하여 반환
        return toResponseDTO(savedMember);
    }

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

    //회원 정보 수정
    @Transactional
    public MemberResponseDTO modify(MemberUpdateDTO memberUpdateDTO) {
        Member member = memberRepository.findById(memberUpdateDTO.getMemberId())
                .orElseThrow(() -> MemberException.NOT_FOUND.get());
    }

}

// Service의 create메서드에(회원 가입) .build로 toEntity 구현
// Service에 toResponseDTO 구현
// 이미지 업로드 기능구현 시, 이름 "default_avatar.png"의 사진을 디텍토리에 추가