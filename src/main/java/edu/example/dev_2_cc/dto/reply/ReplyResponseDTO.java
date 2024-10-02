package edu.example.dev_2_cc.dto.reply;

import edu.example.dev_2_cc.entity.Reply;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReplyResponseDTO {
    private Long replyId;
    private Long boardId;
    private String memberId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReplyResponseDTO(Reply reply) {
        this.replyId = reply.getReplyId();
        this.boardId = reply.getBoard().getBoardId();
        this.memberId = reply.getMember().getMemberId();
        this.content = reply.getContent();
        this.createdAt = reply.getCreatedAt();
        this.updatedAt = reply.getUpdatedAt();
    }

}

