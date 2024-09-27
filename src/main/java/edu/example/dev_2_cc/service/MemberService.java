package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.member.MemberRequestDTO;
import edu.example.dev_2_cc.dto.member.MemberResponseDTO;
import edu.example.dev_2_cc.dto.member.MemberUpdateDTO;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.exception.MemberException;
import edu.example.dev_2_cc.exception.MemberTaskException;
import edu.example.dev_2_cc.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public MemberResponseDTO readMember(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> MemberException.NOT_FOUND.get());

        return toResponseDTO(member);
    }

    // 회원 전체 조회
    public List<MemberResponseDTO> list() {
        // 데이터베이스에서 모든 회원을 조회
        List<Member> memberList = memberRepository.findAll();

        // 조회된 회원 목록이 비어있는지 확인
        if (memberList.isEmpty()) {
            throw MemberException.NOT_FOUND.get();
        }

        // 각 Member 엔티티를 MemberResponseDTO로 변환하여 리스트로 반환
        return memberList.stream()
                         .map(this::toResponseDTO)
                         .collect(Collectors.toList());
    }



    //회원 정보 수정
    @Transactional
    public MemberResponseDTO modify(MemberUpdateDTO request) {
        //수정하려는 멤버를 데이터베이스에서 조회
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> MemberException.NOT_FOUND.get());

        try {
            // 비밀번호 수정
            if (request.getPassword() != null) {
                member.changePassword(request.getPassword());
            }
            // 회원 이메일 수정
            if (request.getEmail() != null) {
                member.changeEmail(request.getEmail());
            }
            // 회원 이름 수정
            if (request.getName() != null) {
                member.changeName(request.getName());
            }
            // 회원 주소 수정
            if (request.getAddress() != null) {
                member.changeAddress(request.getAddress());
            }
            // 회원 사진 변경
            if (request.getProfilePic() != null) {
                member.changeProfilePic(request.getProfilePic());
            }
            // role 변경
            if (request.getRole() != null) {
                member.changeRole(request.getRole());
            }
            // 수정한 회원 정보 저장
            Member modifiedMember = memberRepository.save(member);

            // 엔티티를 dto로 변환
            return toResponseDTO(modifiedMember);
        } catch (Exception e) {
            log.error("Error modifying member : " + e.getMessage());
            throw MemberException.NOT_MODIFIED.get();
        }
    }

    // 회원 삭제
    @Transactional
    public void delete(String id) {
        // 삭제히려는 멤버를 데이터베이스에서 조회
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> MemberException.NOT_FOUND.get());

        try {
            //회원 삭제
            memberRepository.delete(member);
        } catch (Exception e) {
            log.error("Error removing member: " + e.getMessage());
            throw MemberException.NOT_REMOVED.get();
        }

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


}

// Service의 create메서드에(회원 가입) .build로 toEntity 구현
// Service에 toResponseDTO 구현
// 이미지 업로드 기능구현 시, 이름 "default_avatar.png"의 사진을 디텍토리에 추가