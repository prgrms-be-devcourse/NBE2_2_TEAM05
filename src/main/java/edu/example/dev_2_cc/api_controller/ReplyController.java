package edu.example.dev_2_cc.api_controller;

import edu.example.dev_2_cc.dto.reply.ReplyResponseDTO;
import edu.example.dev_2_cc.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cc/reply")
@Log4j2
public class ReplyController {
    private final ReplyService replyService;

    @GetMapping("/{replyId}")
    public ResponseEntity<ReplyResponseDTO> getReply(@PathVariable("replyId") Long replyId) {
        return ResponseEntity.ok(replyService.read(replyId));
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Map<String, String>> deleteReply(@PathVariable("replyId") Long replyId) {
        replyService.delete(replyId);
        return ResponseEntity.ok(Map.of("message", "Reply deleted"));
    }
}
