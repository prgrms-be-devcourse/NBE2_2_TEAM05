package edu.example.dev_2_cc.api_controller;

import edu.example.dev_2_cc.dto.member.MemberRequestDTO;
import edu.example.dev_2_cc.dto.member.MemberResponseDTO;
import edu.example.dev_2_cc.dto.member.MemberUpdateDTO;
import edu.example.dev_2_cc.exception.MemberException;
import edu.example.dev_2_cc.exception.MemberTaskException;
import edu.example.dev_2_cc.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static edu.example.dev_2_cc.entity.QMember.member;

@RestController
@RequestMapping("/cc/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원 생성
    @PostMapping
    public ResponseEntity<MemberResponseDTO> createMember(
            @Validated @RequestBody MemberRequestDTO memberRequestDTO) {
        return ResponseEntity.ok(memberService.create(memberRequestDTO));
    }

    // 관리자의 단일 회원 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> getMember(
            @PathVariable String memberId
    ) {
        return ResponseEntity.ok(memberService.readMember(memberId));
    }

    // 마이페이지 내에서 회원의 직접 정보 수정
    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> updateMember(
            @PathVariable String memberId,
            @Validated @RequestBody MemberUpdateDTO memberUpdateDTO
    ) {
        //updateDTO에 memberId 설정
        memberUpdateDTO.setMemberId(memberId);
        //회원 정보 수정
        MemberResponseDTO response = memberService.modify(memberUpdateDTO);

        return ResponseEntity.ok(response);
    }

    // 마이페이지 내에서 회원 탈퇴
    @DeleteMapping("/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable String memberId) {
        try {
            memberService.delete(memberId); // 서비스에서 회원 삭제 로직 호출
            return ResponseEntity.noContent().build(); // 성공적으로 삭제된 경우 204 No Content 응답
        } catch (MemberTaskException e) {
            if (e.getMessage().equals(MemberException.NOT_FOUND.get().getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(("Error deleting member"));
        }
    }


}
