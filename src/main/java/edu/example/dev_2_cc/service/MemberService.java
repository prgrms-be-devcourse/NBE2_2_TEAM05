package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.member.MemberRequestDTO;
import edu.example.dev_2_cc.dto.member.MemberResponseDTO;
import edu.example.dev_2_cc.dto.member.MemberUpdateDTO;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.MemberImage;
import edu.example.dev_2_cc.entity.ProductImage;
import edu.example.dev_2_cc.exception.MemberException;
import edu.example.dev_2_cc.exception.MemberTaskException;
import edu.example.dev_2_cc.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
        // 프로필 사진이 제공되지 않았거나 비어있을 경우 기본값 설정
        String imageFilename = (memberRequestDTO.getImage() == null || memberRequestDTO.getImage().isEmpty())
                ? "default_avatar.png"
                : memberRequestDTO.getImage();

        // MemberId 중복 시 Duplicate 예외 발생 -> APIControllerAdvice가 예외 처리
        Optional<Member> existingMember = memberRepository.findById(memberRequestDTO.getMemberId());
        if (existingMember.isPresent()) {
            throw MemberException.DUPLICATE.get();
        }

        // DTO를 엔티티로 변환하는 .build()
        Member member = Member.builder()
                .memberId(memberRequestDTO.getMemberId())
                .email(memberRequestDTO.getEmail())
                .phoneNumber(memberRequestDTO.getPhoneNumber())
                .name(memberRequestDTO.getName())
                .password(memberRequestDTO.getPassword())
                .sex(memberRequestDTO.getSex())
                .address(memberRequestDTO.getAddress())
                .role("USER") // 디폴트 role -> USER 설정
                .image(MemberImage.builder().filename(imageFilename).build()) // 단일 이미지 설정
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

    // 회원 정보 수정
    @Transactional
    public MemberResponseDTO modify(String memberId, MemberUpdateDTO updateRequestDTO) {
        // 컨트롤러에서 받아온 memberId값을 MemberUpdateDTO에 지정
        updateRequestDTO.setMemberId(memberId);

        // 수정하려는 멤버를 데이터베이스에서 조회
        Member member = memberRepository.findById(updateRequestDTO.getMemberId())
                .orElseThrow(() -> MemberException.NOT_FOUND.get());

        try {
            // 비밀번호 수정
            if (updateRequestDTO.getPassword() != null) {
                member.changePassword(updateRequestDTO.getPassword());
            }
            // 회원 이메일 수정
            if (updateRequestDTO.getEmail() != null) {
                member.changeEmail(updateRequestDTO.getEmail());
            }
            // 회원 이름 수정
            if (updateRequestDTO.getName() != null) {
                member.changeName(updateRequestDTO.getName());
            }
            // 회원 주소 수정
            if (updateRequestDTO.getAddress() != null) {
                member.changeAddress(updateRequestDTO.getAddress());
            }
            // 회원 사진 변경
            if (updateRequestDTO.getImage() != null) {
                member.addImage(updateRequestDTO.getImage());
            }
            // role 변경
            if (updateRequestDTO.getRole() != null) {
                member.changeRole(updateRequestDTO.getRole());
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
        responseDTO.setPhoneNumber(member.getPhoneNumber());
        responseDTO.setName(member.getName());
        responseDTO.setSex(member.getSex());
        responseDTO.setAddress(member.getAddress());
        responseDTO.setImage(member.getImage().getFilename()); // null일 경우가 없기 때문에 코드 간소화
        responseDTO.setRole(member.getRole());
        responseDTO.setCreatedAt(member.getCreatedAt());
        responseDTO.setUpdatedAt(member.getUpdatedAt());
        return responseDTO;
    }


}

// Service의 create메서드에(회원 가입) .build로 toEntity 구현
// Service에 toResponseDTO 구현
// 이미지 업로드 시, "default_avatar.png"파일을 디폴트 값으로 지정