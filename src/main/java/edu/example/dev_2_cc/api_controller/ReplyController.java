package edu.example.dev_2_cc.api_controller;

import edu.example.dev_2_cc.dto.reply.ReplyListDTO;
import edu.example.dev_2_cc.dto.reply.ReplyRequestDTO;
import edu.example.dev_2_cc.dto.reply.ReplyResponseDTO;
import edu.example.dev_2_cc.dto.reply.ReplyUpdateDTO;
import edu.example.dev_2_cc.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/cc/reply")
@Log4j2
public class ReplyController {
    private final ReplyService replyService;

    //-----------------------------------------------댓글----------------------------------------------------

    // Reply 등록 -> ADMIN/USER
    @PostMapping("/")
    public ResponseEntity<ReplyResponseDTO> createReply(@RequestBody ReplyRequestDTO replyRequestDTO) {
        return ResponseEntity.ok(replyService.createReply(replyRequestDTO));
    }

    // Reply 수정 -> ADMIN/USER
    @PutMapping("/{replyId}")
    public ResponseEntity<ReplyResponseDTO> updateReply(@PathVariable Long replyId,
                                                        @Validated @RequestBody ReplyUpdateDTO replyUpdateDTO) {
        return ResponseEntity.ok(replyService.update(replyUpdateDTO));
    }

    // Reply 삭제 -> ADMIN/해당 댓글 USER/해당 게시판 USER
    @DeleteMapping("/{replyId}")
    public ResponseEntity<Map<String, String>> deleteReply(@PathVariable("replyId") Long replyId) {
        replyService.delete(replyId);
        return ResponseEntity.ok(Map.of("message", "Reply deleted"));
    }

    // Reply 전체 리스트 조회 -> ADMIN/USER
    @GetMapping("/listByMember/{memberId}") // Member ID 로 Reply 리스트 조회
    public ResponseEntity<List<ReplyListDTO>> listByMemberId(@Validated @PathVariable("memberId") String memberId) {
        List<ReplyListDTO> replies = replyService.listByMemberId(memberId);
        return ResponseEntity.ok(replies);
    }

    // 댓글 번호로 댓글 조회기능은 사용하지 않기에 일단 주석처리
//    @GetMapping("/{replyId}")
//    public ResponseEntity<ReplyResponseDTO> getReply(@PathVariable("replyId") Long replyId) {
//        return ResponseEntity.ok(replyService.read(replyId));
//    }

    // 게시판 번호로 댓글 전체 조회은 이미 게시판 단일 조회에 구현
//    @GetMapping("/listByBoard/{boardId}")
//    public ResponseEntity<List<ReplyListDTO>> listByBoardId(@Validated @PathVariable("boardId") Long boardId) {
//        List<ReplyListDTO> replies = replyService.list(boardId);
//        return ResponseEntity.ok(replies);
//    }

}