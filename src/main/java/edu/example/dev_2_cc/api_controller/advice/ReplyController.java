package edu.example.dev_2_cc.api_controller.advice;

import edu.example.dev_2_cc.dto.reply.ReplyResponseDTO;
import edu.example.dev_2_cc.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
