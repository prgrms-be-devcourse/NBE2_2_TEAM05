package edu.example.dev_2_cc.api_controller;

import edu.example.dev_2_cc.dto.member.MemberResponseDTO;
import edu.example.dev_2_cc.dto.member.MemberUpdateDTO;
import edu.example.dev_2_cc.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cc/admin")
@RequiredArgsConstructor
public class AdminController {
    private final MemberService memberService;


    // 관리자의 회원 전체 조회
    @GetMapping("/memberlist")
    public ResponseEntity<List<MemberResponseDTO>> getAllMembers() {
        List<MemberResponseDTO> memberList = memberService.list();
        return ResponseEntity.ok(memberList);
    }

    // 관리자의 단일 회원 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> getMember(
            @PathVariable String memberId
    ) {
        return ResponseEntity.ok(memberService.readMember(memberId));
    }

    // 관리자의 회원 정보 수정(권한 수정 포함)
    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> updateMember(
            @PathVariable String memberId,
            @Validated @RequestBody MemberUpdateDTO memberUpdateDTO
    ) {
//        //updateDTO에 memberId 설정
//        memberUpdateDTO.setMemberId(memberId);

        //회원 정보 수정
        MemberResponseDTO response = memberService.modifyAdmin(memberId, memberUpdateDTO);

        return ResponseEntity.ok(response);
    }

}
